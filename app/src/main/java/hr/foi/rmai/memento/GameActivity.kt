package hr.foi.rmai.memento

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator

class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private var gameThread: GameThread? = null
    private lateinit var surfaceHolder: SurfaceHolder

    private val surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            gameThread = GameThread(holder, gameView)
            gameThread?.start()
        }

        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            gameThread?.gameRunning = false
            try {
                gameThread?.join()
            } catch (e: InterruptedException) {

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val windowMetrics: WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val height = windowMetrics.bounds.height()
        val width = windowMetrics.bounds.width()

        gameView = GameView(this, width, height)
        surfaceHolder = gameView.holder
        surfaceHolder.addCallback(surfaceCallback)

        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameThread?.gameRunning = true
    }

    override fun onPause() {
        super.onPause()
        gameThread?.gameRunning = false
    }
}