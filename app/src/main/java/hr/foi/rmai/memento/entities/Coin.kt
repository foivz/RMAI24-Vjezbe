package hr.foi.rmai.memento.entities

class Coin(worldStartX: Int, worldStartY: Int, type: Char)
    : GameObject(0.5f, 0.5f, 1, "coin", 'c'){

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