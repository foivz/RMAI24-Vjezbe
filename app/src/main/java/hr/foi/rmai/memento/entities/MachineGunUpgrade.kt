package hr.foi.rmai.memento.entities

class MachineGunUpgrade(worldStartX: Int, worldStartY: Int)
    : GameObject(1f, 1f, 1, "clip", 'u') {
    init {
        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {

    }

}