package hr.foi.rmai.memento.views

import android.graphics.Rect

class Animation(
    bitmapName: String,
    pixelsPerMeter: Int,
    var frameWidth: Int,
    var frameHeight: Int,
    val frameCount: Int,
    animFps: Int
) {
    private val sourceRect: Rect
    private val framePeriod: Int
    private var currentFrame: Int = 0
    private var frameTick: Long = 0

    init {
        framePeriod = 1000 / animFps
        frameWidth *= pixelsPerMeter
        frameHeight *= pixelsPerMeter
        sourceRect = Rect(0, 0, frameWidth, frameHeight)
    }

    fun getCurrentFrame(
        time: Long,
        xVelocity: Float,
        moves: Boolean
    ): Rect {
        if (xVelocity != 0f || !moves) {
            if (time > frameTick + framePeriod) {
                frameTick = time
                currentFrame++
                if (currentFrame >= frameCount) {
                    currentFrame = 0
                }
            }
        }

        sourceRect.left = currentFrame * frameWidth
        sourceRect.right = sourceRect.left + frameWidth

        return sourceRect
    }
}





