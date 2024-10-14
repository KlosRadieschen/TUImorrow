package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

/**
 * Abstract class representing a menu in the TUI application.
 */
abstract class Menu {
	/**
	 * Represents a basic window that is utilized within the Menu class to display UI panels.
	 * The window is initialized as an instance of BasicWindow.
	 * It is primarily used to set up and show main UI components and exit panels.
	 */
	private val window = BasicWindow()
	/**
	 * Main panel of the application's user interface.
	 *
	 * This panel serves as the primary container for various sub-panels
	 * used throughout the application. It is dynamically populated with
	 * different components and panels by the `createAndAddPanels` method.
	 *
	 * The `mainPanel` is typically displayed within a window, and may
	 * include elements such as input fields, buttons, and selection lists,
	 * depending on the current state of the application.
	 *
	 * It is bordered and centered when displayed via the `show` method.
	 */
	val mainPanel: Panel = Panel()

	/**
	 * Abstract method to create and add panels to the main application window.
	 *
	 * @param textGUI The MultiWindowTextGUI object to which the panels will be added.
	 */
	abstract fun createAndAddPanels(textGUI: MultiWindowTextGUI)

	/**
	 * Displays the main menu window in the provided `textGUI`.
	 *
	 * If the window's component is `null`, it creates and adds the necessary panels via the `createAndAddPanels` method.
	 * Sets the window to be centered with a double-line border labeled "TUImorrow".
	 *
	 * @param textGUI The `MultiWindowTextGUI` instance where the window will be displayed.
	 */
	fun show(textGUI: MultiWindowTextGUI) {
		if (window.component == null) {
			createAndAddPanels(textGUI)
		}
		window.setHints(listOf(Window.Hint.CENTERED))
		window.component = mainPanel.withBorder(Borders.doubleLine(" TUImorrow "))
		textGUI.addWindowAndWait(window)
	}

	/**
	 * Creates a panel with an Exit button that, when pressed, closes the current window.
	 *
	 * @return The panel containing the Exit button.
	 */
	protected fun createExitPanel(): Panel {
		val exitPanel = Panel()
		exitPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("    Exit    ", Runnable {
			window.close()
		}).addTo(exitPanel)
		return exitPanel
	}
}