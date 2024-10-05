package main

import java.time.LocalDateTime

data class Task(
	var name: String,
	var list: String,
	var endDateTime: LocalDateTime,
	val createDateTime: LocalDateTime = LocalDateTime.now()  // Default value set to current time
)