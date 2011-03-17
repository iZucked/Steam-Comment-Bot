/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

/**
 * @author hinton
 *
 */
public interface ICellRenderer {
	/**
	 * Render the given object for viewing in a table cell.
	 * @param object
	 * @return string rep. of object
	 */
	String render(Object object);
}
