package main.menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

object MainMenu : Menu() {
	override fun createAndAddPanels(textGUI: MultiWindowTextGUI) {
		mainPanel.addComponent(createOptionsPanel(textGUI).withBorder(Borders.singleLine(" Options ")))
		mainPanel.addComponent(createExitPanel().withBorder(Borders.singleLine()))
	}

	private fun createOptionsPanel(textGUI: MultiWindowTextGUI): Panel {
		val optionsPanel = Panel()
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button(" Show Tasks ", Runnable {
			ListMenu.show(textGUI)
		}).addTo(optionsPanel)
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("  Add Task  ", Runnable {
			TODO()
		}).addTo(optionsPanel)
		return optionsPanel
	}
}