package hr.foi.rmai.memento

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import hr.foi.rmai.memento.entities.GameObject
import hr.foi.rmai.memento.levels.LevelManager
import hr.foi.rmai.memento.utils.InputController
import hr.foi.rmai.memento.views.Viewport

class GameView(
    context: Context,
    private val width: Int,
    private val height: Int
): SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport
    private lateinit var levelManager: LevelManager
    private lateinit var inputController: InputController

    init {
        viewport = Viewport(width, height)

        loadLevel("TestLevel", 16f, 0.25f)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))

            paint.setColor(Color.argb(80, 255, 255, 255))
            val buttonsToDraw = inputController.getButtons()

            for (rect in buttonsToDraw) {
                val rf = RectF(
                    rect.left.toFloat(),
                    rect.top.toFloat(),
                    rect.right.toFloat(),
                    rect.bottom.toFloat()
                )

                canvas.drawRoundRect(rf, 15f, 15f, paint)
            }

            paint.setColor(Color.argb(255, 255, 255, 255))

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
            if (gameObject.bitmapName == "player") {
                Log.i("aaaaa", gameObject.worldLocation.x.toString())
                Log.i("aaaaa", gameObject.worldLocation.y.toString())
            }
            if (gameObject.active) {
                if (!viewport.clipObjects(
                    gameObject.worldLocation.x,
                    gameObject.worldLocation.y,
                    gameObject.width.toFloat(),
                    gameObject.height.toFloat()
                )) {
                    gameObject.visible = true

                    checkCollisionWithPlayer(gameObject)
                }
                if (levelManager.playing) {
                    gameObject.update(fps, levelManager.gravity)
                }
            } else {
                gameObject.visible = false
            }
        }

        if (levelManager.playing) {
            viewport.setWorldCenter(
                levelManager.player.worldLocation.x,
                levelManager.player.worldLocation.y
            )
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

        inputController = InputController(width, height, levelManager)

        viewport.setWorldCenter(
            levelManager.player.worldLocation.x,
            levelManager.player.worldLocation.y
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        inputController.handleInput(event)

        return true
    }

    private fun checkCollisionWithPlayer(gameObject: GameObject) {
        val hit: Int =
            levelManager.player.checkCollisions(gameObject.rectHitbox)
        if (hit > 0) {
            when (gameObject.type) {
                else -> {
                    if (hit == 1) {
                        levelManager.player.xVelocity = 0f
                        levelManager.player.isPressingRight = false
                    }
                    if (hit == 2) {
                        levelManager.player.isFalling = false
                    }
                }
            }
        }
    }
}




