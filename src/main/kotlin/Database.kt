package main

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
	}

	fun connect() {
		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC")

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
			val formattedEndDateTime = task.endDateTime.format(formatter)

			// Add every property of the task to the database
			val properties = Task::class.declaredMemberProperties
			var i = 0
			for (property in properties) {
				preparedStatement?.setString(i, property.get(task) as String)
				i++
			}

			// Execute the insert SQL statement
			val rowsAffected = preparedStatement?.executeUpdate()

			println("$rowsAffected row(s) inserted successfully.")
		} catch (e: SQLException) {
			println("Failed to insert task: ${e.message}")
		}
	}

	// Get all tasks from the database and put them into al. Setting list to null retrieves all tasks
	fun getTasks(al: ArrayList<Task>, list: String?) {
		try {
			al.clear()
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

				// Create a Task object and add it to the list
				val task = Task(name, taskList, createDateTime, endDateTime)
				al.add(task)
			}
		} catch (e: Exception) {
			e.printStackTrace() // Print the error
		}
	}
}