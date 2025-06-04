package hr.foi.rmai.memento.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import hr.foi.rmai.memento.utils.RectHitbox
import hr.foi.rmai.memento.views.Animation

abstract class GameObject(
    val width: Float,
    val height: Float,
    var animFrameCount: Int,
    val bitmapName: String,
    val type: Char
) {
    protected var anim: Animation? = null
    var animated = false
    protected var animFps = 1

    val rectHitbox = RectHitbox()
    val worldLocation: WorldLocation = WorldLocation(0f, 0f, 0)
    var active = true
    var visible = true

    val LEFT = -1
    val RIGHT = 1
    var facing = 0

    var moves = false

    private var _xVelocity: Float
    var xVelocity: Float
        get() = _xVelocity
        set(value) {
            if (moves) {
                _xVelocity = value
            }
        }

    private var _yVelocity: Float
    var yVelocity: Float
        get() = _yVelocity
        set(value) {
            if (moves) {
                _yVelocity = value
            }
        }

    init {
        _xVelocity = 0f
        _yVelocity = 0f
    }

    fun move(fps: Int) {
        if (xVelocity != 0f) {
            worldLocation.x += xVelocity / fps
        }

        if (yVelocity != 0f) {
            worldLocation.y += yVelocity / fps
        }
    }

    fun setWorldLocation(
        x: Float,
        y: Float,
        z: Int
    ) {
        worldLocation.x = x
        worldLocation.y = y
        worldLocation.z = z
    }

    fun prepareBitmap(context: Context, pixelsPerMeter: Int): Bitmap {
        val resID = context.resources.getIdentifier(
            bitmapName,
            "drawable",
            context.packageName
        )

        var bitmap = BitmapFactory.decodeResource(
            context.resources,
            resID
        )
        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (width * animFrameCount * pixelsPerMeter).toInt(),
            (height * pixelsPerMeter).toInt(),
            false
        )

        return bitmap
    }

    fun updateRectHitbox() {
        rectHitbox.top = worldLocation.y
        rectHitbox.left = worldLocation.x
        rectHitbox.bottom = worldLocation.y + height
        rectHitbox.right = worldLocation.x + width
    }

    fun setAnimated(
        pixelsPerMeter: Int,
        animated: Boolean
    ) {
        this.animated = animated
        anim = Animation(
            bitmapName,
            pixelsPerMeter,
            width.toInt(),
            height.toInt(),
            animFrameCount,
            animFps
        )
    }

    fun getRectToDraw(deltaTime: Long): Rect? {
        return anim?.getCurrentFrame(
            deltaTime,
            xVelocity,
            moves
        )
    }
    abstract fun update(fps: Int, gravity: Float)
}





