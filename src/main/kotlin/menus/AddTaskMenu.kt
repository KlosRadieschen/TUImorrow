package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuItem
import main.database.ColorBuffer
import main.database.Database
import main.tasks.Task
import java.awt.Color
import java.time.LocalDate


/**
 * An implementation of `Menu` that provides functionality to add a new task.
 *
 * This object creates panels for input fields and buttons that allow the user
 * to enter task details such as name, list, and due date, and to add the task
 * to the application.
 */
object AddTaskMenu : main.menus.Menu() {
	/**
	 * Overrides the abstract method to create and add panels to the main application window.
	 *
	 * Adds an inputs panel with a single-line border labeled "Add task"
	 * and an exit panel with a single-line border to the main panel.
	 *
	 * @param textGUI The MultiWindowTextGUI object to which the panels will be added.
	 */
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createInputsPanel().withBorder(Borders.singleLine(" Add task ")))
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
		var listName = ""
		val newListInput = TextBox().setText("New List Name")

		inputsPanel.addComponent(Label("Name: "))
		val nameInput = TextBox().addTo(inputsPanel)

		inputsPanel.addComponent(EmptySpace(TerminalSize(0, 1)));

		if (ColorBuffer.colors.isNotEmpty()) {
			val listMenu = Menu("Select List")
			for (entry in ColorBuffer.colors) {
				listMenu.add(MenuItem(entry.key, Runnable {
					listName = entry.key
				}))
			}
			listMenu.add(MenuItem("New List Name", Runnable {
				inputsPanel.addComponent(4, newListInput)
				inputsPanel.addComponent(5, createListColorSelector())
			}))
			inputsPanel.addComponent(listMenu)
		} else {
			inputsPanel.addComponent(newListInput)
			inputsPanel.addComponent(createListColorSelector())
		}

		inputsPanel.addComponent(EmptySpace(TerminalSize(0, 1)));

		inputsPanel.addComponent(Label("Due Date: "))
		val yearBox = TextBox().setText(LocalDate.now().year.toString())
		val monthBox = TextBox().setText(LocalDate.now().monthValue.toString())
		val dayBox = TextBox()

		inputsPanel.addComponent(Label("Enter Year (YYYY):"))
		inputsPanel.addComponent(yearBox)
		inputsPanel.addComponent(Label("Enter Month (MM):"))
		inputsPanel.addComponent(monthBox)
		inputsPanel.addComponent(Label("Enter Day (DD):"))
		inputsPanel.addComponent(dayBox)

		inputsPanel.addComponent(EmptySpace(TerminalSize(0, 1)));

		Button("     Add     ", Runnable {
			val year = yearBox.text.toInt()
			val month = monthBox.text.toInt()
			val day = dayBox.text.toInt()

			if (newListInput.text != "New List") listName = newListInput.text

			Database.insertTask(Task(
				name = nameInput.text,
				listName = listName,
				dueDate = LocalDate.of(year, month, day),
			))
		}).addTo(inputsPanel)

		return inputsPanel
	}

	private fun createListColorSelector() : ComboBox<String> {
		val colorChooser = ComboBox<String>()
		for (color in ColorBuffer.COLORLIST) {
			colorChooser.addItem(ColorBuffer.getColorName(color))
		}
		return colorChooser
	}
}