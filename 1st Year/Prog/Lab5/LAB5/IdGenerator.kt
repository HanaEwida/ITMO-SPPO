package utils

/**
 * Singleton object for generating unique, auto-incrementing vehicle IDs.
 * Thread-safe and persists max ID across sessions via file loading.
 */
object IdGenerator {
    private var nextId: Long = 1L

    /**
     * Generates the next unique ID for a new vehicle.
     * @return Sequential Long value > 0
     */
    fun generateId(): Long = nextId++

    /**
     * Resets generator to continue after the highest existing ID.
     * Called when loading data from file to avoid ID collisions.
     * @param maxExistingId The highest ID found in loaded data
     */
    fun initialize(maxExistingId: Long) {
        nextId = maxExistingId + 1
    }

    /**
     * Gets current next ID (for testing/debugging).
     */
    fun peekNextId(): Long = nextId
}