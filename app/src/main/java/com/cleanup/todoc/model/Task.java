package com.cleanup.todoc.model;


import static com.cleanup.todoc.ui.MainActivity.projectList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;


@Entity(foreignKeys = @ForeignKey(entity = Project.class,

        parentColumns = "id",

        childColumns = "projectId"))
public class Task  {


    @PrimaryKey(autoGenerate = true)
    private long id;


    private long projectId;


    @SuppressWarnings("NullableProblems")
    @NonNull
    private String name;

    /**
     * The timestamp when the task has been created
     */
    private long creationTimestamp;


    public Task(long id, long projectId, @NonNull String name, long creationTimestamp) {
        this.setId(id);
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }


    public long getProjectId() {
        return projectId;
    }


    public long getCreationTimestamp() {
        return creationTimestamp;
    }


    public long getId() {
        return id;
    }


    private void setId(long id) {
        this.id = id;
    }


    private void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * Returns the project associated to the task.
     *
     * @return the project associated to the task
     */
    @Nullable
    public Project  getProject() {  return projectList.get((int)projectId);  }

    @NonNull
    public String getName() {
        return name;
    }


    private void setName(@NonNull String name) {
        this.name = name;
    }


    private void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

}
