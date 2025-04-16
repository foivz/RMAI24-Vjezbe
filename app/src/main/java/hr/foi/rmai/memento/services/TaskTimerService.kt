package hr.foi.rmai.memento.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task

class TaskTimerService : Service() {
    private val tasks = mutableListOf<Task>()
    private var started: Boolean = false
    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf(startId)
            return START_NOT_STICKY
        }

        val taskId = intent.getIntExtra("task_id", -1)
        val isCanceled = intent.getBooleanExtra("cancel", false)

        TasksDatabase.buildInstance(applicationContext)
        val task = TasksDatabase.getInstance().getTasksDao().getTask(taskId)

        if (tasks.contains(task)) {
            if (isCanceled) {
                tasks.remove(task)
            }
        } else if (task.dueDate > Date()) {
            tasks.add(task)

            if (!started) {
                // val notification = ...
                startForeground(NOTIFICATION_ID, notification)

                started = true
            }
        }

        return START_NOT_STICKY
    }
}