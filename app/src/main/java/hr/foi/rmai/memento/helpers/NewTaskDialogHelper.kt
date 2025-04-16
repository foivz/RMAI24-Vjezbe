package hr.foi.rmai.memento.helpers

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.text.SimpleDateFormat
import java.util.Calendar

class NewTaskDialogHelper(private val view: View) {
    private val spinner = view.findViewById<Spinner>(R.id.spn_new_task_dialog_course)
    private val dateSelection = view.findViewById<EditText>(R.id.et_new_task_dialog_date)
    private val timeSelection = view.findViewById<EditText>(R.id.et_new_task_dialog_time)

    private val selectedDateTime: Calendar = Calendar.getInstance()
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.")
    private val sdfTime = SimpleDateFormat("HH:mm")

    fun populateSpinner(courses: List<TaskCourse>) {
        val spinnerAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            courses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }

    fun activateDateTimeListeners() {
        dateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedDateTime.set(year, monthOfYear, dayOfMonth)
                        dateSelection.setText(sdfDate.format(selectedDateTime.time).toString())
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
                ).show()
                view.clearFocus()
            }
        }
        timeSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    view.context, { _, hourOfDay, minute ->
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDateTime.set(Calendar.MINUTE, minute)
                        timeSelection.setText(sdfTime.format(selectedDateTime.time).toString())
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE), true
                ).show()
                view.clearFocus()
            }
        }
    }

    fun buildTask(): Task {
        val etName = view.findViewById<EditText>(R.id.et_new_task_dialog_name)
        val newTaskName = etName.text.toString()
        val spinnerCourse = view.findViewById<Spinner>(R.id.spn_new_task_dialog_course)
        val selectedCourse = spinnerCourse.selectedItem as TaskCourse

        return Task(0, newTaskName, selectedDateTime.time, selectedCourse.id, false)
    }
}