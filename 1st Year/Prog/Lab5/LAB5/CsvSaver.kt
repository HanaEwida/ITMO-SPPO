package io

import model.Vehicle
import java.io.FileOutputStream
import java.io.BufferedOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility for saving Vehicle collection to CSV file.
 * Uses BufferedOutputStream as required by task specification.
 */
object CsvSaver {

    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(DATE_FORMAT)

    /**
     * Saves all vehicles to CSV file.
     *
     * @param filename Output file path
     * @param collection HashMap of vehicles to save
     * @return true if save succeeded, false otherwise
     */
    fun save(filename: String, collection: java.util.HashMap<Long, Vehicle>): Boolean {
        return try {
            // REQUIRED: Use BufferedOutputStream for writing
            BufferedOutputStream(FileOutputStream(filename)).use { stream ->
                collection.values.forEach { vehicle ->
                    val line = toCsvLine(vehicle) + "\n"
                    stream.write(line.toByteArray(Charsets.UTF_8))
                }
            }
            println("Saved ${collection.size} vehicles to $filename")
            true

        } catch (e: SecurityException) {
            System.err.println("Permission denied writing to: $filename")
            false
        } catch (e: Exception) {
            System.err.println("Error saving file: ${e.message}")
            false
        }
    }

    /**
     * Converts Vehicle to CSV line format.
     */
    private fun toCsvLine(v: Vehicle): String = buildString {
        append("${v.id},")
        append("${v.name},")
        append("${v.coordinates.x},")
        append("${v.coordinates.y},")
        append("${dateFormat.format(v.creationDate)},")
        append("${v.enginePower},")
        append("${v.type?.name ?: ""},")  // Empty string for null
        append(v.fuelType.name)
    }
}