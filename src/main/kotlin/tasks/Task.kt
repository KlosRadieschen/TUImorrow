package main.tasks

import java.time.LocalDateTime

/**
 * Represents a task with a name, associated list name, due date and time,
 * and the creation date and time.
 *
 * @property name The name of the task.
 * @property listName The name of the list to which the task belongs.
 * @property dueDateTime The due date and time for the task.
 * @property createDateTime The creation date and time for the task. Default value is the current time.
 */
data class Task (
	var name: String,
	var listName: String,
	var dueDateTime: LocalDateTime,
	val createDateTime: LocalDateTime = LocalDateTime.now()  // Default value set to current time
)