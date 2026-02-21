package io

import model.*
import utils.IdGenerator
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility for loading Vehicle collection from CSV file.
 * Uses InputStreamReader as required by task specification.
 *
 * CSV Format (one vehicle per line):
 * id,name,x,y,creationDate,enginePower,type,fuelType
 * Example: 1,Tesla Model S,45.7,100,2026-02-21,500,CAR,DIESEL
 */
object CsvLoader {

    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(DATE_FORMAT)

    /**
     * Loads vehicles from CSV file into HashMap.
     *
     * @param filename Path to CSV file
     * @return HashMap<Long, Vehicle> with loaded data, or empty map on error
     */
    fun load(filename: String): java.util.HashMap<Long, Vehicle> {
        val collection = java.util.HashMap<Long, Vehicle>()
        var maxId = 0L

        try {
            //  REQUIRED: Use InputStreamReader for reading
            InputStreamReader(FileInputStream(filename), Charsets.UTF_8).use { reader ->
                reader.buffered().forEachLine { line ->
                    if (line.isBlank()) return@forEachLine

                    try {
                        val vehicle = parseLine(line)
                        collection[vehicle.id] = vehicle
                        if (vehicle.id > maxId) maxId = vehicle.id
                    } catch (e: Exception) {
                        System.err.println(" Skipping invalid line: $line")
                        System.err.println("   Error: ${e.message}")
                    }
                }
            }
            IdGenerator.initialize(maxId)
            println(" Loaded ${collection.size} vehicles from $filename")

        } catch (e: java.io.FileNotFoundException) {
            println(" File not found: $filename - starting with empty collection")
        } catch (e: SecurityException) {
            System.err.println(" Permission denied reading: $filename")
        } catch (e: Exception) {
            System.err.println(" Error loading file: ${e.message}")
        }

        return collection
    }

    /**
     * Parses one CSV line into a Vehicle object.
     * @throws Exception if parsing fails or validation fails
     */
    private fun parseLine(line: String): Vehicle {
        val parts = line.split(",").map { it.trim() }
        require(parts.size == 8) { "Expected 8 fields, got ${parts.size}" }

        val id = parts[0].toLong()
        val name = parts[1]
        val x = parts[2].toDouble()
        val y = parts[3].toLong()
        val creationDate = dateFormat.parse(parts[4])
        val enginePower = parts[5].toLong()

        // Use specific functions instead of complex generics
        val type = parseVehicleType(parts[6])
        val fuelType = parseFuelType(parts[7])

        return Vehicle(
            id = id,
            name = name,
            coordinates = Coordinates(x, y),
            creationDate = creationDate,
            enginePower = enginePower,
            type = type,
            fuelType = fuelType
        )
    }

    /**
     * Parses VehicleType from string, returns null for empty string.
     */
    private fun parseVehicleType(value: String): VehicleType? {
        if (value.isBlank()) return null
        return try {
            VehicleType.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Parses FuelType from string (required, throws if invalid).
     */
    private fun parseFuelType(value: String): FuelType {
        return try {
            FuelType.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid FuelType: $value. Options: ${FuelType.values().joinToString(", ")}")
        }
    }
}