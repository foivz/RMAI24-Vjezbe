package hr.foi.rmai.memento.entities

import android.graphics.PointF

class Drone(worldStartX: Int, worldStartY: Int)
    : GameObject(1f, 1f, 1, "drone", 'd') {
    var lastWaypointSetTime: Long = 0
    var currentWaypoint: PointF
    val MAX_X_VELOCITY = 3f
    val MAX_Y_VELOCITY = 3f

    init {
        moves = true
        active = true
        visible = true
        currentWaypoint = PointF()
        setWorldLocation(
            worldStartX.toFloat(),
            worldStartY.toFloat(),
            0
        )
        updateRectHitbox()
        facing = RIGHT
    }

    override fun update(fps: Int, gravity: Float) {
        if (currentWaypoint.x > worldLocation.x) {
            xVelocity = MAX_X_VELOCITY
        } else if (currentWaypoint.x < worldLocation.x) {
            xVelocity = -MAX_X_VELOCITY
        } else {
            xVelocity = 0f
        }

        if (currentWaypoint.y >= worldLocation.y) {
            yVelocity = MAX_X_VELOCITY
        } else if (currentWaypoint.y < worldLocation.y) {
            yVelocity = -MAX_Y_VELOCITY
        } else {
            yVelocity = 0f
        }

        move(fps)
        updateRectHitbox()
    }

    fun setWaypoint(playerLocation: WorldLocation) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000) {
            lastWaypointSetTime = System.currentTimeMillis()
            currentWaypoint.x = playerLocation.x
            currentWaypoint.y = playerLocation.y
        }
    }
}




