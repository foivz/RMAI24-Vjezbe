package hr.foi.rmai.memento.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getString
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.helpers.DeletedTaskRecovery
import hr.foi.rmai.memento.services.TaskTimerService
import java.text.SimpleDateFormat
import java.util.Date

class TasksAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskCompleted: ((taskId: Int) -> Unit)? = null
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTaskName: TextView
        private val tvDueDate: TextView
        private val svTaskCourse: SurfaceView

        private val taskTimer: ImageView = view.findViewById(R.id.iv_task_timer)
        private var isTimerActive = false

        private val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm")

        init {
            tvTaskName = view.findViewById(R.id.tv_task_name)
            tvDueDate = view.findViewById(R.id.tv_due_date)
            svTaskCourse = view.findViewById(R.id.sv_task_course)

            view.setOnClickListener {
                if (Date() < taskList[adapterPosition].dueDate) {
                    val intent = Intent(view.context, TaskTimerService::class.java).apply {
                        putExtra("task_id", taskList[adapterPosition].id)
                    }
                    isTimerActive = !isTimerActive
                    if (isTimerActive) {
                        taskTimer.visibility = View.VISIBLE
                    } else {
                        intent.putExtra("cancel", true)
                        taskTimer.visibility = View.GONE
                    }
                    view.context.startService(intent)
                } else if (taskTimer.isVisible) {
                    taskTimer.visibility = View.GONE
                }
            }

            view.setOnLongClickListener {
                AlertDialog.Builder(view.context)
                    .setPositiveButton(
                        getString(
                            view.context,
                            R.string.task_mark_as_completed
                        )
                    ) { _, _ ->
                        val completedTask = taskList[adapterPosition]
                        completedTask.completed = true

                        TasksDatabase.getInstance().getTasksDao()
                            .insertTask(completedTask)
                        removeTaskFromList()

                        if (onTaskCompleted != null) {
                            onTaskCompleted.invoke(completedTask.id)
                        }
                    }
                    .setNegativeButton(getString(view.context, R.string.delete_task)) { _, _ ->
                        val deletedTask = taskList[adapterPosition]
                        val tasksDao = TasksDatabase.getInstance().getTasksDao()

                        DeletedTaskRecovery.pushTask(deletedTask, view.context.cacheDir)

                        val snack = Snackbar.make(view, "Revert?", Snackbar.LENGTH_LONG)
                            .setAction("Recover") { view ->
                                try {
                                    val poppedTaskId =
                                        DeletedTaskRecovery.popTask(view.context.cacheDir)
                                    val restoredTask = tasksDao.getTask(poppedTaskId)
                                    addTask(restoredTask)
                                } catch (ex: Exception) {
                                    Toast.makeText(view.context, ex.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        snack.show()

                        tasksDao.removeTask(deletedTask)
                        removeTaskFromList()
                    }
                    .setNeutralButton(getString(view.context, R.string.cancel)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setTitle(tvTaskName.text)
                    .show()

                return@setOnLongClickListener true
            }
        }

        fun removeTaskFromList() {
            taskList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }

        fun bind(task: Task) {
            tvTaskName.text = task.name
            tvDueDate.text = sdf.format(task.dueDate)
            svTaskCourse.setBackgroundColor(task.course.color.toColorInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)

        return TaskViewHolder(taskView)
    }

    fun addTask(newTask: Task) {
        var newIndexInList = taskList.indexOfFirst { task ->
            task.dueDate > newTask.dueDate
        }
        if (newIndexInList == -1) {
            newIndexInList = taskList.size
        }
        taskList.add(newIndexInList, newTask)
        notifyItemInserted(newIndexInList)
    }

    fun removeTaskById(taskId: Int) {
        val deletedIndex = taskList.indexOfFirst { task ->
            task.id == taskId
        }

        if (deletedIndex != -1) {
            taskList.removeAt(deletedIndex)
            notifyItemRemoved(deletedIndex)
        }
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }
}