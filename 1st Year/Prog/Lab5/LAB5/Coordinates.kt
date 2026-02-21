package model

/**
 * Represents geographic coordinates for vehicle positioning.
 * Used to store location data for vehicles in the collection.
 *
 * @property x The X coordinate (decimal value, any real number)
 * @property y The Y coordinate (must be greater than -722)
 * @throws IllegalArgumentException if y <= -722
 */
class Coordinates(
    val x: Double,
    val y: Long
) {
    init {
        require(y > -722) { "Coordinates.y must be greater than -722, but was $y" }
    }

    override fun toString(): String = "Coordinates{x=$x, y=$y}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Coordinates
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}