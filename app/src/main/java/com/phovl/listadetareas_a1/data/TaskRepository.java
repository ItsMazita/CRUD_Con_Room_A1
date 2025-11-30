package com.phovl.listadetareas_a1.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    private final ExecutorService executorService;

    //Constructor: Inicializa la base de datos y el Dao
    public TaskRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();

        //Maneja operaciones de fondo
        executorService = Executors.newSingleThreadExecutor();
    }

    //Exponemos las taks observables
    public LiveData<List<Task>> getAllTasks(    ) {
        return allTasks;
    }

    //Operaciones CRUD
    public void insert(Task task){
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task){
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task){
        executorService.execute(() -> taskDao.delete(task));
    }

}
