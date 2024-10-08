package main.Database

import java.awt.Color

object ColorBuffer {
	val colors = HashMap<String, Color>()

	fun getListColor(listName: String): Color {
		if (colors.containsKey(listName)) return colors[listName]!!
		else {
			val newColor = Database.getListColor(listName)
			colors[listName] = newColor
			return newColor
		}
	}
}