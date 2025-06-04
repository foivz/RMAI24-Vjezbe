package hr.foi.rmai.memento.utils

import android.graphics.PointF

class PlayerState {
    private var numCredits = 0
    private var mgFireRate = 0
    private var lives = 0
    private var restartX = 0f
    private var restartY = 0f

    init {
        lives = 3
        mgFireRate = 1
        numCredits = 0
    }

    fun getCredits(): Int {
        return numCredits
    }

    fun gotCredit() {
        numCredits++
    }

    fun resetCredits() {
        numCredits = 0
    }

    fun loseLife() {
        lives--
    }

    fun addLife() {
        lives++
    }

    fun resetLives() {
        lives = 3
    }

    fun getLives(): Int {
        return lives
    }

    fun saveLocation(location: PointF) {
        restartX = location.x
        restartY = location.y
    }

    fun loadLocation(): PointF {
        return PointF(restartX, restartY)
    }
}






