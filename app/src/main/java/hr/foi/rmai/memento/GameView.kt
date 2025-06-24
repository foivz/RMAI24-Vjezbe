package hr.foi.rmai.memento

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import hr.foi.rmai.memento.entities.Drone
import hr.foi.rmai.memento.entities.GameObject
import hr.foi.rmai.memento.levels.LevelManager
import hr.foi.rmai.memento.utils.InputController
import hr.foi.rmai.memento.utils.PlayerState
import hr.foi.rmai.memento.utils.RectHitbox
import hr.foi.rmai.memento.views.Viewport

class GameView(
    context: Context,
    private val width: Int,
    private val height: Int
): SurfaceView(context) {
    private val playerState: PlayerState
    private val paint = Paint()
    private val viewport: Viewport
    private lateinit var levelManager: LevelManager
    private lateinit var inputController: InputController

    init {
        viewport = Viewport(width, height)
        playerState = PlayerState()

        loadLevel("TestLevel", 16f, 0.25f)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))
            drawBackgrounds(canvas, 0, -3)

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
                            gameObject.width.toFloat(),
                            gameObject.height.toFloat()
                        )

                        drawGameObject(
                            canvas,
                            gameObject,
                            toScreen2d,
                            paint
                        )
                    }
                }
            }

            paint.setColor(Color.argb(255, 255, 255, 255))

            for (i in 0 until levelManager.player.bfg.numBullets) {
                toScreen2d = viewport.worldToScreen(
                    levelManager.player.bfg.getBulletX(i),
                    levelManager.player.bfg.getBulletY(i),
                    0.25f,
                    0.05f
                )

                canvas.drawRect(toScreen2d, paint)
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

                    checkCollisionWithPlayer(gameObject)
                }
                checkBulletCollision(gameObject)

                if (levelManager.playing) {
                    gameObject.update(fps, levelManager.gravity)

                    if (gameObject.type == 'd') {
                        val d = gameObject as Drone
                        d.setWaypoint(levelManager.player.worldLocation)
                    }
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
            playerY,
            width
        )

        inputController = InputController(width, height, levelManager)

        val location = PointF(playerX, playerY)
        playerState.saveLocation(location)

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
                'c' -> handleCoinPickup(gameObject, hit)
                'e' -> handleExtraLife(gameObject, hit)
                'd' -> handleEnemy()
                'g' -> handleEnemy()
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

    private fun drawWholeBitmap(
        toScreen2d: Rect,
        paint: Paint,
        canvas: Canvas,
        gameObject: GameObject
    ) {
        canvas.drawBitmap(
            levelManager.getBitmap(gameObject.type),
            toScreen2d.left.toFloat(),
            toScreen2d.top.toFloat(),
            paint
        )
    }

    private fun drawAnimatedRegularWay(
        toScreen2d: Rect,
        paint: Paint,
        canvas: Canvas,
        gameObject: GameObject
    ) {
        canvas.drawBitmap(
            levelManager.getBitmap(gameObject.type),
            gameObject.getRectToDraw(System.currentTimeMillis()),
            toScreen2d,
            paint
        )
    }

    private fun drawAnimatedRotated(
        canvas: Canvas,
        toScreen2d: Rect,
        gameObject: GameObject
    ) {
        val flipper = Matrix()
        flipper.preScale(-1f, 0.75f)

        val r: Rect? = gameObject.getRectToDraw(System.currentTimeMillis())
        val b: Bitmap

        if (r != null) {
            b = Bitmap.createBitmap(
                levelManager.getBitmap(gameObject.type),
                r.left,
                r.top,
                r.width(),
                r.height(),
                flipper,
                true
            )

            canvas.drawBitmap(
                b,
                toScreen2d.left.toFloat(),
                toScreen2d.top.toFloat(),
                paint
            )
        }
    }

    private fun drawGameObject(
        canvas: Canvas,
        gameObject: GameObject,
        toScreen2d: Rect,
        paint: Paint
    ) {
        if (gameObject.animated) {
            if (gameObject.facing == gameObject.RIGHT) {
                drawAnimatedRotated(canvas, toScreen2d, gameObject)
            } else {
                drawAnimatedRegularWay(toScreen2d, paint, canvas, gameObject)
            }
        } else {
            drawWholeBitmap(toScreen2d, paint, canvas, gameObject)
        }
    }

    private fun handlePickup(gameObject: GameObject, hit: Int) {
        gameObject.active = false
        gameObject.visible = false

        if (hit != 2) {
            levelManager.player.restorePreviousVelocity()
        }
    }

    private fun handleExtraLife(gameObject: GameObject, hit: Int) {
        handlePickup(gameObject, hit)
        playerState.addLife()
    }

    private fun handleCoinPickup(gameObject: GameObject, hit: Int) {
        handlePickup(gameObject, hit)
        playerState.gotCredit()
    }

    private fun handleEnemy() {
        playerState.loseLife()

        var location = PointF(
            playerState.loadLocation().x,
            playerState.loadLocation().y
        )
        levelManager.player.setWorldLocation(
            location.x,
            location.y,
            0
        )
        levelManager.player.xVelocity = 0f
    }

    private fun checkBulletCollision(gameObject: GameObject) {
        for (i in 0 until levelManager.player.bfg.numBullets) {
            var r = RectHitbox()
            r.left = levelManager.player.bfg.getBulletX(i)
            r.right = levelManager.player.bfg.getBulletX(i) + 0.1f
            r.top = levelManager.player.bfg.getBulletY(i)
            r.bottom= levelManager.player.bfg.getBulletY(i) + 0.1f

            if (gameObject.rectHitbox.intersects(r)) {
                levelManager.player.bfg.hideBullet(i)

                if (gameObject.type == 'g') {
                    gameObject.setWorldLocation(
                        gameObject.worldLocation.x +
                        2 * levelManager.player.bfg.getDirection(i),
                        gameObject.worldLocation.y,
                        gameObject.worldLocation.z
                    )
                } else if (gameObject.type == 'd') {
                    gameObject.setWorldLocation(-100f, -100f, 0)
                }
            }
        }
    }

    private fun drawBackgrounds(
        canvas: Canvas,
        start: Int,
        stop: Int
    ) {
        var fromRect1: Rect
        var fromRect2: Rect
        var toRect1: Rect
        var toRect2: Rect

        levelManager.backgrounds.forEach { bg ->
            if (bg.z < start && bg.z > stop) {
                if (!viewport.clipObjects(
                    -1f,
                    bg.y,
                    1000f,
                    bg.height.toFloat()
                )) {
                    val startY = viewport.screenCenterY -
                        (viewport.getViewportWorldCenterY() - bg.y) *
                        viewport.pixelsPerMeterY

                    val endY = viewport.screenCenterY -
                            (viewport.getViewportWorldCenterY() - bg.endY) *
                            viewport.pixelsPerMeterY

                    fromRect1 = Rect(
                        0,
                        0,
                        bg.width - bg.xClip,
                        bg.height
                    )
                    toRect1 = Rect(
                        bg.xClip,
                        startY.toInt(),
                        bg.width,
                        endY.toInt()
                    )
                    fromRect2 = Rect(
                        bg.width - bg.xClip,
                        0,
                        bg.width,
                        bg.height
                    )
                    toRect2 = Rect(
                        0,
                        startY.toInt(),
                        bg.xClip,
                        endY.toInt()
                    )

                    if (!bg.reversedFirst) {
                        canvas.drawBitmap(
                            bg.bitmap,
                            fromRect1,
                            toRect1,
                            paint
                        )
                        canvas.drawBitmap(
                            bg.bitmapReversed,
                            fromRect2,
                            toRect2,
                            paint
                        )
                    } else {
                        canvas.drawBitmap(
                            bg.bitmap,
                            fromRect2,
                            toRect2,
                            paint
                        )
                        canvas.drawBitmap(
                            bg.bitmapReversed,
                            fromRect1,
                            toRect1,
                            paint
                        )

                        bg.xClip -=
                            (levelManager.player.xVelocity / (20 / bg.speed)).toInt()

                        if (bg.xClip >= bg.width) {
                            bg.xClip = 0
                            bg.reversedFirst = !bg.reversedFirst
                        } else if (bg.xClip <= 0) {
                            bg.xClip = bg.width
                            bg.reversedFirst = !bg.reversedFirst
                        }
                    }
                }
            }
        }
    }
}




