package com.phovl.listadetareas_a1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phovl.listadetareas_a1.data.Task;
import com.phovl.listadetareas_a1.ui.TaskAdapter;
import com.phovl.listadetareas_a1.ui.TaskViewModel;
import com.phovl.listadetareas_a1.AddEditTaskActivity;


public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Inicializar ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        //Inicializar Adapter y conectarlo
        adapter = new TaskAdapter(taskViewModel);
        recyclerView.setAdapter(adapter);

        //Observar lista de tasks desde Room
        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
        });

        //Botón para agregar una nueva task
        findViewById(R.id.imageView).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });


    }
}
