package main

import java.awt.Color
import java.util.TreeSet

class TaskManager {
	private var tasks = TreeSet<Task>()

	fun retrieveList(list: String, listColor: Color) {
		tasks = Database.getTasks(list)
	}
}