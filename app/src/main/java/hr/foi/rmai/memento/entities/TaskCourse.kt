package hr.foi.rmai.memento.entities

data class TaskCourse(val name: String, val color: String) {
    override fun toString(): String {
        return name
    }
}