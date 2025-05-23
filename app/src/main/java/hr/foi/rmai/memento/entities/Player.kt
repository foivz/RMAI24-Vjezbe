package hr.foi.rmai.memento.entities

import hr.foi.rmai.memento.utils.RectHitbox

class Player(locationX: Float, locationY: Float)
    : GameObject(1, 2, 5, "player", 'p') {
    val MAX_X_VELOCITY = 10f
    var isPressingRight = false
    var isPressingLeft = false
    var isFalling = false
    private var isJumping = false
    private var jumpTime: Long = 0
    private val maxJumpTime: Long = 700

    private var rectHitboxFeet = RectHitbox()
    private var rectHitboxHead = RectHitbox()
    private var rectHitboxLeft = RectHitbox()
    private var rectHitboxRight = RectHitbox()

    init {
        setWorldLocation(
            locationX.toFloat(),
            locationY.toFloat(),
            0
        )

        moves = true
        facing = LEFT
        isFalling = false
    }

    override fun update(fps: Int, gravity: Float) {
        checkCurrentMovementDirection()
        checkPlayerDirection()
        handleJumping(gravity)

        move(fps)

        val locX = worldLocation.x
        val locY = worldLocation.y

        updateLeftHitbox(locX, locY)
        updateRightHitbox(locX, locY)
        updateHeadHitbox(locX, locY)
        updateFeetHitbox(locX, locY)
    }

    fun checkCollisions(rectHitbox: RectHitbox): Int {
        var collided = 0

        if (rectHitboxLeft.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.right - width * 0.2f
            collided = 1
        }

        if (rectHitboxRight.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.left - width * 0.8f
            collided = 1
        }

        if (rectHitboxFeet.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.top - height
            collided = 2
        }

        if (rectHitboxHead.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.bottom
            collided = 3
        }

        return collided
    }

    private fun checkCurrentMovementDirection() {
        if (isPressingRight) {
            xVelocity = MAX_X_VELOCITY
        } else if (isPressingLeft) {
            xVelocity = -MAX_X_VELOCITY
        } else {
            xVelocity = 0f
        }
    }

    private fun checkPlayerDirection() {
        if (xVelocity > 0) {
            facing = RIGHT
        } else if (xVelocity < 0) {
            facing = LEFT
        }
    }

    private fun handleJumping(gravity: Float) {
        if (isJumping) {
            val timeJumping = System.currentTimeMillis() - jumpTime
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    yVelocity = -gravity
                } else if (timeJumping > maxJumpTime / 2) {
                    yVelocity = gravity
                }
            } else {
                isJumping = false
            }
        } else {
            yVelocity = gravity
            isFalling = true
        }
    }

    fun startJump() {
        if (!isFalling && !isJumping) {
            isJumping = true
            jumpTime = System.currentTimeMillis()
        }
    }

    private fun updateFeetHitbox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * 0.95f
        rectHitboxFeet.left = lx + width * 0.2f
        rectHitboxFeet.bottom = ly + height * 0.98f
        rectHitboxFeet.right = lx + width * 0.8f
    }

    private fun updateHeadHitbox(lx: Float, ly: Float) {
        rectHitboxHead.top = ly
        rectHitboxHead.left = lx + width * 0.2f
        rectHitboxHead.bottom = ly + height * 0.6f
        rectHitboxHead.right = lx + width * 0.8f
    }

    private fun updateLeftHitbox(lx: Float, ly: Float) {
        rectHitboxLeft.top = ly + height * 0.2f
        rectHitboxLeft.left = lx + width * 0.2f
        rectHitboxLeft.bottom = ly + height * 0.8f
        rectHitboxLeft.right = lx + width * 0.3f
    }

    private fun updateRightHitbox(lx: Float, ly: Float) {
        rectHitboxRight.top = ly + height * 0.2f
        rectHitboxRight.left = lx + width * 0.8f
        rectHitboxRight.bottom = ly + height * 0.8f
        rectHitboxRight.right = lx + width * 0.7f
    }
}








