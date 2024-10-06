package main

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.Window.Hint
import java.util.*


object MainMenu {
	fun show(textGUI: MultiWindowTextGUI) {
		val window = BasicWindow()
		window.setHints(listOf(Hint.CENTERED))

		val mainPanel: Panel = Panel()

		val optionsPanel: Panel = Panel()
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button(" Show Tasks ", Runnable {
			TODO()
		}).addTo(optionsPanel)
		optionsPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button(" Add Task ", Runnable {
			TODO()
		}).addTo(optionsPanel)

		val exitPanel: Panel = Panel()
		exitPanel.addComponent(EmptySpace(TerminalSize(0, 0)))
		Button("    Exit    ", Runnable {
			TODO()
		}).addTo(exitPanel)

		mainPanel.addComponent(optionsPanel.withBorder(Borders.singleLine(" Options ")))
		mainPanel.addComponent(exitPanel.withBorder(Borders.singleLine()))

		window.component = mainPanel.withBorder(Borders.doubleLine(" TUImorrow "))
		textGUI.addWindowAndWait(window)
	}
}