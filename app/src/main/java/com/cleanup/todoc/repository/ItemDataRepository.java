package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;


import com.cleanup.todoc.database.dao.ItemDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * Created by Philippe on 27/02/2018.
 */

public class ItemDataRepository {

    private final ItemDao itemDao;

    public ItemDataRepository(ItemDao itemDao) { this.itemDao = itemDao; }

    // --- GET ---

    public LiveData<List<Task>> getItems(){ return this.itemDao.getItems(); }

    // --- CREATE ---

    public void createItem(Task task){ itemDao.insertItem(task); }

    // --- DELETE ---
    public void deleteItem(long itemId){ itemDao.deleteItem(itemId); }

    // --- UPDATE ---
    public void updateItem(Task task){ itemDao.updateItem(task); }

    //sort AZ item
    public LiveData<List<Task>> sortAZItem(){return itemDao.sortAZItem();}

    //sort ZA item
    public LiveData<List<Task>> sortZAItem() {return itemDao.sortZAItem();}

    //sort old item
    public LiveData<List<Task>> sortOldItem() {return itemDao.sortOldItem();}

    //sort Recent item
    public LiveData<List<Task>> sortRecentItem() {return itemDao.sortRecentItem();}


}

