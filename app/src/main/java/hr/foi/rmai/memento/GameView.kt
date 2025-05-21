package hr.foi.rmai.memento

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView
import hr.foi.rmai.memento.entities.GameObject
import hr.foi.rmai.memento.levels.LevelManager
import hr.foi.rmai.memento.views.Viewport

class GameView(context: Context, width: Int, height: Int): SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport
    private lateinit var levelManager: LevelManager

    init {
        viewport = Viewport(width, height)

        loadLevel("TestLevel", 16f, 0.25f)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))

            var toScreen2d: Rect
            for (layer in -1..1) {
                for (gameObject in levelManager.gameObjects) {
                    if (gameObject.visible && gameObject.worldLocation.z == layer) {
                        toScreen2d = viewport.worldToScreen(
                            gameObject.worldLocation.x,
                            gameObject.worldLocation.y,
                            gameObject.width,
                            gameObject.height
                        )

                        canvas.drawBitmap(
                            levelManager.getBitmap(gameObject.type),
                            toScreen2d.left.toFloat(),
                            toScreen2d.top.toFloat(),
                            paint
                        )
                    }
                }
            }
        }
    }

    fun update(fps: Int) {
        for (gameObject: GameObject in levelManager.gameObjects) {
            if (gameObject.active) {
                if (!viewport.clipObjects(
                    gameObject.worldLocation.x,
                    gameObject.worldLocation.y,
                    gameObject.width.toFloat(),
                    gameObject.height.toFloat()
                )) {
                    gameObject.visible = true
                }
            } else {
                gameObject.visible = false
            }
        }
    }

    fun loadLevel(level: String, playerX: Float, playerY: Float) {
        levelManager = LevelManager(
            level,
            context,
            viewport.pixelsPerMeterX,
            playerX,
            playerY
        )

        viewport.setWorldCenter(
            levelManager.player.worldLocation.x,
            levelManager.player.worldLocation.y
        )
    }
}




