package hr.foi.rmai.memento.helpers

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse

class NewTaskDialogHelper(private val view: View) {
    private val spinner = view.findViewById<Spinner>(R.id.spn_new_task_dialog_course)

    fun populateSpinner(courses: List<TaskCourse>) {
        val spinnerAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            courses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }

    fun activateDateTimeListeners() {

    }

    fun buildTask(): Task {
        TODO()
    }
}