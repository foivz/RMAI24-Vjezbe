package hr.foi.rmai.memento

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

class GameThread(val surfaceHolder: SurfaceHolder, val gameView: GameView): Thread() {
    @Volatile
    var gameRunning = true

    var fps: Int = 0
    private var frameStartTime: Long = 0
    private var frameElapsedTime: Long = 0

    override fun run() {
        while (gameRunning) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()

            if (canvas != null) {
                frameStartTime = System.currentTimeMillis()
                gameView.update(fps)
                gameView.draw(canvas)

                surfaceHolder.unlockCanvasAndPost(canvas)

                frameElapsedTime = System.currentTimeMillis() - frameStartTime

                if (frameElapsedTime > 1) {
                    fps = (1000 / frameElapsedTime).toInt()
                }
            }
        }
    }
}