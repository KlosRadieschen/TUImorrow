package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuItem
import main.database.ColorBuffer
import main.tasks.TaskManager


object ListMenu : main.menus.Menu() {
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createListSelectorPanel().withBorder(Borders.singleLine("Options")))
		mainPanel.addComponent(createTasksPanel().withBorder(Borders.singleLine("Tasks")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

	private fun createListSelectorPanel(): Panel {
		val listSelectorPanel = Panel()
		val listMenu = Menu("Choose List")
		for (entry in ColorBuffer.colors) {
			val listMenuEntry = MenuItem(entry.key, Runnable {
				TaskManager.currentList = entry.key
			})
			listMenu.add(listMenuEntry)
		}
		listSelectorPanel.addComponent(listMenu)
		return listSelectorPanel
	}

	private fun createTasksPanel(): Panel {
		val tasksPanel = Panel()
		TaskManager.retrieveTasks()
		val size = TerminalSize(14, 10)
		val checkBoxList = CheckBoxList<String>(size)
		for (task in TaskManager.tasks) {
			checkBoxList.addItem(task.name)
		}
		tasksPanel.addComponent(checkBoxList)
		return tasksPanel
	}
}