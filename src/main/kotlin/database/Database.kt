package main.database

import main.tasks.Task
import java.awt.Color
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.full.declaredMemberProperties

object Database {
	private val homeDirectory: String = System.getProperty("user.home")
	private val dbPath = "$homeDirectory/.local/share/tuimorrow/db.sqlite"
	private val jdbcUrl = "jdbc:sqlite:$dbPath"
	private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	private var connection: Connection? = null

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

	fun createTablesIfNotExists() {
		val createTaskTableSQL = """
            CREATE TABLE IF NOT EXISTS Task (
                name TEXT ,
                list TEXT NOT NULL,
                createDateTime TEXT NOT NULL,
                dueDateTime TEXT,
				PRIMARY KEY (name, dueDateTime)
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

	fun disconnect() {
		connection?.close()
		println("Database connection closed.")
	}

	fun insertTask(task: Task) {
		try {
			val sql = "INSERT INTO Task VALUES (?, ?, ?, ?)"

			// Create a PreparedStatement object with the insert SQL statement
			val preparedStatement = connection?.prepareStatement(sql)

			// Convert Dates to Strings
			val formattedCreateDateTime = task.createDateTime.format(formatter)
			val formattedEndDateTime = task.dueDateTime.format(formatter)

			// Add every property of the task to the database
			val properties = Task::class.declaredMemberProperties
			for ((i, property) in properties.withIndex()) {
				preparedStatement?.setString(i, property.get(task) as String)
			}

			// Execute the insert SQL statement
			val rowsAffected = preparedStatement?.executeUpdate()

			println("$rowsAffected row(s) inserted successfully.")
		} catch (e: SQLException) {
			println("Failed to insert task: ${e.message}")
		}
	}

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
				val createDateTime = LocalDateTime.parse(resultSet.getString("createDateTime"), formatter)
				val endDateTime = LocalDateTime.parse(resultSet.getString("endDateTime"), formatter)

				// Create a Task object and add it to the set
				ts.add(Task(name, taskList, createDateTime, endDateTime))
			}
		} catch (e: Exception) {
			throw e
		}
	}

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