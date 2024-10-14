package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuItem
import main.database.ColorBuffer
import main.tasks.TaskManager


/**
 * Represents a menu for selecting and interacting with task lists within the Text UI application.
 */
object ListMenu : main.menus.Menu() {
	/**
	 * Creates and adds panels to the main application window.
	 *
	 * @param textGUI The MultiWindowTextGUI object to which the panels will be added.
	 */
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createListSelectorPanel().withBorder(Borders.singleLine("Options")))
		mainPanel.addComponent(createTasksPanel().withBorder(Borders.singleLine("Tasks")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

	/**
	 * Creates a panel that contains a menu for selecting a list.
	 * The menu is populated with entries from the ColorBuffer.colors collection.
	 * When a menu item is selected, it updates the TaskManager's current list.
	 *
	 * @return The panel containing the list selection menu.
	 */
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

	/**
	 * Creates a panel containing a list of tasks.
	 *
	 * Retrieves the tasks using the TaskManager, adds them to a CheckBoxList, and
	 * then adds the CheckBoxList to the panel. The tasks are displayed in a panel
	 * of fixed size.
	 *
	 * @return The panel containing the list of tasks.
	 */
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