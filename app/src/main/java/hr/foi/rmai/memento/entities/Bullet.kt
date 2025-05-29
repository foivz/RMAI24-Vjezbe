package hr.foi.rmai.memento.entities

class Bullet internal constructor(
    var x: Float,
    val y: Float,
    speed: Int,
    val direction: Int
) {
    private var xVelocity: Float

    init {
        xVelocity = (speed * direction).toFloat()
    }

    fun update(fps: Long) {
        x += xVelocity / fps
    }

    fun hideBullet() {
        x = -100f
        xVelocity = 0f
    }
}




