package commands

import model.*
import io.CsvSaver
import utils.IdGenerator
import java.util.*
import kotlin.collections.HashMap

/**
 * Manages all interactive commands for the Vehicle collection.
 * Implements the command pattern with validation and error handling.
 */
class CommandManager(
    private val collection: java.util.HashMap<Long, Vehicle>,
    private val filename: String
) {
    private val initializationDate = Date()

    /**
     * Processes user command and returns whether to continue running.
     * @return true to continue, false to exit program
     */
    /**
     * Processes user command and returns whether to continue running.
     * @return true to continue, false to exit program
     */
    fun execute(input: String): Boolean {
        val parts = input.trim().split(Regex("\\s+"), limit = 2)
        val command = parts[0].lowercase()
        val args = if (parts.size > 1) parts[1] else ""

        return when (command) {
            "help" -> {
                showHelp()
                true
            }
            "info" -> {
                showInfo()
                true
            }
            "show" -> {
                showAll()
                true
            }
            "insert" -> {
                handleInsert(args)
            }
            "update" -> {
                handleUpdate(args)
            }
            "remove_key" -> {
                handleRemoveKey(args)
            }
            "clear" -> {
                clearCollection()
            }
            "save" -> {
                saveCollection()
            }
            "exit" -> false  //  Single value, no braces needed
            "remove_greater" -> {
                handleRemoveGreater(args)
            }
            "replace_if_greater" -> {
                handleReplaceIfGreater(args)
            }
            "remove_greater_key" -> {
                handleRemoveGreaterKey(args)
            }
            "remove_all_by_fuel_type" -> {
                handleRemoveByFuelType(args)
            }
            "count_greater_than_type" -> {
                handleCountByType(args)
            }
            "print_field_descending_type" -> {
                printTypesDescending()
            }
            "execute_script" -> {
                executeScript(args)
            }
            else -> {
                println(" Unknown command. Type 'help' for list.")
                true
            }
        }
    }

    private fun showHelp() {
        println("""
 Available commands:
  help                          - Show this help message
  info                          - Show collection statistics
  show                          - Display all vehicles
  insert null {element}         - Add new vehicle (enter details when prompted)
  update id {element}           - Update vehicle with given ID
  remove_key null               - Remove vehicle by key (ID)
  clear                         - Remove all vehicles
  save                          - Save collection to file
  exit                          - Exit without saving
  remove_greater {element}      - Remove vehicles > specified vehicle
  replace_if_greater null {element} - Replace if new > old by key
  remove_greater_key null       - Remove vehicles with key > given key
  remove_all_by_fuel_type fuelType - Remove by fuel type
  count_greater_than_type type  - Count vehicles with type > specified
  print_field_descending_type   - Print types in descending order
  execute_script file_name      - Run commands from file
        """.trimIndent())
    }

    private fun showInfo() {
        println("""
 Collection Info:
  Type: ${collection.javaClass.simpleName}
  Initialized: $initializationDate
  Elements: ${collection.size}
  Storage file: $filename
  Next auto ID: ${IdGenerator.peekNextId()}
        """.trimIndent())
    }

    private fun showAll() {
        if (collection.isEmpty()) {
            println(" Collection is empty")
            return
        }
        // ✅ Sorting by default using Comparable
        collection.values.sorted().forEach { println(it) }
    }

    private fun handleInsert(args: String): Boolean {
        // Format: "insert null" - the "null" is literal, vehicle details come via prompts
        if (args.trim() != "null") {
            println(" Usage: insert null")
            return false
        }
        try {
            val vehicle = InputHelper.readVehicle()
            collection[vehicle.id] = vehicle
            println(" Added: ${vehicle.name} (ID=${vehicle.id})")
            return true
        } catch (e: Exception) {
            println(" Error adding vehicle: ${e.message}")
            return false
        }
    }

    private fun handleUpdate(args: String): Boolean {
        val parts = args.trim().split(" ", limit = 2)
        if (parts.size < 2 || parts[0].trim() !in collection.keys.map { it.toString() }) {
            println(" Usage: update <id> (then enter new vehicle details)")
            return false
        }
        val id = parts[0].toLong()
        try {
            val updated = InputHelper.readVehicle().copy(id = id) // Keep original ID
            collection[id] = updated
            println(" Updated vehicle ID=$id")
            return true
        } catch (e: Exception) {
            println(" Error updating: ${e.message}")
            return false
        }
    }

    private fun handleRemoveKey(args: String): Boolean {
        if (args.trim() != "null") {
            println(" Usage: remove_key null")
            return false
        }
        print("Enter key (ID) to remove: ")
        val key = readLine()?.trim()?.toLongOrNull()
        if (key == null) {
            println(" Invalid key")
            return false
        }
        if (collection.remove(key) != null) {
            println(" Removed vehicle with ID=$key")
        } else {
            println(" No vehicle found with ID=$key")
        }
        return true
    }

    private fun clearCollection(): Boolean {
        val count = collection.size
        collection.clear()
        println(" Cleared $count vehicles from collection")
        return true
    }

    private fun saveCollection(): Boolean {
        return CsvSaver.save(filename, collection)
    }

    private fun handleRemoveGreater(args: String): Boolean {
        // For simplicity: prompt user to enter comparison vehicle
        println("Enter vehicle to compare against:")
        try {
            val comparator = InputHelper.readVehicle()
            val before = collection.size
            // Remove all vehicles that are "greater than" comparator (by ID)
            collection.entries.removeIf { it.value > comparator }
            println(" Removed ${before - collection.size} vehicles")
            return true
        } catch (e: Exception) {
            println(" Error: ${e.message}")
            return false
        }
    }

    private fun handleReplaceIfGreater(args: String): Boolean {
        if (args.trim() != "null") {
            println(" Usage: replace_if_greater null")
            return false
        }
        print("Enter key (ID) to check: ")
        val key = readLine()?.trim()?.toLongOrNull() ?: return false.also {
            println(" Invalid key")
        }
        val existing = collection[key] ?: return false.also {
            println(" No vehicle with ID=$key")
        }
        println("Enter new vehicle details:")
        try {
            val candidate = InputHelper.readVehicle().copy(id = key)
            if (candidate > existing) {
                collection[key] = candidate
                println("Replaced vehicle ID=$key (new value is greater)")
            } else {
                println("New value not greater - no change made")
            }
            return true
        } catch (e: Exception) {
            println(" Error: ${e.message}")
            return false
        }
    }

    private fun handleRemoveGreaterKey(args: String): Boolean {
        if (args.trim() != "null") {
            println(" Usage: remove_greater_key null")
            return false
        }
        print("Enter key threshold: ")
        val threshold = readLine()?.trim()?.toLongOrNull() ?: return false.also {
            println("Invalid number")
        }
        val removed = collection.keys.filter { it > threshold }.also { keys ->
            keys.forEach { collection.remove(it) }
        }.size
        println(" Removed $removed vehicles with key > $threshold")
        return true
    }

    private fun handleRemoveByFuelType(args: String): Boolean {
        val fuelArg = args.trim().uppercase()
        if (fuelArg !in FuelType.values().map { it.name }) {
            println("Invalid fuel type. Options: ${FuelType.values().joinToString(", ")}")
            return false
        }
        val target = FuelType.valueOf(fuelArg)
        val removed = collection.entries.removeIf { it.value.fuelType == target }
        println(" Removed $removed vehicles with fuelType=$target")
        return true
    }

    private fun handleCountByType(args: String): Boolean {
        val typeArg = args.trim().uppercase()
        if (typeArg !in VehicleType.values().map { it.name }) {
            println(" Invalid type. Options: ${VehicleType.values().joinToString(", ")}")
            return false
        }
        val target = VehicleType.valueOf(typeArg)
        // Count vehicles whose type.ordinal > target.ordinal (enum order)
        val count = collection.values.count {
            it.type != null && it.type.ordinal > target.ordinal
        }
        println(" Vehicles with type > $target: $count")
        return true
    }

    private fun printTypesDescending(): Boolean {
        val types = collection.values.mapNotNull { it.type }
            .sortedDescending()
        if (types.isEmpty()) {
            println("No vehicles with type set")
        } else {
            println(" Types in descending order:")
            types.forEach { println("  - $it") }
        }
        return true
    }

    private fun executeScript(filename: String): Boolean {
        return try {
            val file = java.io.File(filename)
            val scriptFile = if (file.isAbsolute) {
                file
            } else {
                // Try project root directory
                java.io.File(System.getProperty("user.dir"), filename)
            }

            println(" Executing script: $filename")
            scriptFile.forEachLine { line ->
                if (line.isNotBlank() && !line.startsWith("#")) {
                    println("> $line")
                    execute(line)
                }
            }
            true
        } catch (e: Exception) {
            println("Script error: ${e.message}")
            true  // Continue running
        }
    }
    // Helper to read Long from console (for commands that need it)
    private fun readLine(): String? = kotlin.io.readLine()
}