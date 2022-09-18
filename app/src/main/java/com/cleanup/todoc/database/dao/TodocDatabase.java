package com.cleanup.todoc.database.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)

public abstract class TodocDatabase extends RoomDatabase {


    // --- SINGLETON ---
    private static volatile TodocDatabase INSTANCE;


    // --- DAO ---
    public abstract ItemDao itemDao();


    public abstract ProjectDao projectDao();


    // --- INSTANCE ---
    public static TodocDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (TodocDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                            TodocDatabase.class, "MyDatabase.db")

                            .addCallback(prepopulateDatabase())

                            .build();

                }

            }

        }

        return INSTANCE;

    }


    private static Callback prepopulateDatabase() {

        return new RoomDatabase.Callback() {

            @Override

            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                super.onCreate(db);
                List<Project> projectList = new ArrayList<>();
                projectList.add(new Project(0, "Projet Tartampion", 0xFFEADAD1));
                projectList.add(new Project(1, "Projet Lucidia", 0xFFB4CDBA));
                projectList.add(new Project(2, "Projet Circus", 0xFFA3CED2));

                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao().createProject( projectList));

            }

        };

    }

}