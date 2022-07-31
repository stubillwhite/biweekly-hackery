package hexed

// We can just treat this as a normal grid with diagonals being a half-step. To simplify, rather than deal with half-steps, we
// can just use a double-step for vertical movement, a single step for diagonals, and halve the final distance.

// Grid for reference
//
//    +----+        +----+        +----+
//   /      \      /      \      /      \
// -+        +----+        +----+        +-
//   \      /      \      /      \      /
//    +----+        +----+        +----+
//   /      \      /      \      /      \
// -+        +----+        +----+        +-
//   \      /      \      /      \      /
//    +----+        +----+        +----+
//   /      \      /      \      /      \
// -+        +----+        +----+        +-
//   \      /      \      /      \      /
//    +----+        +----+        +----+

object HexEd {

    enum class Direction(val symbol: String, val dx: Int, val dy: Int) {
        NORTH("n", 0, 2),
        NORTH_EAST("ne", 1, 1),
        SOUTH_EAST("se", 1, -1),
        SOUTH("s", 0, -2),
        SOUTH_WEST("sw", -1, -1),
        NORTH_WEST("nw", -1, 1);

        companion object {
            fun fromString(s: String): Direction {
                return Direction.values().first { it.symbol == s }
            }
        }
    }

    data class Location(val x: Int, val y: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        val directions = parseInput(readInput("hexed/input.txt"))
        val locationsVisited = locationsVisited(directions)

        // Part one
        val finalDistance = distanceFromOrigin(locationsVisited.last())
        println("Solution to part one: $finalDistance")

        // Part two
        val furthestDistance = locationsVisited.map { distanceFromOrigin(it) }.max()
        println("Solution to part two: $furthestDistance")

        println("Done")
    }

    private fun readInput(path: String): String {
        return Thread.currentThread().contextClassLoader.getResource(path)!!.readText()
    }

    private fun parseInput(s: String): List<Direction> {
        return s.split(",").map { Direction.fromString(it) }
    }

    private fun distanceFromOrigin(location: Location): Int {
        val (x, y) = location

        return when {
            x < 0 || y < 0 -> distanceFromOrigin(Location(Math.abs(x), Math.abs(y)))
            x == 0 -> y / 2
            y == 0 -> x
            else -> (1 + distanceFromOrigin(Location(x - 1, y - 1)))
        }
    }

    private fun newLocation(location: Location, direction: Direction): Location {
        return Location(location.x + direction.dx, location.y + direction.dy)
    }

    private fun locationsVisited(directions: List<Direction>): List<Location> {
        val origin = listOf(Location(0, 0))
        return directions.fold(origin) { locations, direction -> locations + newLocation(locations.last(), direction) }
    }
}