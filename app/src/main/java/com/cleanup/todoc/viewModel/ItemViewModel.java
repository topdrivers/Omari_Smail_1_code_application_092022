package com.cleanup.todoc.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ItemDataRepository;
import com.cleanup.todoc.repository.ProjectDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ItemViewModel extends ViewModel {


    // REPOSITORIES
    private final ItemDataRepository itemDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;


    // DATA
    @Nullable
    private LiveData <List<Project>> currentProject;

    public ItemViewModel(ItemDataRepository itemDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.itemDataSource = itemDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }

    public void init() {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject();
    }

    // -------------
    // FOR Project
    // -------------

    public LiveData <List<Project>> getProjects() { return projectDataSource.getProject();  }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Task>> getItems() {
        return itemDataSource.getItems();
    }


    public void createItem(Task task) {
        executor.execute(() -> itemDataSource.createItem(task));
    }

    public void deleteItem(long itemId) {
        executor.execute(() -> itemDataSource.deleteItem(itemId));
    }

    public void updateItem(Task task) {
        executor.execute(() -> itemDataSource.updateItem(task));
    }

    public LiveData<List<Task>> sortAZItem(){
        executor.execute(itemDataSource::sortAZItem);
        return itemDataSource.sortAZItem();
    }


    public LiveData<List<Task>> sortZAItem() {
        executor.execute(itemDataSource::sortZAItem);
        return itemDataSource.sortZAItem();
    }

    public LiveData<List<Task>> sortOldItem() {
        executor.execute(itemDataSource::sortOldItem);
        return itemDataSource.sortOldItem();
    }

    public LiveData<List<Task>> sortRecentItem() {
        executor.execute(itemDataSource::sortRecentItem);
        return itemDataSource.sortRecentItem();
    }


}
