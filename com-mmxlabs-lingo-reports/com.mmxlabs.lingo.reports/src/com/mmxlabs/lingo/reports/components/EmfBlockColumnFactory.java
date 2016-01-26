/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

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
public abstract class EmfBlockColumnFactory {
	public abstract ColumnHandler addColumn(final ColumnBlockManager blockManager);

//	public abstract ColumnHandler addColumn(AbstractConfigurableGridReportView report);

	public List<ColumnHandler> addColumns(final ColumnBlockManager blockManager) {
		final List<ColumnHandler> result = new ArrayList<>();
		result.add(addColumn(blockManager));
		return result;
	}

}
