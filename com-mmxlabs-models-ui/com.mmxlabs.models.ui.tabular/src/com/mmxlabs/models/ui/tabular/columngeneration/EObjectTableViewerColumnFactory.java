/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;

import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class EObjectTableViewerColumnFactory implements IColumnFactory {

	private final EObjectTableViewer viewer;

	public EObjectTableViewerColumnFactory(final EObjectTableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public GridViewerColumn createColumn(final ColumnHandler handler) {
		GridColumnGroup group = handler.block.getOrCreateColumnGroup(viewer.getGrid());

		final String title = handler.title;
		final ICellRenderer formatter = handler.getFormatter();
		final ICellManipulator manipulator = handler.getManipulator();
		final EMFPath[] paths = handler.getPaths();
		final ETypedElement[][] features = handler.getFeatures();
		final String tooltip = handler.getTooltip();

		final GridViewerColumn column = viewer.addColumn(title, group, formatter, manipulator, paths[0]);
		GridViewerHelper.configureLookAndFeel(column);

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
