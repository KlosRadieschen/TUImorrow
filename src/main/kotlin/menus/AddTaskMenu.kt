package main.menus

import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.menu.MenuItem
import main.database.ColorBuffer

/**
 * An implementation of `Menu` that provides functionality to add a new task.
 *
 * This object creates panels for input fields and buttons that allow the user
 * to enter task details such as name, list, and due date, and to add the task
 * to the application.
 */
object AddTaskMenu : Menu() {
	/**
	 * Overrides the abstract method to create and add panels to the main application window.
	 *
	 * Adds an inputs panel with a single-line border labeled "Add task"
	 * and an exit panel with a single-line border to the main panel.
	 *
	 * @param textGUI The MultiWindowTextGUI object to which the panels will be added.
	 */
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createInputsPanel().withBorder(Borders.singleLine("Add task")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

	/**
	 * Creates and configures an input panel for adding a task.
	 * The panel includes input fields for the task name, a combo box for selecting a list,
	 * and a due date input field, along with an Add button.
	 *
	 * @return The fully configured input panel.
	 */
	private fun createInputsPanel(): Panel {
		val inputsPanel = Panel()
		inputsPanel.addComponent(Label("Name: "))
		TextBox().addTo(inputsPanel)

		val listMenu = ComboBox<String>("List")
		for (entry in ColorBuffer.colors) listMenu.addItem(entry.key)
		listMenu.addItem("New List")
		inputsPanel.addComponent(listMenu)

		inputsPanel.addComponent(Label("Due Date: "))
		TextBox().addTo(inputsPanel)

		Button("Add").addTo(inputsPanel)

		return inputsPanel
	}
}