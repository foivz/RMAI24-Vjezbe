package hr.foi.rmai.memento.entities

class Player(locationX: Float, locationY: Float)
    : GameObject(1, 2, 5, "player", 'p') {
    init {
        setWorldLocation(
            locationX.toFloat(),
            locationY.toFloat(),
            0
        )
    }

    override fun update(fps: Int, gravity: Float) {

    }
}