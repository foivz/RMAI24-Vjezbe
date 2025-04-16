package hr.foi.rmai.memento.adapters

import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task
import java.text.SimpleDateFormat

class TasksAdapter(private val taskList: MutableList<Task>,
                    private val onTaskCompleted: ((taskId: Int) -> Unit)? = null
    ) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvTaskName: TextView
        private val tvDueDate: TextView
        private val svTaskCourse: SurfaceView

        private val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm")

        init {
            tvTaskName = view.findViewById(R.id.tv_task_name)
            tvDueDate = view.findViewById(R.id.tv_due_date)
            svTaskCourse = view.findViewById(R.id.sv_task_course)

            view.setOnLongClickListener {
                AlertDialog.Builder(view.context)
                    .setPositiveButton("Mark as completed") { _, _ ->
                        val completedTask = taskList[adapterPosition]
                        completedTask.completed = true

                        TasksDatabase.getInstance().getTasksDao()
                            .insertTask(completedTask)
                        removeTaskFromList()

                        if (onTaskCompleted != null) {
                            onTaskCompleted.invoke(completedTask.id)
                        }
                    }
                    .setNegativeButton("Delete task") { _, _ ->
                        val deletedTask = taskList[adapterPosition]
                        TasksDatabase.getInstance().getTasksDao()
                            .removeTask(deletedTask)
                        removeTaskFromList()
                    }
                    .setNeutralButton("Cancel") { dialog, _ ->
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

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }
}