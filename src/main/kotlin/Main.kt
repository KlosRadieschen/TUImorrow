package main

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory

fun main() {
	Database.createDBIfNotExists()
	Database.connect()

	// Setup terminal and screen layers
	val terminal = DefaultTerminalFactory().createTerminal()
	terminal.setCursorVisible(false);
	val screen: Screen = TerminalScreen(terminal)
	screen.startScreen()
	val textGUI = MultiWindowTextGUI(screen, DefaultWindowManager(), EmptySpace(TextColor.ANSI.BLUE))

	MainMenu.show(textGUI)
}