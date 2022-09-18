package com.cleanup.todoc.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import java.util.List;

/**
 * Created by Philippe on 28/02/2018.
 */

public class ProjectDataRepository {
    public static LiveData<List<Project>> projectList ;

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET Project ---
    public LiveData <List<Project>> getProject() {
        projectList = generateProject();
        projectList = projectDao.getProject();
        return projectList;
    }

    static LiveData<List<Project>> generateProject() {
        return new LiveData <List<Project>>() {
            @Override
            public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Project>> observer) {
                super.observe(owner, observer);
            }
        };
    }

}
