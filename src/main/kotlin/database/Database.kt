package main.database

import main.tasks.Task
import java.awt.Color
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.full.declaredMemberProperties

/**
 * Singleton object representing a database manager, responsible for managing a SQLite database.
 */
object Database {
	/**
	 * Represents the home directory of the current user.
	 *
	 * This value is obtained from the system property "user.home".
	 */
	private val homeDirectory: String = System.getProperty("user.home")
	/**
	 * The path to the SQLite database file used by the application.
	 * This path is constructed using the user's home directory and places the database
	 * file in the `.local/share/tuimorrow` directory with the name `db.sqlite`.
	 */
	private val dbPath = "$homeDirectory/.local/share/tuimorrow/db.sqlite"
	/**
	 * The JDBC URL used to establish a database connection to an SQLite database.
	 * The URL is constructed dynamically using the specified database path.
	 */
	private val jdbcUrl = "jdbc:sqlite:$dbPath"
	/**
	 * A `DateTimeFormatter` pattern used for formatting and parsing
	 * date and time strings in the format `yyyy-MM-dd HH:mm:ss`.
	 *
	 * Utilized in functions to convert `LocalDateTime` objects to
	 * formatted strings for database insertion and vice versa.
	 */
	private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	/**
	 * Represents a database connection used for executing various SQL operations.
	 *
	 * This variable is intended to establish a connection to a database, allowing
	 * for the creation of tables, insertion of tasks, retrieval of tasks, and
	 * retrieval of list colors.
	 *
	 * The connection is managed via:
	 * - [connect]: Opens the connection to the database.
	 * - [disconnect]: Closes the connection to the database.
	 *
	 * The connection state is checked and utilized in various functions like
	 * [createTablesIfNotExists], [insertTask], [getTasks], [getListColor], and [getAllListColors]
	 * to perform the necessary database operations.
	 */
	private var connection: Connection? = null

	/**
	 * Creates the database file if it does not already exist.
	 *
	 * This method checks for the existence of the specified SQLite file. If the file does not exist, it creates the file along
	 * with necessary parent directories. Following that, it initializes the database tables by calling `createTablesIfNotExists`.
	 *
	 * The method handles the creation of the SQLite file and calls another method to ensure that the required tables exist.
	 *
	 * Prints messages to the console regarding the creation status of the directories, file, and tables.
	 */
	fun createDBIfNotExists() {
		// Construct the full path to the SQLite file
		val dbFile = File(dbPath)

		// Check if the file exists
		if (!dbFile.exists()) {
			println("File does not exist. Creating it now...")

			// Create parent directories if they don't exist
			val parentDir = dbFile.parentFile
			if (!parentDir.exists()) {
				parentDir.mkdirs()
				println("Created directories: ${parentDir.path}")
			}

			// Create the SQLite file
			dbFile.createNewFile()
			println("Created SQLite file at: ${dbFile.path}")
		} else {
			println("SQLite file already exists at: ${dbFile.path}")
		}

		createTablesIfNotExists()
	}

	/**
	 * Creates the necessary `Task` and `List` tables in the database if they do not already exist.
	 *
	 * - The `Task` table contains columns for `name`, `list`, `createDateTime`, and `dueDateTime` with a composite primary key on `name` and `dueDateTime`.
	 * - The `List` table contains columns for `name` and `color` with a primary key on `name`.
	 *
	 * This function attempts to execute the create table SQL statements within a database connection.
	 * If successful, it prints confirmation messages. If it fails, it prints an error message.
	 *
	 * @throws SQLException If a database access error occurs or the SQL statements are invalid.
	 */
	private fun createTablesIfNotExists() {
		val createTaskTableSQL = """
            CREATE TABLE IF NOT EXISTS Task (
                name TEXT ,
                list TEXT NOT NULL,
                createDate DATE NOT NULL,
                dueDate DATE,
				PRIMARY KEY (name, dueDate)
            );
        """.trimIndent()

		val createListTableSQL = """
            CREATE TABLE IF NOT EXISTS List (
                name TEXT PRIMARY KEY,
                color TEXT NOT NULL
            );
        """.trimIndent()

		try {
			connection?.createStatement()?.use { statement ->
				// Create Task table
				statement.execute(createTaskTableSQL)
				println("Task table created or already exists.")

				// Create List table
				statement.execute(createListTableSQL)
				println("List table created or already exists.")
			}
		} catch (e: SQLException) {
			println("Failed to create tables: ${e.message}")
		}
	}

	/**
	 * Establishes a connection to the database using the configured JDBC URL.
	 *
	 * Attempts to load the SQLite JDBC driver and connect to the database.
	 * Prints a success message if the connection is established, or an error
	 * message if it fails to load the driver or connect to the database.
	 *
	 * Exceptions:
	 * @throws ClassNotFoundException if the SQLite JDBC driver class is not found.
	 * @throws SQLException if a database access error occurs.
	 */
	fun connect() {
		try {
			// Establish the database connection
			connection = DriverManager.getConnection(jdbcUrl)

			println("Connected to the database.")
		} catch (e: ClassNotFoundException) {
			println("Failed to load SQLite JDBC driver: ${e.message}")
		} catch (e: SQLException) {
			println("Failed to connect to the database: ${e.message}")
		}
	}

