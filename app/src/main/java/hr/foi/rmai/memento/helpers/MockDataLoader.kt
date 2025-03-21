package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getMockData(): List<Task> = listOf(
        Task("Submit seminar paper", Date(), TaskCourse("DP", "#02AB34"), false),
        Task("Prepare for exercises", Date(), TaskCourse("SIS", "#ABCDEF"), false),
        Task("Rally a project team", Date(), TaskCourse("RMAI", "#12AD89"), false),
        Task("Work on 1st homework", Date(), TaskCourse("OS", "#56AC45"), false)
    )
}