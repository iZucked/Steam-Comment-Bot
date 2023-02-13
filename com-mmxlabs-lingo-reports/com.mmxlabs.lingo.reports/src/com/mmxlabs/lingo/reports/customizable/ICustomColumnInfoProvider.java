/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

public interface ICustomColumnInfoProvider {

	/**
	 * Get corresponding index for the column
	 * 
	 * @param columnObj
	 */
	int getColumnIndex(String blockID);

	/**
	 * Get the width of the column
	 * 
	 * @param columnObj
	 */
	default int getColumnWidth(String blockID) {
		return 0;
	}

	/**
	 * Returns true if the column represented by parameters is showing in the viewer
	 * 
	 * @param columnObj
	 */
	default boolean isColumnVisible(String blockID) {
		return true;
	}

	/**
	 * Returns true if the column represented by parameters is configured as movable
	 * 
	 * @param columnObj
	 */
	default boolean isColumnMovable(String blockID) {
		return true;
	}

	/**
	 * Returns true if the column represented by parameters is configured as
	 * resizable
	 * 
	 * @param columnObj
	 */
	default boolean isColumnResizable(String blockID) {
		return false;
	}

}
