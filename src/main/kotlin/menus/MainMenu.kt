package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

/**
 * Represents the main menu of the TUI application.
 *
 * This object extends the abstract `Menu` class and provides implementation
 * for creating and adding panels specific to the main menu.
 */
object MainMenu : Menu() {
	/**
	 * Creates and adds the necessary panels to the main application window.
	 *
	 * @param textGUI The `MultiWindowTextGUI` instance where the panels will be added.
	 */
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createOptionsPanel(textGUI).withBorder(Borders.singleLine(" Options ")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

	/**
	 * Creates an options panel containing buttons to show tasks and add a new task.
	 *
	 * @param textGUI The MultiWindowTextGUI instance where the panel will be displayed.
	 * @return The panel containing the options buttons.
	 */
	private fun createOptionsPanel(textGUI: MultiWindowTextGUI): Panel {
		val optionsPanel = Panel()
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button(" Show Tasks ", Runnable {
			ListMenu.show(textGUI)
		}).addTo(optionsPanel)
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("  Add Task  ", Runnable {
			AddTaskMenu.show(textGUI)
		}).addTo(optionsPanel)
		return optionsPanel
	}
}