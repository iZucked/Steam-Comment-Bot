/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.events.TreeListener;

import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class ColumnHandler {

	public static final String COLUMN_HANDLER = "COLUMN_HANDLER";

	private final ICellRenderer formatter;
	private final ICellManipulator manipulator;
	private final EMFPath[] path;
	public final String title;
	private String tooltip;
	public GridViewerColumn column;
	public int viewIndex;
	public final ColumnBlock block;
	private final ETypedElement[][] features;
	private IColumnFactory columnFactory;
	private @Nullable Predicate<Object> rowFilter;
	public boolean detailColumn = true;
	public boolean summaryColumn = true;
	public EMFMultiPath multiPathForSorting;
	public Function<GridColumnGroup, TreeListener> treeListener;

	public ColumnHandler(final ColumnBlock block, @Nullable Predicate<Object> rowFilter, final ICellRenderer formatter, final @Nullable ICellManipulator manipulator, final ETypedElement[] features,
			final String title, final IColumnFactory columnFactory) {
		super();
		this.rowFilter = rowFilter;
		this.formatter = formatter;
		this.manipulator = manipulator;
		this.columnFactory = columnFactory;
		this.features = new ETypedElement[][] { features };
		this.path = new EMFPath[] { new EMFPath(true, features) };

		this.title = title;
		this.block = block;
	}

	public ColumnHandler(final ColumnBlock block, @Nullable Predicate<Object> rowFilter, final ICellRenderer formatter, final @Nullable ICellManipulator manipulator, final ETypedElement[][] features,
			final String title, final IColumnFactory columnFactory) {
		super();
		this.rowFilter = rowFilter;
		this.formatter = formatter;
		this.manipulator = manipulator;
		this.features = features;
		this.columnFactory = columnFactory;
		this.path = new EMFPath[features.length];
		for (int i = 0; i < features.length; ++i) {
			getPaths()[i] = new EMFPath(true, features[i]);
			// getPaths()[i] = new CompiledEMFPath(getClass().getClassLoader(), true, features[i]);
		}

		this.title = title;
		this.block = block;
	}

	public GridViewerColumn createColumn() {
		if (columnFactory == null) {
			return null;
		}
		final GridViewerColumn column = columnFactory.createColumn(this);
		final GridColumn tc = column.getColumn();

		tc.setDetail(detailColumn);
		tc.setSummary(summaryColumn);

		tc.setData(COLUMN_HANDLER, this);

		if (multiPathForSorting != null) {
			tc.setData(EObjectTableViewer.COLUMN_SORT_PATH, multiPathForSorting);
		}

		
		if (treeListener != null) {
			tc.getColumnGroup().addTreeListener(treeListener.apply(tc.getColumnGroup()));
		}
		
		this.column = column;

		return column;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		if (column != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}
	}

	public ICellRenderer getFormatter() {
		return formatter;
	}

	public ICellManipulator getManipulator() {
		if (manipulator != null) {
			return manipulator;
		} else {
			return NoEditingCellManipulator.INSTANCE;
		}
	}

	public EMFPath[] getPaths() {
		return path;
	}

	public ETypedElement[][] getFeatures() {
		return features;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void destroy() {
		columnFactory.destroy(column);
		column = null;
	}

	public void setColumnFactory(IColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public boolean includeRow(Object element) {
		if (rowFilter != null) {
			return rowFilter.test(element);
		}
		return true;
	}
}