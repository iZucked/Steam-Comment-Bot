/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.ui.tabular.renderers.CellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyCellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyRowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.RowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.TopLeftRenderer;

public final class GridViewerHelper {

	public static final int FLAGS_NONE = 0;
	public static final int FLAGS_ROW_HOVER = 1;

	public static void configureLookAndFeel(final @NonNull GridTableViewer viewer) {
		configureLookAndFeel(viewer, true);
	}

	public static void configureLookAndFeel(final @NonNull GridTableViewer viewer, boolean checkFocus) {
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());
		viewer.getGrid().setEmptyRowHeaderRenderer(new EmptyRowHeaderRenderer());
		viewer.getGrid().setEmptyCellRenderer(new EmptyCellRenderer(checkFocus));
	}

	public static void configureLookAndFeel(final @NonNull GridTreeViewer viewer) {
		configureLookAndFeel(viewer, true);
	}

	public static void configureLookAndFeel(final @NonNull GridTreeViewer viewer, boolean checkFocus) {
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());
		viewer.getGrid().setEmptyRowHeaderRenderer(new EmptyRowHeaderRenderer());
		viewer.getGrid().setEmptyCellRenderer(new EmptyCellRenderer(checkFocus));
	}

	public static void configureLookAndFeel(final @NonNull GridViewerColumn column) {
		column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		column.getColumn().setCellRenderer(new CellRenderer());
	}

	public static void configureLookAndFeel(final @NonNull GridViewerColumn column, int flags) {
		column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		if ((flags & FLAGS_ROW_HOVER) != 0) {
			column.getColumn().setCellRenderer(new CellRenderer() {
				@Override
				public boolean isDrawAsHover() {
					return isRowHover();
				}
			});
		} else {
			column.getColumn().setCellRenderer(new CellRenderer());
		}
	}

	public static void configureLookAndFeel(final @NonNull GridColumnGroup group) {
		group.setHeaderRenderer(new ColumnGroupHeaderRenderer());
	}

	public static void recalculateRowHeights(Grid grid) {
		GC gc = new GC(Display.getDefault());
		for (GridItem item : grid.getItems()) {
			int height = 1;
			for (GridColumn column : grid.getColumns()) {
				// GridColumn column = (GridColumn) columnsIterator.next();
				// column.getCellRenderer().setColumn(indexOf(column));
				height = Math.max(height, column.getCellRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT, item).y);
			}

			if (grid.isRowHeaderVisible() && grid.getRowHeaderRenderer() != null) {
				height = Math.max(height, grid.getRowHeaderRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT, item).y);
			}
			item.setHeight(height <= 0 ? 16 : height);
		}
		gc.dispose();
	}
}
