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
	public int getColumnIndex(Object columnObj);

	/**
	 * Get the width of the column
	 * 
	 * @param columnObj
	 */
	public int getColumnWidth(Object columnObj);

	/**
	 * Returns true if the column represented by parameters is showing in the viewer
	 * 
	 * @param columnObj
	 */
	public boolean isColumnVisible(Object columnObj);

	/**
	 * Returns true if the column represented by parameters is configured as movable
	 * 
	 * @param columnObj
	 */
	public boolean isColumnMovable(Object columnObj);

	/**
	 * Returns true if the column represented by parameters is configured as resizable
	 * 
	 * @param columnObj
	 */
	public boolean isColumnResizable(Object columnObj);

}
