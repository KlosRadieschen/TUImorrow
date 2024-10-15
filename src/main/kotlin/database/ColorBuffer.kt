package main.database

import java.awt.Color

/**
 * ColorBuffer is an object that manages a buffer of colors.
 * It provides functionalities to retrieve color names, get colors
 * associated with a list name, and refresh the entire buffer
 * of colors from a database.
 */
object ColorBuffer {
	/**
	 * A map that holds color values indexed by their names.
	 *
	 * This HashMap stores color values of type [Color] with their corresponding
	 * string names as keys. It can be used to look up colors by their names
	 * for various purposes, such as UI theming or graphics rendering.
	 */
	val colors = HashMap<String, Color>()
	/**
	 * An array of predefined `Color` objects representing a basic color palette.
	 * It contains the following colors: `Color.RED`, `Color.GREEN`, `Color.BLUE`,
	 * `Color.YELLOW`, `Color.WHITE`, `Color.PINK`, and `Color.ORANGE`.
	 */
	val COLORLIST = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE, Color.PINK, Color.ORANGE)

	/**
	 * Retrieves the name of the specified color using reflection.
	 *
	 * @param color The color for which the name is to be retrieved.
	 * @return The name of the color, or "Unknown Color" if the name could not be determined.
	 */
	fun getColorName(color: Color): String {
		// Use reflection to find the name of the Color
		val colorClass = Color::class.java
		for (field in colorClass.fields) {
			if (field.type == Color::class.java) {
				try {
					if (color == field.get(null)) {
						return field.name
					}
				} catch (e: IllegalAccessException) {
					e.printStackTrace()
				}
			}
		}
		return "Unknown Color"
	}


	/**
	 * Retrieves the color associated with the specified list name, either from the local cache
	 * or by querying the database if the color is not already cached.
	 *
	 * @param listName The name of the list for which the color is to be retrieved.
	 * @return The color associated with the specified list name.
	 * @throws NoSuchElementException If no list with the specified name is found.
	 * @throws SQLException If there is an error while accessing the database.
	 */
	fun getListColor(listName: String): Color {
		if (colors.containsKey(listName)) return colors[listName]!!
		else {
			val newColor = Database.getListColor(listName)
			colors[listName] = newColor
			return newColor
		}
	}

	/**
	 * Refreshes the colors by retrieving all colors from the database.
	 *
	 * The method queries the database to get the latest list of colors and updates
	 * the internal list of colors accordingly. This function does not take any
	 * parameters and does not return any value.
	 *
	 * Note: Ensure that the database connection is properly established before
	 * calling this method to prevent potential errors or exceptions.
	 */
	fun refreshColors() {
		Database.getAllListColors(colors)
	}
}