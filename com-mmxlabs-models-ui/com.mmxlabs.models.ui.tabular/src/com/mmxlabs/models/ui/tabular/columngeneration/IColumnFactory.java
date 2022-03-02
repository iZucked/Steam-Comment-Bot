/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

public interface IColumnFactory {
	GridViewerColumn createColumn(ColumnHandler handler);

	void destroy(GridViewerColumn column);
}
