package hr.foi.rmai.memento.levels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import hr.foi.rmai.memento.entities.Background
import hr.foi.rmai.memento.entities.Coin
import hr.foi.rmai.memento.entities.Drone
import hr.foi.rmai.memento.entities.ExtraLife
import hr.foi.rmai.memento.entities.GameObject
import hr.foi.rmai.memento.entities.Grass
import hr.foi.rmai.memento.entities.Guard
import hr.foi.rmai.memento.entities.Player

class LevelManager(
    level: String,
    context: Context,
    pixelsPerMeter: Int,
    playerX: Float,
    playerY: Float,
    screenWidth: Int
) {
    val gameObjects: ArrayList<GameObject> = ArrayList()
    val bitmaps: Array<Bitmap?> = arrayOfNulls(20)
    var backgrounds: ArrayList<Background> = ArrayList()

    private var currentLevel: LevelData? = null
    var playing = false
    private var currentIndex = 0
    var player: Player
    var gravity: Float = 6f

    init {
        player = Player(0f, 0f, pixelsPerMeter)
        currentLevel = when (level) {
            "TestLevel" -> TestLevel()
            "LevelCave" -> LevelCave()
            else -> TestLevel()
        }

        loadBackgrounds(
            context,
            pixelsPerMeter,
            screenWidth
        )
        loadMapData(context, pixelsPerMeter, playerX, playerY)
        setWaypoints()
        playing = false
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
                            player = Player(playerX, playerY, pixelsPerMeter)
                            gameObjects.add(player)
                        }
                        'c' -> gameObjects.add(Coin(j, i, c))
                        'e' -> gameObjects.add(ExtraLife(j, i, c))
                        'd' -> gameObjects.add(Drone(j, i))
                        'g' -> gameObjects.add(Guard(j, i, pixelsPerMeter))
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
            'c' -> 3
            'e' -> 4
            'd' -> 5
            'g' -> 6
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

    private fun isTileTwoSpacesBelow(
        tile: GameObject,
        guard: GameObject
    ): Boolean {
        return tile.worldLocation.y == guard.worldLocation.y + 2
    }

    private fun isXCoordSame(
        tile: GameObject,
        guard: GameObject
    ): Boolean {
        return tile.worldLocation.x == guard.worldLocation.x
    }

    private fun getLeftmostTile(startTileIndex: Int): Float {
        for (i in 0..4) {
            return if (!gameObjects[startTileIndex - i].traversable) {
                gameObjects[startTileIndex - (i + 1)].worldLocation.x
            } else {
                gameObjects[startTileIndex - 5].worldLocation.x
            }
        }

        return -1f
    }

    private fun getRightmostTile(startTileIndex: Int): Float {
        for (i in 0..4) {
            return if (!gameObjects[startTileIndex + i].traversable) {
                gameObjects[startTileIndex + (i - 1)].worldLocation.x
            } else {
                gameObjects[startTileIndex + 5].worldLocation.x
            }
        }

        return -1f
    }

    private fun findWaypoints(guard: GameObject) {
        var startTileIndex = -1
        var waypointX1: Float
        var waypointX2: Float

        for (tile in gameObjects) {
            startTileIndex++
            if (isTileTwoSpacesBelow(tile, guard)) {
                if (isXCoordSame(tile, guard)) {
                    waypointX1 = getLeftmostTile(startTileIndex)
                    waypointX2 = getRightmostTile(startTileIndex)

                    Log.i("aaaa", "x1:" + waypointX1.toString())
                    Log.i("aaaa", "x2:" + waypointX2.toString())

                    val g = guard as Guard
                    g.setWaypoints(waypointX1, waypointX2)
                }
            }
        }
    }

    fun setWaypoints() {
        for (guard in gameObjects) {
            if (guard.type == 'g') {
                findWaypoints(guard)
            }
        }
    }

    private fun loadBackgrounds(
        context: Context,
        pixelsPerMeter: Int,
        screenWidth: Int
    ) {
        backgrounds = ArrayList()
        for (bgData in currentLevel!!.backgroundDataList) {
            backgrounds.add(
                Background(
                    context,
                    pixelsPerMeter,
                    screenWidth,
                    bgData
                )
            )
        }
    }
}








