package main.Tasks

import java.time.LocalDateTime

data class Task (
	var name: String,
	var listName: String,
	var dueDateTime: LocalDateTime,
	val createDateTime: LocalDateTime = LocalDateTime.now()  // Default value set to current time
)