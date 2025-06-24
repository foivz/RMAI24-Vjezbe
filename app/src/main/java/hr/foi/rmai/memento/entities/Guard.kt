package hr.foi.rmai.memento.entities

class Guard(worldStartX: Int, worldStartY: Int, pixelsPerMeter: Int)
    : GameObject(1f, 2f, 5, "guard", 'g') {
    private var waypointX1 = 0f
    private var waypointX2 = 0f

    private var currentWaypoint = 0
    val MAX_X_VELOCITY = 2f

    init {
        moves = true
        active = true
        visible = true
        facing = LEFT

        animFps = 8
        setAnimated(pixelsPerMeter, true)

        setWorldLocation(
            worldStartX.toFloat(),
            worldStartY.toFloat(),
            0
        )
        xVelocity = -MAX_X_VELOCITY
        currentWaypoint = 1
    }

    override fun update(fps: Int, gravity: Float) {
        if (currentWaypoint == 1) {
            if (worldLocation.x <= waypointX1) {
                currentWaypoint = 2
                xVelocity = MAX_X_VELOCITY
                facing = RIGHT
            }
        }
        else if (currentWaypoint == 2) {
            if (worldLocation.x >= waypointX2) {
                currentWaypoint = 1
                xVelocity = -MAX_X_VELOCITY
                facing = LEFT
            }
        }

        move(fps)
        updateRectHitbox()
    }

    fun setWaypoints(x1: Float, x2: Float) {
        waypointX1 = x1
        waypointX2 = x2
    }
}



