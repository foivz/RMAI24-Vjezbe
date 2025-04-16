package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.database.TasksDAO
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getMockData(): MutableList<Task> {
        val courses = getMockCourses()

        return mutableListOf(
            Task(0, "Submit seminar paper", Date(), 0, false),
            Task(1, "Prepare for exercises", Date(), 1, false),
            Task(2, "Rally a project team", Date(), 0, false),
            Task(3, "Work on 1st homework", Date(), 2, false))
    }

    fun getMockCourses(): List<TaskCourse> = listOf(
        TaskCourse(1,"RMAI", "#02AB34"),
        TaskCourse(2,"OS", "#56AC45"),
        TaskCourse(3,"EP", "#ABCDEF"),
        TaskCourse(4,"RWA", "#12AD89"))

    fun loadMockData() {
        val tasksDao = TasksDatabase.getInstance().getTasksDao()
        val taskCoursesDao = TasksDatabase.getInstance().getTaskCoursesDao()

        if (tasksDao.getAllTasks(false).isEmpty() &&
            tasksDao.getAllTasks(true).isEmpty() &&
            taskCoursesDao.getAllCourses().isEmpty()) {

            val courses = arrayOf(
                TaskCourse(1,"RMAI", "#02AB34"),
                TaskCourse(2,"OS", "#56AC45"),
                TaskCourse(3,"EP", "#ABCDEF"),
                TaskCourse(4,"RWA", "#12AD89"))
            taskCoursesDao.insertCourse(*courses)

            val dbCourses = taskCoursesDao.getAllCourses()

            val tasks = arrayOf(
                Task(0, "Submit seminar paper", Date(), dbCourses[0].id, false),
                Task(1, "Prepare for exercises", Date(), dbCourses[1].id, false),
                Task(2, "Rally a project team", Date(), dbCourses[2].id, false),
                Task(3, "Work on 1st homework", Date(), dbCourses[3].id, false))
            tasksDao.insertTask(*tasks)
        }
    }
}