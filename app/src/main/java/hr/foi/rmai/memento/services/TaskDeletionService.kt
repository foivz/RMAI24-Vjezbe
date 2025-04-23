package hr.foi.rmai.memento.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.TaskDeletionServiceHelper

class TaskDeletionService : Service() {
    inner class TaskDeletionBinder : Binder() {
        fun getService(): TaskDeletionService = this@TaskDeletionService
    }

    fun deleteOldTasks(onTaskDeletion: (Int) -> Unit) {
        TasksDatabase.buildInstance(applicationContext)

        val tasksDao = TasksDatabase.getInstance().getTasksDao()

        tasksDao.getAllTasks(true).forEach {
            if (it.isOverdue()) {
                tasksDao.removeTask(it)
                onTaskDeletion(it.id)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? = TaskDeletionBinder()
}