package hr.foi.rmai.memento

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(val surfaceHolder: SurfaceHolder, val gameView: GameView): Thread() {
    @Volatile
    var gameRunning = true

    val fpsTarget: Long = 60
    val targetSleep = (1000 / fpsTarget)

    override fun run() {
        while (gameRunning) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()

            if (canvas != null) {
                gameView.draw(canvas)

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }
}