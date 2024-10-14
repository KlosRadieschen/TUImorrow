package main

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import main.database.ColorBuffer
import main.database.Database
import main.menus.MainMenu

fun main() {
	Database.connect()
	Database.createTablesIfNotExists()
	ColorBuffer.refreshColors()

	// Setup terminal and screen layers
	val terminal = DefaultTerminalFactory().createTerminal()
	terminal.setCursorVisible(false);
	val screen: Screen = TerminalScreen(terminal)
	screen.startScreen()
	val textGUI = MultiWindowTextGUI(screen, DefaultWindowManager(), EmptySpace(TextColor.ANSI.MAGENTA_BRIGHT))

	MainMenu.show(textGUI)
}