package com.cleanup.todoc.ui;

import static com.cleanup.todoc.utils.TaskDialogUtils.showAddTaskDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injection.Injection;
import com.cleanup.todoc.injection.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.viewModel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {


    /**
     * List of all current tasks of the application
     */
    @NonNull
    static final ArrayList<Task> tasks = new ArrayList<>();

    public static  List<Project> projectList;

    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, this);


    @SuppressWarnings("NullableProblems")
    @NonNull
    private static RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private static TextView lblNoTasks;

    public static ItemViewModel itemViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        configureViewModel();
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog(this));

        getAllProject();
        getItems();
        updateUi();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            itemViewModel.sortAZItem().observe(this,this::updateItemsList);
        } else if (id == R.id.filter_alphabetical_inverted) {
            itemViewModel.sortZAItem().observe(this,this::updateItemsList);
        } else if (id == R.id.filter_oldest_first) {
            itemViewModel.sortOldItem().observe(this,this::updateItemsList);
        } else if (id == R.id.filter_recent_first) {
            itemViewModel.sortRecentItem().observe(this,this::updateItemsList);
        }

        itemViewModel.getItems().observe(this, this::updateTasks);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {
        itemViewModel.deleteItem(task.getId());
        itemViewModel.getItems().observe(this, this::updateTasks);
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        itemViewModel = new ViewModelProvider(this, mViewModelFactory).get(ItemViewModel.class);
        itemViewModel.init();
    }

    private void getAllProject() {
        itemViewModel.getProjects().observe(this, this::getProjectInVar);
    }

    public  void createItem(Task task){
        itemViewModel.createItem(task);
        itemViewModel.getItems().observe(this, this::updateTasks);
    }

    private void getItems(){
         itemViewModel.getItems().observe(this, this::updateItemsList);
    }


    private void getProjectInVar(List<Project> projects) {
        projectList = projects;
    }


    private void updateItemsList(List<Task> tasks){
        this.adapter.updateTasks(tasks);
    }


    private void updateUi() {
        itemViewModel.getItems().observe(this, this::updateTasks);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private  void updateTasks(List<Task> tasks) {

        if (tasks.size()==0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        }else{
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
        }

    }



}
