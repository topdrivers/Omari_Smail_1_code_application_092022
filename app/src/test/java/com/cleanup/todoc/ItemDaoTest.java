package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.Utils.LiveDataTestUtil;
import com.cleanup.todoc.database.dao.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ItemDaoTest {


    // FOR DATA
    private TodocDatabase database;
    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static List<Project> PROJECT_DEMO = new ArrayList<>();
    private static Task TASK1 = new Task(1, PROJECT_ID, "Test1", 100);
    private static Task TASK2 = new Task(2, PROJECT_ID, "Test2", 100);
    private static Task TASK3 = new Task(3, PROJECT_ID, "Test3", 100);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                        TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        PROJECT_DEMO.add(new Project(PROJECT_ID, "Projet 1", 0x000000));
        PROJECT_DEMO.add(new Project(PROJECT_ID+1, "Projet 2", 0x00FF00));
        PROJECT_DEMO.add(new Project(PROJECT_ID+2, "Projet 3", 0xFF0000));
    }
    public void listProject(){
        PROJECT_DEMO.add(new Project(PROJECT_ID, "Projet 1", 0x000000));
        PROJECT_DEMO.add(new Project(PROJECT_ID+1, "Projet 2", 0x00FF00));
        PROJECT_DEMO.add(new Project(PROJECT_ID+2, "Projet 3", 0xFF0000));
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        // BEFORE : Adding a new project
        this.database.projectDao().createProject(PROJECT_DEMO);
        // TEST
        List<Project> projectList = LiveDataTestUtil.getValue(this.database.projectDao().getProject());
        Project project =  projectList.get((int) PROJECT_ID);
        System.out.println("-----------project name----"+project.getName());
        System.out.println("-----------project name----"+PROJECT_DEMO.get((int) PROJECT_ID).getName());
        System.out.println("-----------project id----"+project.getId());
        System.out.println("-----------project id----"+PROJECT_DEMO.get((int) PROJECT_ID).getName());
        //Project project = (Project) LiveDataTestUtil.getValue(project2);
        assertTrue(project.getName().equals(PROJECT_DEMO.get((int) PROJECT_ID).getName()) && project.getId() == (int)PROJECT_ID+1);
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems());
        assertTrue(items.isEmpty());
    }


    @Test
    public void insertAndGetTasks() throws InterruptedException {
        // BEFORE : Adding demo project & demo tasks

        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.itemDao().insertItem(TASK1);
        this.database.itemDao().insertItem(TASK2);
        this.database.itemDao().insertItem(TASK3);

        // TEST
        List<Task> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems());
        assertEquals(3, items.size());
    }


    @Test
    public void insertAndUpdateTask() throws InterruptedException {
        // BEFORE : Adding demo project & demo tasks. Next, update task added & re-save it
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.itemDao().insertItem(TASK1);

        Task taskUpdate = new Task(1, PROJECT_ID, "TEST", 200);

        Task taskAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems()).get(0);
        assertEquals(taskAdded.getId(), TASK1.getId());

        this.database.itemDao().updateItem(taskUpdate);

        //TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.itemDao().getItems());
        assertEquals(1, tasks.size());
        assertNotEquals(tasks.get(0).getName(), TASK1.getName());
        assertNotEquals(tasks.get(0).getName(), taskUpdate);
    }


    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // BEFORE : Adding demo project & demo task. Next, get the task added & delete it.
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.itemDao().insertItem(TASK1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems()).get(0);
        this.database.itemDao().deleteItem(taskAdded.getId());

        //TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.itemDao().getItems());
        assertTrue(tasks.isEmpty());
    }



}
