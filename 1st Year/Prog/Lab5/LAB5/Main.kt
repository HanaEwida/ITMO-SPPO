import commands.CommandManager
import io.CsvLoader
import java.util.*

/**
 * Main entry point for Vehicle Collection Manager.
 *
 * Usage: kotlin MainKt <filename.csv>
 *
 * Loads vehicles from CSV on startup, then enters interactive command loop.
 * All classes documented with KDoc (compatible with javadoc).
 */
fun main(args: Array<String>) {
    // ✅ Command line argument for filename
    if (args.isEmpty()) {
        println("❌ Usage: program <filename.csv>")
        println("Example: kotlin MainKt vehicles.csv")
        return
    }

    val filename = args[0]
    println(" Vehicle Collection Manager")
    println(" Loading from: $filename")

    //  Auto-populate from file on startup using InputStreamReader
    val collection = CsvLoader.load(filename)

    //  Use java.util.HashMap as required
    val commandManager = CommandManager(collection, filename)

    println(" Ready! Type 'help' to see commands.\n")

    // Interactive command loop
    while (true) {
        try {
            print("> ")
            val input = readLine()?.trim() ?: continue

            if (input.isBlank()) continue

            val shouldContinue = commandManager.execute(input)
            if (!shouldContinue) {
                println(" Goodbye! (Use 'save' before exit to keep changes)")
                break
            }
        } catch (e: Exception) {
            System.err.println(" Unexpected error: ${e.message}")
            e.printStackTrace()
        }
    }
}