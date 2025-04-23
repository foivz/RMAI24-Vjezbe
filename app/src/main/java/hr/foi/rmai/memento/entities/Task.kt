package hr.foi.rmai.memento.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.foi.rmai.memento.converters.DateConverter
import hr.foi.rmai.memento.database.TasksDatabase
import java.util.Calendar
import java.util.Date

@Entity(tableName = "tasks", foreignKeys = [
    ForeignKey(
        entity = TaskCourse::class,
        parentColumns = ["id"],
        childColumns = ["course_id"],
        onDelete = ForeignKey.RESTRICT
    )
])
@TypeConverters(DateConverter::class)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name="due_date") val dueDate: Date,
    @ColumnInfo(name="course_id", index = true) val courseId: Int,
    var completed: Boolean
) {
    @delegate:Ignore
    val course: TaskCourse by lazy {
        TasksDatabase.getInstance().getTaskCoursesDao().getCourseById(courseId)
    }

    fun isOverdue(): Boolean {
        val dateFromMonthAgo = Calendar.getInstance().apply {
            add(Calendar.DATE, -30)
        }.time
        return dueDate.before(dateFromMonthAgo)
    }
}
