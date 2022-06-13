package com.mmxlabs.lingo.reports.utils;

import org.eclipse.jface.viewers.TableViewer;

import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;

/**
 * Update various aspects of a columns from a viewer such {@link TableViewer}
 */
public interface IColumnUpdater {

	/**
	 * Set the column represented by parameters as visible
	 * 
	 * @param columnObj
	 * @param visible
	 */
	void setColumnVisible(ColumnBlock columnObj, boolean visible);

	/**
	 * Dummy method - more a result of symmetry
	 * 
	 * @param columnObj
	 * @param movable
	 */
	default void setColumnMovable(ColumnBlock columnObj, boolean movable) {

	}

	/**
	 * Call back to notify change in the index of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param index
	 */
	default void setColumnIndex(ColumnBlock columnObj, int index) {

	}

	/**
	 * Call back to notify change in the index of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param index
	 */
	void swapColumnPositions(ColumnBlock columnObj1, ColumnBlock columnObj2);

	/**
	 * Dummy method - more a result of symmetry
	 * 
	 * @param columnObj
	 * @param resizable
	 */
	default void setColumnResizable(ColumnBlock columnObj, boolean resizable) {

	}

	/**
	 * Call back to notify change in the width of the column represented by
	 * columnObj
	 * 
	 * @param columnObj
	 * @param newWidth
	 */
	default void setColumnWidth(ColumnBlock columnObj, int newWidth) {

	}

	ColumnBlock[] resetColumnStates();

}
