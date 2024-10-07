package main.Menus

import com.googlecode.lanterna.gui2.*

abstract class Menu {
	val window = BasicWindow()
	val mainPanel: Panel = Panel()

	abstract fun createAndAddPanels()

	fun show(textGUI: MultiWindowTextGUI) {
		window.setHints(listOf(Window.Hint.CENTERED))
		window.component = mainPanel.withBorder(Borders.doubleLine(" TUImorrow "))
		textGUI.addWindowAndWait(window)
	}
}