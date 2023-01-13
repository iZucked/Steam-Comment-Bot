/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

/**
 * Update various aspects of a columns from a viewer such {@link TableViewer}
 */
public interface ICustomColumnUpdater {

	/**
	 * Set the column represented by parameters as visible
	 * 
	 * @param columnObj
	 * @param visible
	 */
	void setColumnVisible(String columnObj, boolean visible);

	/**
	 * Dummy method - more a result of symmetry
	 * 
	 * @param columnObj
	 * @param movable
	 */
	void setColumnMovable(String columnObj, boolean movable);

	/**
	 * Call back to notify change in the index of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param index
	 */
	void setColumnIndex(String columnObj, int index);

	/**
	 * Call back to notify change in the index of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param index
	 */
	void swapColumnPositions(String columnObj1, String columnObj2);

	/**
	 * Dummy method - more a result of symmetry
	 * 
	 * @param columnObj
	 * @param resizable
	 */
	void setColumnResizable(String columnObj, boolean resizable);

	/**
	 * Call back to notify change in the width of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param newWidth
	 */
	public void setColumnWidth(String columnObj, int newWidth);

	String[] resetColumnStates();

}
