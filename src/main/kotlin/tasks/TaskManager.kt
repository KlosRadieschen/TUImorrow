package main.tasks

import main.database.Database
import java.awt.Color
import java.util.TreeSet

object TaskManager {
	val tasks = TreeSet<Task>(compareBy<Task> { it.dueDateTime }.thenBy { it.name })
	var currentList: String? = null

	fun retrieveTasks() {
		Database.getTasks(tasks, currentList)
	}
}