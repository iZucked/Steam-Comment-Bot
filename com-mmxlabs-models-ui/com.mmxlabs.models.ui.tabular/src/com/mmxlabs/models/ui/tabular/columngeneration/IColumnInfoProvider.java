/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

/**
 * Update various aspects of a columns from a viewer such {@link TableViewer}
 */
public interface IColumnInfoProvider {

	/**
	 * Get corresponding index for the column
	 * 
	 * @param columnObj
	 */
	int getColumnIndex(ColumnBlock columnObj);

	/**
	 * Get the width of the column
	 * 
	 * @param columnObj
	 */
	default int getColumnWidth(ColumnBlock columnObj) {
		return 0;
	}

	/**
	 * Returns true if the column represented by parameters is showing in the viewer
	 * 
	 * @param columnObj
	 */
	boolean isColumnVisible(ColumnBlock columnObj);

	/**
	 * Returns true if the column represented by parameters is configured as movable
	 * 
	 * @param columnObj
	 */
	default boolean isColumnMovable(ColumnBlock columnObj) {
		return true;
	}

	/**
	 * Returns true if the column represented by parameters is configured as
	 * resizable
	 * 
	 * @param columnObj
	 */
	default boolean isColumnResizable(ColumnBlock columnObj) {
		return false;
	}

}
