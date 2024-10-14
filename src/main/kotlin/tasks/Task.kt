package main.tasks

import java.time.LocalDate

/**
 * Represents a task with a name, associated list name, due date and time,
 * and the creation date and time.
 *
 * @property name The name of the task.
 * @property listName The name of the list to which the task belongs.
 * @property dueDate The due date and time for the task.
 * @property createDate The creation date and time for the task. Default value is the current time.
 */
data class Task (
	var name: String,
	var listName: String,
	var dueDate: LocalDate,
	val createDate: LocalDate = LocalDate.now()  // Default value set to current time
)