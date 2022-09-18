package com.cleanup.todoc.utils;

import static com.cleanup.todoc.ui.MainActivity.itemViewModel;
import static com.cleanup.todoc.ui.MainActivity.projectList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.ui.TasksAdapter;

import java.util.Date;
import java.util.List;

public class TaskDialogUtils {
    /**     * Dialog to create a new task     */
    @Nullable
    static AlertDialog dialog = null;

    /**     * EditText that allows user to set the name of a task     */
    @Nullable
    static EditText dialogEditText = null;

    /**     * Spinner that allows the user to associate a project to a task     */
    @Nullable
    static Spinner dialogSpinner = null;


    static final List<Project> allProjects = projectList;

    /**     * Shows the Dialog for adding a Task     */

    public static void showAddTaskDialog(Context context) {
        final AlertDialog dialog = getAddTaskDialog(context);

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**     * Returns the dialog allowing the user to create a new task     */

    @NonNull
    static AlertDialog getAddTaskDialog(Context context) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });

        return dialog;
    }

    /** Sets the data of the Spinner with projects to associate to a new task */
    static void populateDialogSpinner() {
        assert dialog != null;
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /** Called when the user clicks on the positive button of the Create Task Dialog.*/
    static void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(Resources.getSystem().getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                long id = 0;

                Task task = new Task(
                        id,
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                new MainActivity().createItem(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }


}
