package main

import java.time.LocalDateTime

class Task {
	var name: String
	var endDateTime: LocalDateTime
	private val createDateTime: LocalDateTime

	constructor(name: String, endDateTime: LocalDateTime) {
		this.name = name
		this.endDateTime = endDateTime
		this.createDateTime = LocalDateTime.now()!!
	}
}