package com.cleanup.todoc;

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
import static org.junit.Assert.assertSame;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */


@RunWith(RobolectricTestRunner.class)
public class TaskUnitTest {

    // FOR DATA
    private TodocDatabase database;
    private static final List<Project> PROJECT_DEMO = new ArrayList<>();
    // DATA SET FOR TEST
    private static final long PROJECT_ID = 1;
    private static final Task TASK1 = new Task(1, PROJECT_ID, "aaa", 100);
    private static final Task TASK2 = new Task(2, PROJECT_ID, "zzz", 100);
    private static final Task TASK3 = new Task(3, PROJECT_ID, "ttt", 100);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {

        this.database = Room.inMemoryDatabaseBuilder(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext(),
                        TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        PROJECT_DEMO.add(new Project(0, "Projet 1", 0x000000));
        PROJECT_DEMO.add(new Project(1, "Projet 2", 0x00FF00));
        PROJECT_DEMO.add(new Project(2, "Projet 3", 0xFF0000));
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }



    @Test
    public void test_az_comparator() throws InterruptedException {
        database.projectDao().createProject(PROJECT_DEMO);

        database.itemDao().insertItem(TASK1);
        database.itemDao().insertItem(TASK2);
        database.itemDao().insertItem(TASK3);

        List<Task> taskFromDB = LiveDataTestUtil.getValue(database.itemDao().sortAZItem());

        assertSame(taskFromDB.get(0).getId(), TASK1.getId());
        assertSame(taskFromDB.get(1).getId(), TASK3.getId());
        assertSame(taskFromDB.get(2).getId(), TASK2.getId());

    }

    @Test
    public void test_za_comparator() throws InterruptedException {
        database.projectDao().createProject(PROJECT_DEMO);

        database.itemDao().insertItem(TASK1);
        database.itemDao().insertItem(TASK2);
        database.itemDao().insertItem(TASK3);

        List<Task> taskFromDB = LiveDataTestUtil.getValue(database.itemDao().sortZAItem());

        assertSame(taskFromDB.get(0).getId(), TASK2.getId());
        assertSame(taskFromDB.get(1).getId(), TASK3.getId());
        assertSame(taskFromDB.get(2).getId(), TASK1.getId());

    }

    @Test
    public void test_recent_comparator() throws InterruptedException {
        database.projectDao().createProject(PROJECT_DEMO);

        database.itemDao().insertItem(TASK1);
        database.itemDao().insertItem(TASK2);
        database.itemDao().insertItem(TASK3);

        List<Task> taskFromDB = LiveDataTestUtil.getValue(database.itemDao().sortRecentItem());

        assertSame(taskFromDB.get(0).getId(), TASK1.getId());
        assertSame(taskFromDB.get(1).getId(), TASK2.getId());
        assertSame(taskFromDB.get(2).getId(), TASK3.getId());

    }

    @Test
    public void test_old_comparator() throws InterruptedException {
        database.projectDao().createProject(PROJECT_DEMO);

        database.itemDao().insertItem(TASK1);
        database.itemDao().insertItem(TASK2);
        database.itemDao().insertItem(TASK3);

        List<Task> taskFromDB = LiveDataTestUtil.getValue(database.itemDao().sortOldItem());

        assertSame(taskFromDB.get(0).getId(), TASK1.getId());
        assertSame(taskFromDB.get(1).getId(), TASK2.getId());
        assertSame(taskFromDB.get(2).getId(), TASK3.getId());

    }
}