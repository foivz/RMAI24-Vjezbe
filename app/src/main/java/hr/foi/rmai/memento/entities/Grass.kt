package hr.foi.rmai.memento.entities

class Grass(locationX: Int, locationY: Int, type: Char)
    : GameObject(1f, 1f, 1, "turf", type) {
    init {
        traversable = true

        setWorldLocation(
            locationX.toFloat(),
            locationY.toFloat(),
            0
        )

        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {

    }
}