/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class EObjectTableViewerColumnFactory implements IColumnFactory {

	private final EObjectTableViewer viewer;

	public EObjectTableViewerColumnFactory(final EObjectTableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public GridViewerColumn createColumn(final ColumnHandler handler) {

		final String title = handler.title;
		final ICellRenderer formatter = handler.getFormatter();
		final EMFPath[] paths = handler.getPaths();
		final ETypedElement[][] features = handler.getFeatures();
		final String tooltip = handler.getTooltip();

		final GridViewerColumn column = viewer.addColumn(title, formatter, NoEditingCellManipulator.INSTANCE, paths[0]);

		final GridColumn tc = column.getColumn();

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	@Override
	public void destroy(final GridViewerColumn column) {
		viewer.removeColumn(column);
	}
}
