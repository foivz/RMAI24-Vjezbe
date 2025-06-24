package hr.foi.rmai.memento.levels

import hr.foi.rmai.memento.entities.BackgroundData

class TestLevel: LevelData() {
    init {
        tiles.add("p............g..........d")
        tiles.add("111......................")
        tiles.add("............111..........")
        tiles.add("...............111.......")
        tiles.add("....................c.e..")
        tiles.add("....11.............111111")

        backgroundDataList.add(BackgroundData(
            "forest",
            -1,
            -7f,
            4f,
            20f,
            20
        ))
    }
}