/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jdt.annotation.NonNull;
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
	@NonNull
	String @Nullable [] getAdditionalHeaderAttributes(final GridColumn column);

	/**
	 * Returns additional style attributes for the grid item at the specified column index.
	 * 
	 * @param item
	 * @param columnIdx
	 * @return
	 */
	@NonNull
	String @Nullable [] getAdditionalAttributes(@NonNull GridItem item, final int columnIdx);

	@NonNull
	String @Nullable [] getAdditionalRowHeaderAttributes(@NonNull GridItem item);

	@NonNull
	String getTopLeftCellLowerText();

	@NonNull
	String getTopLeftCellUpperText();

	@NonNull
	String getTopLeftCellText();

	@NonNull
	String @Nullable [] getAdditionalPreRows();

	default void begin() {
	}

	default void done() {
	}
}
