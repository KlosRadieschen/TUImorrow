package main.tasks

import main.database.Database
import java.awt.Color
import java.util.TreeSet

/**
 * Object responsible for managing tasks.
 *
 * @property tasks A sorted set of tasks, ordered by due date and time, and then by name.
 * @property currentList The current list name being managed. Can be null.
 */
object TaskManager {
	/**
	 * A sorted set of tasks, ordered primarily by their due date and time, and secondarily by their name.
	 * Each task in the set is an instance of the `Task` class.
	 */
	val tasks = TreeSet<Task>(compareBy<Task> { it.dueDateTime }.thenBy { it.name })
	/**
	 * Represents the current task list being used or viewed within the application.
	 * This variable stores the name of the current list as a nullable string.
	 * If the value is null, it implies that no specific list is selected and all tasks are to be retrieved.
	 */
	var currentList: String? = null

	/**
	 * Retrieves tasks from the database and updates the current list of tasks.
	 *
	 * This function fetches all tasks associated with `currentList` from the database.
	 * The retrieved tasks are stored in the `tasks` set, replacing its previous contents.
	 */
	fun retrieveTasks() {
		Database.getTasks(tasks, currentList)
	}
}