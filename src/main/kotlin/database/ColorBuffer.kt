package main.database

import java.awt.Color

/**
 * Singleton object that manages a buffer of colors associated with different lists.
 *
 * This object interacts with a 'Database' to retrieve and cache color information to minimize database queries.
 */
object ColorBuffer {
	/**
	 * A map that stores color associations for different list names.
	 * The key is the name of the list, and the value is the corresponding color object.
	 * This map is typically populated from the database and is used to retrieve colors
	 * for different lists in the application.
	 */
	val colors = HashMap<String, Color>()

	/**
	 * Retrieves the color associated with the specified list name.
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
	 * Refreshes the color data by retrieving all list colors from the database.
	 *
	 * Uses the `Database.getAllListColors` method to populate the `colors` map with the list names as keys and
	 * the corresponding Color objects as values.
	 */
	fun refreshColors() {
		Database.getAllListColors(colors)
	}
}