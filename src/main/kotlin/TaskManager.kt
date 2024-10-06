package main

class TaskManager {
	private val tasks = ArrayList<Task>()

	fun retrieveList(list: String) {
		Database.getTasks(tasks, list)
	}
}