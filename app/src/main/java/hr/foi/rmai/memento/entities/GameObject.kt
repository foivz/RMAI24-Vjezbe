package hr.foi.rmai.memento.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

abstract class GameObject(
    val width: Int,
    val height: Int,
    val animFrameCount: Int,
    val bitmapName: String,
    val type: Char
) {
    val worldLocation: WorldLocation = WorldLocation(0f, 0f, 0)
    var active = true
    var visible = true

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

    abstract fun update(fps: Int, gravity: Float)
}





