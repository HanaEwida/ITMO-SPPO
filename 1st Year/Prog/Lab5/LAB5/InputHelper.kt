package commands

import model.*

/**
 * Helper object for safe, validated user input.
 * Handles prompts, error messages, retries, and null values.
 */
object InputHelper {

    /**
     * Reads non-null, non-blank String from user.
     */
    fun readString(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrBlank()) return input
            println(" This field cannot be empty. Please try again.")
        }
    }

    /**
     * Reads nullable String (empty input = null).
     */
    fun readNullableString(prompt: String): String? {
        print(prompt)
        val input = readLine()?.trim()
        return if (input.isNullOrEmpty()) null else input
    }

    /**
     * Reads Long with validation (> minValue).
     */
    fun readLong(prompt: String, minValue: Long): Long {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            val value = input?.toLongOrNull()
            if (value != null && value > minValue) return value
            println(" Please enter a number > $minValue")
        }
    }

    /**
     * Reads Double (any value allowed).
     */
    fun readDouble(prompt: String): Double {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            val value = input?.toDoubleOrNull()
            if (value != null) return value
            println(" Please enter a valid decimal number")
        }
    }

    /**
     * Reads enum value with user-friendly prompt showing options.
     * Empty input returns null for nullable enums.
     */
    inline fun <reified T : Enum<T>> readEnum(
        prompt: String,
        nullable: Boolean = false
    ): T? {
        val constants = enumValues<T>().joinToString(", ")
        println("Available options: $constants")

        while (true) {
            print(prompt)
            val input = readLine()?.trim()

            if (input.isNullOrEmpty()) {
                if (nullable) return null
                println(" This field cannot be empty")
                continue
            }

            return try {
                enumValueOf<T>(input.uppercase())
            } catch (e: IllegalArgumentException) {
                println(" Invalid option. Choose from: $constants")
                null
            }
        }
    }

    /**
     * Reads Coordinates object with field-by-field prompts.
     */
    fun readCoordinates(): Coordinates {
        println("\n Enter coordinates:")
        val x = readDouble("  Enter x (decimal): ")
        val y = readLong("  Enter y (integer > -722): ", minValue = -722)
        return Coordinates(x, y)
    }

    /**
     * Reads complete Vehicle from user (excluding auto-generated fields).
     */
    fun readVehicle(): Vehicle {
        println("\n Enter vehicle details:")

        val name = readString("  Enter name: ")
        val coordinates = readCoordinates()
        val enginePower = readLong("  Enter engine power (> 0): ", minValue = 0)

        val type = readEnum<VehicleType>(
            "  Enter type (or press Enter for null): ",
            nullable = true
        )

        val fuelType = readEnum<FuelType>(
            "  Enter fuel type (required): ",
            nullable = false
        )!! // Non-null because nullable=false

        //  Auto-generate required fields (user never enters these)
        return Vehicle(
            id = utils.IdGenerator.generateId(),  // Auto-generated
            name = name,
            coordinates = coordinates,
            creationDate = java.util.Date(),       // Auto-generated
            enginePower = enginePower,
            type = type,
            fuelType = fuelType
        )
    }
}