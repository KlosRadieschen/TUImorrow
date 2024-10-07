package main.Menus

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

object MainMenu : Menu() {
	override fun createAndAddPanels() {
		// Options panel and it's buttons
		val optionsPanel: Panel = Panel()
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button(" Show Tasks ", Runnable {
			TODO()
		}).addTo(optionsPanel)
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("  Add Task  ", Runnable {
			TODO()
		}).addTo(optionsPanel)

		// Exit panel and button
		val exitPanel: Panel = Panel()
		exitPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("    Exit    ", Runnable {
			window.close()
		}).addTo(exitPanel)

		// Add panels
		mainPanel.addComponent(optionsPanel.withBorder(Borders.singleLine(" Options ")))
		mainPanel.addComponent(exitPanel.withBorder(Borders.singleLine()))
	}
}