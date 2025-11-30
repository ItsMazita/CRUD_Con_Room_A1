package com.phovl.listadetareas_a1.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import com.phovl.listadetareas_a1.data.Task;

@Dao
public interface TaskDao {

    //Insertar una nueva task
    @Insert
    void insert(Task task);

    //Actualizar una task
    @Update
    void update(Task task);

    //Eliminar una task
    @Delete
    void delete(Task task);

    //Obtenemos todas las task ordenadas en orden ascendente de id
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    LiveData<List<Task>> getAllTasks();

}