	/**
	 * Disconnects the current database connection if one exists.
	 * This method closes the connection and prints a confirmation message to the console.
	 */
	fun disconnect() {
		connection?.close()
		println("Database connection closed.")
	}

	/**
	 * Inserts a task into the database.
	 *
	 * @param task The task to be inserted, containing properties such as name,
	 *             list name, due date and time, and creation date and time.
	 */
	fun insertTask(task: Task) {
		val sql = "INSERT INTO Task VALUES (?, ?, ?, ?)"
		var preparedStatement: PreparedStatement? = null
		try {
			if (connection == null) {
				throw SQLException("Database connection is null.")
			}

			preparedStatement = connection!!.prepareStatement(sql)
			// Convert Dates to Strings or compatible SQL date object
			val formattedCreateDateTime = task.createDate.toString() // or your desired format
			val formattedEndDateTime = task.dueDate.toString()      // or your desired format

			// Set the properties explicitly for better manageability
			preparedStatement.setString(1, task.name)
			preparedStatement.setString(2, task.listName)
			preparedStatement.setDate(3, java.sql.Date.valueOf(task.dueDate))
			preparedStatement.setDate(4, java.sql.Date.valueOf(task.createDate))

			val rowsAffected = preparedStatement.executeUpdate()
			println("$rowsAffected row(s) inserted successfully.")
		} catch (e: SQLException) {
			println("Failed to insert task: ${e.message}")
		} finally {
			try {
				preparedStatement?.close()
			} catch (e: SQLException) {
				println("Failed to close PreparedStatement: ${e.message}")
			}
		}
	}

	/**
	 * Retrieves tasks from the database and adds them to the given TreeSet.
	 * If the specified list is null, retrieves all tasks.
	 *
	 * @param ts The TreeSet to which the retrieved tasks will be added.
	 * @param list The name of the list to filter tasks by. If null, all tasks are retrieved.
	 */
// Get all tasks from the database and put them into ts, then return that. Setting list to null retrieves all tasks
	fun getTasks(ts: TreeSet<Task>, list: String?) {
		try {
			ts.clear()
			val statement = connection?.createStatement()

			// Execute a query
			var statementWhere = ""
			if (list != null) statementWhere = " WHERE list=$list"
			val resultSet = statement?.executeQuery("SELECT * FROM Task$statementWhere")

			// Process the results
			while (resultSet?.next()!!) {
				val name = resultSet.getString("name")
				val taskList = resultSet.getString("list")
				val createDate = resultSet.getDate("createDate").toLocalDate()
				val endDate = resultSet.getDate("dueDate").toLocalDate()

				// Create a Task object and add it to the set
				ts.add(Task(name, taskList, createDate, endDate))
			}
		} catch (e: Exception) {
			throw e
		}
	}

	/**
	 * Retrieves the color associated with the specified list name from the database.
	 *
	 * @param listName The name of the list for which the color is to be retrieved.
	 * @return The color associated with the specified list name.
	 * @throws NoSuchElementException If no list with the specified name is found.
	 * @throws SQLException If there is an error while accessing the database.
	 */
	fun getListColor(listName: String): Color {
		try {
			// Prepare SQL query to get the color for the specified list name
			val sql = "SELECT color FROM List WHERE name = ?"
			val preparedStatement = connection?.prepareStatement(sql)

			// Set the list name parameter in the query
			preparedStatement?.setString(1, listName)

			// Execute the query
			val resultSet = preparedStatement?.executeQuery()

			// If a result is found, convert the color string to a java.awt.Color object
			if (resultSet != null && resultSet.next()) {
				val colorString = resultSet.getString("color")

				// Assuming color is stored in hex format (e.g., "#FF5733")
				return Color.decode(colorString)
			} else {
				// Handle case when the list name is not found
				println("List '$listName' not found.")
				throw NoSuchElementException("List with name '$listName' not found.")
			}
		} catch (e: SQLException) {
			throw e
		}
	}

	/**
	 * Retrieves all list colors from the database and populates the provided map.
	 *
	 * @param colors A HashMap where the key is the list name and the value is the corresponding Color object.
	 */
	fun getAllListColors(colors: HashMap<String, Color>) {
		try {
			// Prepare SQL query to get the color for the specified list name
			val sql = "SELECT * FROM List"
			val preparedStatement = connection?.prepareStatement(sql)

			// Execute the query
			val resultSet = preparedStatement?.executeQuery()

			// If a result is found, convert the color string to a java.awt.Color object
			if (resultSet != null) {
				while (resultSet.next()) {
					colors[resultSet.getString("name")] = Color.decode(resultSet.getString("color"))
				}
			}
		} catch (e: SQLException) {
			throw e
		}
	}
}