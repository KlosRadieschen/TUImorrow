package main

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.Window.Hint
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.Terminal

object MainMenu {
	fun show(textGUI: MultiWindowTextGUI, screen: Screen, terminal: Terminal) {
		// Create window and main Panel
		val window = BasicWindow()
		window.setHints(listOf(Hint.CENTERED))
		val mainPanel: Panel = Panel()



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
			TODO()
		}).addTo(exitPanel)



		// add sub-panels to the main panel, then display window
		mainPanel.addComponent(optionsPanel.withBorder(Borders.singleLine(" Options ")))
		mainPanel.addComponent(exitPanel.withBorder(Borders.singleLine()))
		window.component = mainPanel.withBorder(Borders.doubleLine(" TUImorrow "))
		textGUI.addWindowAndWait(window)
	}
}