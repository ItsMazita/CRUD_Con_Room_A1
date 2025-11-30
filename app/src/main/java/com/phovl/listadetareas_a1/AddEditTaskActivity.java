package com.phovl.listadetareas_a1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.phovl.listadetareas_a1.data.Task;
import com.phovl.listadetareas_a1.ui.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private CheckBox checkCompleted;
    private TaskViewModel taskViewModel;
    private TextView textCreatedAt;
    private Task taskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_task);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        checkCompleted = findViewById(R.id.checkCompleted);
        textCreatedAt = findViewById(R.id.textCreatedAt);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        //Revisamos si vienen datos para editar
        Intent intent = getIntent();
        if (intent.hasExtra("task_id")) {
            taskToEdit = new Task();
            taskToEdit.setId(intent.getIntExtra("task_id", -1));
            taskToEdit.setTaskTitle(intent.getStringExtra("task_title"));
            taskToEdit.setTaskDescription(intent.getStringExtra("task_description"));
            taskToEdit.setCompleted(intent.getBooleanExtra("task_completed", false));
            taskToEdit.setCreatedAt(intent.getStringExtra("task_created_at"));

            //Mostramos los datos en la UI
            editTitle.setText(taskToEdit.getTaskTitle());
            editDescription.setText(taskToEdit.getTaskDescription());
            checkCompleted.setChecked(taskToEdit.isCompleted());
            textCreatedAt.setText("Creada el: " + taskToEdit.getCreatedAt());
        } else {

            //nueva tarea mostrar fecha actual como referencia
            String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date());
            textCreatedAt.setText("Se creará el: " + currentDate);
        }

        //Botón para volver atrás
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });

        //Botón para guardar
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            //Validación del título
            if (title.isEmpty()) {
                editTitle.setError("El título no puede estar vacío");
                Toast.makeText(this, "Debes ingresar un título", Toast.LENGTH_SHORT).show();
                return;
            }

            if (taskToEdit == null) {
                Task newTask = new Task();
                newTask.setTaskTitle(title);
                newTask.setTaskDescription(description);
                newTask.setCompleted(checkCompleted.isChecked());

                //Asignamos fecha automáticamente
                String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(new Date());
                newTask.setCreatedAt(currentDate);

                taskViewModel.insert(newTask);
            } else {
                taskToEdit.setTaskTitle(title);
                taskToEdit.setTaskDescription(description);
                taskToEdit.setCompleted(checkCompleted.isChecked());
                //Mantener la fecha original
                taskViewModel.update(taskToEdit);
            }
            finish();
        });
    }
}
