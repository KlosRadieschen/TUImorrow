package main

class TaskManager {
	private val tasks = ArrayList<Task>()

	fun retrieveList(list: String) {
		for (task in tasks) if (task.list == list) Database.insertTask(task)
	}
}