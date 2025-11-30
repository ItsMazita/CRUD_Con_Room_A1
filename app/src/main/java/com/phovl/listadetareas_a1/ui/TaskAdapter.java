package com.phovl.listadetareas_a1.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phovl.listadetareas_a1.AddEditTaskActivity;
import com.phovl.listadetareas_a1.R;
import com.phovl.listadetareas_a1.data.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private final TaskViewModel taskViewModel; // para actualizar Room

    // Constructor recibe el ViewModel
    public TaskAdapter(TaskViewModel viewModel) {
        this.taskViewModel = viewModel;
    }

    // Actualizar la lista
    public void submitList(List<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.title.setText(task.getTaskTitle());
        holder.description.setText(task.getTaskDescription());

        //Limpia el listener antes de setChecked para evitar disparos dobles
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());

        // Listener para cambios en el CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            taskViewModel.update(task); // actualiza en Room
        });

        //Menú flotante al presionar los tres puntos
        holder.btnEdit.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnEdit);
            popup.getMenuInflater().inflate(R.menu.task_item_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_edit) {

                    //Abrir AddEditTaskActivity con datos
                    Intent intent = new Intent(v.getContext(), AddEditTaskActivity.class);
                    intent.putExtra("task_id", task.getId());
                    intent.putExtra("task_title", task.getTaskTitle());
                    intent.putExtra("task_description", task.getTaskDescription());
                    intent.putExtra("task_completed", task.isCompleted());
                    intent.putExtra("task_created_at", task.getCreatedAt());
                    v.getContext().startActivity(intent);
                    return true;
                } else if (id == R.id.action_delete) {

                    // Eliminar tarea
                    taskViewModel.delete(task);
                    Toast.makeText(v.getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        CheckBox checkBox;
        ImageButton btnEdit;

        TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            checkBox = itemView.findViewById(R.id.checkBox);

            // botón de tres puntos
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
