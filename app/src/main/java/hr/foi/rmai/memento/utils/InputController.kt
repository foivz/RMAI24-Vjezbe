package hr.foi.rmai.memento.utils

import android.graphics.Rect
import android.view.MotionEvent
import hr.foi.rmai.memento.levels.LevelManager

class InputController(
    screenWidth: Int,
    screenHeight: Int,
    val levelManager: LevelManager
) {
    private var x: Int = 0
    private var y: Int = 0

    var left: Rect
    var right: Rect
    var jump: Rect
    var shoot: Rect
    var pause: Rect

    init {
        val buttonWidth = screenWidth / 8
        val buttonHeight = screenHeight / 7
        val buttonPadding = screenWidth / 40

        left = Rect(
            buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth,
            screenHeight - buttonPadding
        )

        right = Rect(
            buttonWidth + buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth + buttonPadding + buttonWidth,
            screenHeight - buttonPadding
        )

        jump = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonPadding - buttonHeight - buttonPadding
        )

        shoot = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonPadding
        )

        pause = Rect(
            screenWidth - buttonPadding - buttonWidth,
            buttonPadding,
            screenWidth - buttonPadding,
            buttonPadding + buttonHeight
        )
    }

    fun getButtons(): ArrayList<Rect> {
        val currentButtonList = ArrayList<Rect>()

        currentButtonList.add(left)
        currentButtonList.add(right)
        currentButtonList.add(jump)
        currentButtonList.add(shoot)
        currentButtonList.add(pause)

        return currentButtonList
    }

    fun handleInput(motionEvent: MotionEvent) {
        val pointerCount = motionEvent.pointerCount

        for (i in 0..<pointerCount) {
            x = motionEvent.getX(i).toInt()
            y = motionEvent.getY(i).toInt()

            if (levelManager.playing) {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> handleDown()
                    MotionEvent.ACTION_POINTER_DOWN -> handleDown()
                    MotionEvent.ACTION_UP -> handleUp()
                    MotionEvent.ACTION_POINTER_UP -> handleUp()
                }
            } else {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> handlePause()
                }
            }
        }
    }

    private fun handleMovement() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingLeft = false
            levelManager.player.isPressingRight = true
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = true
            levelManager.player.isPressingRight = false
        } else if (jump.contains(x, y)) {
            levelManager.player.startJump()
        }
    }

    private fun handlePause() {
        if (pause.contains(x, y)) {
            levelManager.switchPlayingStatus()
        }
    }

    private fun handleUp() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = false
        }
    }

    private fun handleDown() {
        handleMovement()
        handlePause()
        handleShooting()
    }

    private fun handleShooting() {
        if (shoot.contains(x, y)) {
            if (levelManager.player.pullTriger()) {
                // ništa
            }
        }
    }
}






