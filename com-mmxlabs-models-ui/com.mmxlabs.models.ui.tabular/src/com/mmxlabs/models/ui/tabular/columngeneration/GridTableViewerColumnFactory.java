/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;

import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class GridTableViewerColumnFactory implements IColumnFactory {

	private final GridTableViewer viewer;
	private final EObjectTableViewerSortingSupport sortingSupport;
	private final EObjectTableViewerFilterSupport filterSupport;
	

	public GridTableViewerColumnFactory(final GridTableViewer viewer, final EObjectTableViewerSortingSupport sortingSupport, final EObjectTableViewerFilterSupport filterSupport) {
		this.viewer = viewer;
		this.sortingSupport = sortingSupport;
		this.filterSupport = filterSupport;
	}

	@Override
	public GridViewerColumn createColumn(final ColumnHandler handler) {
		GridColumnGroup group = handler.block.getOrCreateColumnGroup(viewer.getGrid());

		final String title = handler.title;
		final ICellRenderer formatter = handler.getFormatter();
		final EMFPath[] path = handler.getPaths();
		final ETypedElement[][] features = handler.getFeatures();
		final String tooltip = handler.getTooltip();

		final GridColumn col;
		if (group != null) {
			col = new GridColumn(group, SWT.NONE);
		} else {
			col = new GridColumn(viewer.getGrid(), SWT.NONE);
		}
		final GridViewerColumn column = new GridViewerColumn(viewer, col);
		column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		column.getColumn().setText(title);
		column.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, formatter);

		// Set a default label provider
		column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {

				Object element = cell.getElement();
				if (element != null) {
					boolean found = false;
					for (final EMFPath p : path) {
						final Object x = p.get((EObject) element);
						if (x != null) {
							element = x;
							found = true;
							break;
						}
					}
					if (!found) {
						element = null;
					}
				}

				if (col.isVisible()) {
					setRowSpan(formatter, cell, element);
				}
				cell.setText(formatter.render(element));
				if (formatter instanceof IImageProvider) {
					cell.setImage(((IImageProvider) formatter).getImage(cell.getElement()));
				}
			}
		});

		final GridColumn tc = column.getColumn();

		tc.setData(ColumnHandler.COLUMN_HANDLER, this);
		tc.setData(EObjectTableViewer.COLUMN_PATH, path);
		tc.setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, formatter);

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		if (filterSupport != null) {
			filterSupport.createColumnMnemonics(tc, title);
		}
		if (sortingSupport != null) {
			sortingSupport.addSortableColumn(viewer, column, tc);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	@Override
	public void destroy(final GridViewerColumn gvc) {
		// filterSupport.removeColum(column);

		if (gvc != null) {
			if (sortingSupport != null) {
				sortingSupport.removeSortableColumn(gvc.getColumn());
			}
			gvc.getColumn().dispose();
		}
	}

	private void setRowSpan(final ICellRenderer formatter, final ViewerCell cell, Object element) {
		if (formatter instanceof IRowSpanProvider) {
			final IRowSpanProvider rowSpanProvider = (IRowSpanProvider) formatter;
			final DataVisualizer dv = viewer.getGrid().getDataVisualizer();
			// final GridItem item = (GridItem) cell.getItem();
			int rowSpan = rowSpanProvider.getRowSpan(cell, element);
			{
				ViewerCell c = cell;
				for (int i = 0; i < rowSpan; i++) {
					dv.setRowSpan((GridItem) c.getItem(), cell.getColumnIndex(), i);
					c = c.getNeighbor(ViewerCell.ABOVE, false);
					// Set row span each time in case the first cell in the range is not visible
				}
				dv.setRowSpan((GridItem) c.getItem(), cell.getColumnIndex(), rowSpan);
			}
		}
	}

}
