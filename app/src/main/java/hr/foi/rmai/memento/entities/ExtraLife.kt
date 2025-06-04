package hr.foi.rmai.memento.entities

class ExtraLife(worldStartX: Int, worldStartY: Int, type: Char)
    : GameObject(0.8f, 0.65f, 1, "life", 'e'){

    init {
        setWorldLocation(
            worldStartX.toFloat(),
            worldStartY.toFloat(),
            0
        )
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {

    }
}
