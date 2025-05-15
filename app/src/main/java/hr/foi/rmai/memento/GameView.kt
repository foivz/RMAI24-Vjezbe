package hr.foi.rmai.memento

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.SurfaceView

class GameView(context: Context, width: Int, height: Int): SurfaceView(context) {
    private val paint = Paint()
    init {

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

    }

    fun update() {

    }
}