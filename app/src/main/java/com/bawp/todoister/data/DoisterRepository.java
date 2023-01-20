package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class DoisterRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public DoisterRepository(Application application) {
        //handle RoomDb
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);

        //fetch a taskDao obj
        taskDao = database.taskDao();

        //fill out allTasks with all tasks from task_table
        allTasks = taskDao.getTasks();
    }

    // get all Tasks method
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    // insert method
    public void insert(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()-> taskDao.insertTask(task));
    }

    // get one Task method
    public LiveData<Task> get(long id){ return taskDao.get(id);}

    // update method
    public void update(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()-> taskDao.update(task));
    }

    // delete method
    public void delete(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()-> taskDao.delete(task));
    }

}
