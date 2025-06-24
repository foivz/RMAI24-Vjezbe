package hr.foi.rmai.memento.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

class Background(
    context: Context,
    yPixelsPerMeter: Int,
    screenWidth: Int,
    data: BackgroundData
) {
    var bitmap: Bitmap
    var bitmapReversed: Bitmap
    var width = 0
    var height = 0
    var reversedFirst = false
    var xClip = 0
    var y = 0f
    var endY = 0f
    var z = 0
    var speed = 0f

    init {
        val resID = context.resources.getIdentifier(
            data.bitmapName,
            "drawable",
            context.packageName
        )
        bitmap = BitmapFactory.decodeResource(
            context.resources,
            resID
        )
        reversedFirst = false
        xClip = 0
        y = data.StartY
        endY = data.endY
        z = data.layer
        speed = data.speed
        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            screenWidth,
            data.height * yPixelsPerMeter,
            true
        )
        width = bitmap.width
        height = bitmap.height

        val matrix = Matrix()
        matrix.setScale(-1f, 1f)
        bitmapReversed = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            width,
            height,
            matrix,
            true
        )
    }
}




