/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to encapsulate information about an EMF report column, allowing the same column to be added to multiple different reports in a class of similar reports.
 * 
 * EmfBlockColumnFactory objects are used by the EMFReportColumnManager class.
 * 
 * Managed columns are assigned to particular column "blocks" which can be selectively hidden, displayed, or reordered by the user.
 * 
 * TODO: refactor all EMFReportView#addColumn calls through this class, so that column logic is uniform.
 */
public interface EmfBlockColumnFactory {

	ColumnHandler addColumn(final ColumnBlockManager blockManager);

	default List<ColumnHandler> addColumns(final ColumnBlockManager blockManager) {
		final List<ColumnHandler> result = new ArrayList<>();
		result.add(addColumn(blockManager));
		return result;
	}
}
