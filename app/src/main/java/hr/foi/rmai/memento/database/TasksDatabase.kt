package hr.foi.rmai.memento.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse

@Database(version = 1, exportSchema = false, entities = [
    Task::class, TaskCourse::class
])
abstract class TasksDatabase: RoomDatabase() {
    abstract fun getTasksDao(): TasksDAO
    abstract fun getTaskCoursesDao(): TaskCoursesDAO

    companion object {
        private var dbInstance: TasksDatabase? = null

        fun getInstance(): TasksDatabase {
            if (dbInstance == null)
                throw NullPointerException("DB instance not yet created!")

            return dbInstance!!
        }

        fun buildInstance(context: Context) {
            if (dbInstance == null) {
                val dbBuilder = Room.databaseBuilder(
                    context,
                    TasksDatabase::class.java,
                    "tasks.db"
                )

                dbBuilder.allowMainThreadQueries()
                dbBuilder.fallbackToDestructiveMigration()

                dbInstance = dbBuilder.build()
            }
        }
    }
}