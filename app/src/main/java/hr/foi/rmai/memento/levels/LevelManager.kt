package hr.foi.rmai.memento.levels

import android.content.Context
import android.graphics.Bitmap
import hr.foi.rmai.memento.entities.GameObject
import hr.foi.rmai.memento.entities.Grass
import hr.foi.rmai.memento.entities.Player

class LevelManager(
    level: String,
    context: Context,
    pixelsPerMeter: Int,
    playerX: Float,
    playerY: Float
) {
    val gameObjects: ArrayList<GameObject> = ArrayList()
    val bitmaps: Array<Bitmap?> = arrayOfNulls(20)

    private var currentLevel: LevelData? = null
    var playing = false
    private var currentIndex = 0
    var player: Player
    var gravity: Float = 6f

    init {
        player = Player(0f, 0f)
        currentLevel = when (level) {
            "TestLevel" -> TestLevel()
            "LevelCave" -> LevelCave()
            else -> TestLevel()
        }

        loadMapData(context, pixelsPerMeter, playerX, playerY)
        playing = true
    }

    private fun loadMapData(
        context: Context,
        pixelsPerMeter: Int,
        playerX: Float,
        playerY: Float
    ) {
        val levelHeight = currentLevel!!.tiles.size
        val levelWidth = currentLevel!!.tiles[0].length

        var c: Char

        for (j in 0..<levelWidth) {
            for (i in 0..<levelHeight) {
                c = currentLevel!!.tiles[i][j]

                if (c != '.') {
                    when (c) {
                        '1' -> gameObjects.add(Grass(j, i, c))
                        'p' -> {
                            player = Player(playerX, playerY)
                            gameObjects.add(player)
                        }
                    }

                    if (bitmaps[getBitmapIndex(c)] == null) {
                        bitmaps[getBitmapIndex(c)] =
                            gameObjects[currentIndex].prepareBitmap(
                                context,
                                pixelsPerMeter
                            )
                    }

                    currentIndex++
                }
            }
        }
    }

    fun getBitmapIndex(blockType: Char): Int {
        return when (blockType) {
            '1' -> 1
            'p' -> 2
            else -> 0
        }
    }

    fun getBitmap(blockType: Char): Bitmap {
        val index = getBitmapIndex(blockType)

        return bitmaps[index]!!
    }

    fun switchPlayingStatus() {
        playing = !playing

        if (playing) {
            gravity = 6f
        } else {
            gravity = 0f
        }
    }
}








