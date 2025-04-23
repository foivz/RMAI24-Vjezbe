package hr.foi.rmai.memento.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import hr.foi.rmai.memento.database.TasksDatabase

class TaskDeletionService : Service() {
    inner class TaskDeletionBinder : Binder() {
        fun getService(): TaskDeletionService = this@TaskDeletionService
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        TasksDatabase.buildInstance(applicationContext)

        val tasksDao = TasksDatabase.getInstance().getTasksDao()

        tasksDao.getAllTasks(true).forEach {
            if (it.isOverdue()) {
                tasksDao.removeTask(it)
            }
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? = TaskDeletionBinder()

    }
}