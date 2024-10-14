package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

abstract class Menu {
	private val window = BasicWindow()
	val mainPanel: Panel = Panel()

	abstract fun createAndAddPanels(textGUI: MultiWindowTextGUI)

	fun show(textGUI: MultiWindowTextGUI) {
		if (window.component == null) {
			createAndAddPanels(textGUI)
		}
		window.setHints(listOf(Window.Hint.CENTERED))
		window.component = mainPanel.withBorder(Borders.doubleLine(" TUImorrow "))
		textGUI.addWindowAndWait(window)
	}

	protected fun createExitPanel(): Panel {
		val exitPanel = Panel()
		exitPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("    Exit    ", Runnable {
			window.close()
		}).addTo(exitPanel)
		return exitPanel
	}
}