/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.CompiledEMFPath;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class ColumnHandler {
	/**
	 * 
	 */
	public static final String COLUMN_HANDLER = "COLUMN_HANDLER";
	private final ICellRenderer formatter;
	private final EMFPath[] path;
	public final String title;
	private String tooltip;
	public GridViewerColumn column;
	public int viewIndex;
	public final ColumnBlock block;
	private final ETypedElement[][] features;
	private IColumnFactory columnFactory;

	public ColumnHandler(final ColumnBlock block, final ICellRenderer formatter, final ETypedElement[] features, final String title, final IColumnFactory columnFactory) {
		super();
		this.formatter = formatter;
		this.columnFactory = columnFactory;
		this.features = new ETypedElement[][] { features };
		this.path = new EMFPath[] { new CompiledEMFPath(getClass().getClassLoader(), true, features) };

		this.title = title;
		this.block = block;
	}

	public ColumnHandler(final ColumnBlock block, final ICellRenderer formatter, final ETypedElement[][] features, final String title, final IColumnFactory columnFactory) {
		super();
		this.formatter = formatter;
		this.features = features;
		this.columnFactory = columnFactory;
		this.path = new EMFPath[features.length];
		for (int i = 0; i < features.length; ++i) {
			getPaths()[i] = new CompiledEMFPath(getClass().getClassLoader(), true, features[i]);
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
		tc.setData(COLUMN_HANDLER, this);
		this.column = column;

		return column;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public ICellRenderer getFormatter() {
		return formatter;
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
}