package hr.foi.rmai.memento.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import hr.foi.rmai.memento.utils.RectHitbox

abstract class GameObject(
    val width: Int,
    val height: Int,
    val animFrameCount: Int,
    val bitmapName: String,
    val type: Char
) {
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
            (width * animFrameCount * pixelsPerMeter),
            (height * pixelsPerMeter),
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

    abstract fun update(fps: Int, gravity: Float)
}





