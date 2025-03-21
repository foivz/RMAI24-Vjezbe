package hr.foi.rmai.memento.adapters

package hr.foi.rmai.memento.adapters

import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.entities.Task
import java.text.SimpleDateFormat

class TasksAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvTaskName: TextView
        private val tvDueDate: TextView
        private val svTaskCourse: SurfaceView

        private val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm")

        init {
            tvTaskName = view.findViewById(R.id.tv_task_name)
            tvDueDate = view.findViewById(R.id.tv_due_date)
            svTaskCourse = view.findViewById(R.id.sv_task_course)
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

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }
}