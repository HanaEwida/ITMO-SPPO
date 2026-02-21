package model

import java.util.Date

/**
 * Represents a vehicle entity stored in the collection.
 * Implements Comparable for natural sorting by ID (required by task).
 *
 * All fields follow strict validation rules as specified in requirements.
 * Fields marked as auto-generated are never set by user input.
 *
 * @property id Unique identifier (>0, auto-generated, never null)
 * @property name Vehicle name (non-null, non-empty string)
 * @property coordinates Location coordinates (non-null, validated)
 * @property creationDate Timestamp when vehicle was added (auto-generated, never null)
 * @property enginePower Engine power in units (>0)
 * @property type Vehicle category (nullable: PLANE/DRONE/BOAT/BICYCLE)
 * @property fuelType Energy source (non-null: DIESEL/NUCLEAR/PLASMA/ANTIMATTER)
 *
 * @constructor Creates a new Vehicle with validation
 * @throws IllegalArgumentException if any field violates constraints
 */
data class Vehicle(  //  Added 'data' keyword here!
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: Date,
    val enginePower: Long,
    val type: VehicleType?,
    val fuelType: FuelType
) : Comparable<Vehicle> {

    init {
        require(id > 0) { "Vehicle.id must be > 0, but was $id" }
        require(name.isNotBlank()) { "Vehicle.name cannot be empty" }
        require(enginePower > 0) { "Vehicle.enginePower must be > 0, but was $enginePower" }
    }

    /**
     * Natural ordering: vehicles sorted by ID ascending.
     * Required for "sorting by default" requirement.
     */
    override fun compareTo(other: Vehicle): Int = this.id.compareTo(other.id)

    //  data class auto-generates equals(), hashCode(), toString()
    // But we can still override toString() for custom format if needed
    override fun toString(): String = buildString {
        append("Vehicle{id=$id, name='$name', coordinates=$coordinates, ")
        append("created=${creationDate}, power=$enginePower, ")
        append("type=${type ?: "null"}, fuel=$fuelType}")
    }
}