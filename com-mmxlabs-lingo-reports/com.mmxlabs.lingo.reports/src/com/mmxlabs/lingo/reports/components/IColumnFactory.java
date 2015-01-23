/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

public interface IColumnFactory {
	GridViewerColumn createColumn(ColumnHandler handler);

	void destroy(GridViewerColumn column);
}
