package hr.foi.rmai.memento.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.MockDataLoader
import hr.foi.rmai.memento.helpers.NewTaskDialogHelper

class PendingFragment : Fragment() {
    private val mockData = MockDataLoader.getMockData()

    private val tasksDao = TasksDatabase.getInstance().getTasksDao()
    private val taskCoursesDao = TasksDatabase.getInstance().getTaskCoursesDao()

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreateTask: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_pending_tasks)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        btnCreateTask = view.findViewById(R.id.fab_pending_fragment_create_task)
        btnCreateTask.setOnClickListener {
            showDialog()
        }

        loadTaskList()
    }

    private fun incrementTasksCreatedCounter() {
        context?.getSharedPreferences("tasks_preferences", Context.MODE_PRIVATE)?.apply {
            val currentCount = getInt("tasks_created_counter", 0)
            edit().putInt("tasks_created_counter", currentCount + 1).apply()
        }
    }

    private fun loadTaskList() {
        val tasks = tasksDao.getAllTasks(false)
        recyclerView.adapter = TasksAdapter(tasks.toMutableList()) { taskId ->
            parentFragmentManager.setFragmentResult(
                "task_completed",
                bundleOf("task_id" to taskId)
            )
        }
    }

    private fun showDialog() {
        val newTaskDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_task_dialog, null)

        val dialogHelper = NewTaskDialogHelper(newTaskDialogView)

        AlertDialog.Builder(requireContext())
            .setView(newTaskDialogView)
            .setTitle(getString(R.string.create_a_new_task))
            .setPositiveButton(getString(R.string.create_a_new_task)) { _, _ ->
                var newTask = dialogHelper.buildTask()
                val tasksAdapter = (recyclerView.adapter as TasksAdapter)

                val newTaskId = tasksDao.insertTask(newTask)[0]
                newTask = tasksDao.getTask(newTaskId.toInt())

                tasksAdapter.addTask(newTask)

                incrementTasksCreatedCounter()
            }
            .show()

        dialogHelper.populateSpinner(taskCoursesDao.getAllCourses())
        dialogHelper.activateDateTimeListeners()
        dialogHelper.setDefaultTaskName()
    }
}