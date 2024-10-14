package main.menus

import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.menu.MenuItem
import main.database.ColorBuffer

object AddTaskMenu : Menu() {
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createInputsPanel().withBorder(Borders.singleLine("Add task")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

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