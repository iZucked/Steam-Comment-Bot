package com.mmxlabs.rcp.common.actions;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;

public interface IAdditionalAttributeProvider {
	/**
	 * Returns additional header style attributes for the grid column
	 * 
	 * @param column
	 * @return
	 */
	@Nullable
	String[] getAdditionalHeaderAttributes(final GridColumn column);

	/**
	 * Returns additional style attributes for the grid item at the specified column index.
	 * 
	 * @param item
	 * @param columnIdx
	 * @return
	 */
	@Nullable
	String[] getAdditionalAttributes(final GridItem item, final int columnIdx);
}
