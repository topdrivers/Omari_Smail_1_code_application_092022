package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM Task" )
    LiveData<List<Task>> getItems();

    @Insert
    void insertItem(Task task);


    @Update
    void updateItem(Task task);


    @Query("DELETE FROM Task WHERE id = :itemId")
    void deleteItem(long itemId);


    @Query("SELECT * FROM Task ORDER BY name ASC")
    LiveData<List<Task>> sortAZItem();


    @Query("SELECT * FROM Task ORDER BY name DESC")
    LiveData<List<Task>> sortZAItem();


    @Query("SELECT * FROM Task ORDER BY creationTimestamp DESC")
    LiveData<List<Task>> sortRecentItem();


    @Query("SELECT * FROM Task ORDER BY creationTimestamp ASC")
    LiveData<List<Task>> sortOldItem();

}