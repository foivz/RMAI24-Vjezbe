package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getMockData(): List<Task> {
        val courses = getMockCourses()

        return listOf(
            Task("Submit seminar paper", Date(), courses[0], false),
            Task("Prepare for exercises", Date(), courses[1], false),
            Task("Rally a project team", Date(), courses[0], false),
            Task("Work on 1st homework", Date(), courses[2], false))
    }

    fun getMockCourses(): List<TaskCourse> = listOf(
        TaskCourse("RMAI", "#02AB34"), TaskCourse("OS", "#56AC45"),
        TaskCourse("EP", "#ABCDEF"), TaskCourse("RWA", "#12AD89"))
}