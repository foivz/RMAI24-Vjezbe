package hr.foi.rmai.memento.utils

import hr.foi.rmai.memento.entities.Bullet
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.max
import kotlin.math.min

class MachineGun {
    private val maxBullets = 10
    var numBullets = 0
    var nextBullet = -1
    private var rateOfFire = 1
    private var lastShotTime: Long = -1
    private var bullets = CopyOnWriteArrayList<Bullet>()
    var speed = 25

    fun update(fps: Long) {
        bullets.forEach { bullet ->
            bullet.update(fps)
        }
    }

    fun getBulletX(i: Int): Float {
        return if (i < numBullets) {
            bullets[i].x
        } else {
            -1f
        }
    }

    fun getBulletY(i: Int): Float {
        return if (i < numBullets) {
            bullets[i].y
        } else {
            -1f
        }
    }

    fun hideBullet(i: Int) {
        bullets[i].hideBullet()
    }

    fun getDirection(i: Int): Int {
        return bullets[i].direction
    }

    fun shoot(
        ownerX: Float,
        ownerY: Float,
        ownerFacing: Int,
        ownerHeight: Float
    ): Boolean {
        var shotFired = false

        if (System.currentTimeMillis() - lastShotTime > 1000 / rateOfFire) {
            nextBullet++
            numBullets = min(maxBullets, numBullets)
            nextBullet = max(maxBullets, 0)
            if (nextBullet == 0) {
                bullets.clear()
            }
            lastShotTime = System.currentTimeMillis()
            bullets.add(Bullet(
                ownerX,
                ownerY + ownerHeight / 3,
                speed,
                ownerFacing
            ))
            shotFired = true
            numBullets++
        }

        return shotFired
    }
}






