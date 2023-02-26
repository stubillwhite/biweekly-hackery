package labyrinth.internal

data class Labyrinth(
    val width: Int,
    val height: Int,
    val passages: Set<Passage>,
    val start: Room,
    val end: Room
)