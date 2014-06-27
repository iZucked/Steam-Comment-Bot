package com.mmxlabs.lingo.reports.components;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.CompiledEMFPath;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class ColumnHandler {
	/**
	 * 
	 */
	private static final String COLUMN_HANDLER = "COLUMN_HANDLER";
	private final IFormatter formatter;
	private final EMFPath path;
	final String title;
	private String tooltip;
	public GridViewerColumn column;
	public int viewIndex;
	private final ColumnBlockManager blockManager;

	public ColumnHandler(final ColumnBlockManager blockManager, final IFormatter formatter, final Object[] features, final String title) {
		super();
		this.blockManager = blockManager;
		this.formatter = formatter;
		this.path = new CompiledEMFPath(getClass().getClassLoader(), true, features);
		this.title = title;
	}

	public GridViewerColumn createColumn(final EObjectTableViewer viewer) {
		final GridViewerColumn column = viewer.addColumn(title, new ICellRenderer() {
			@Override
			public String render(final Object object) {
				return formatter.format(object);
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
				// TODO fix this
				return Collections.emptySet();
			}

			@Override
			public Comparable getComparable(final Object object) {
				return formatter.getComparable(object);
			}

			@Override
			public Object getFilterValue(final Object object) {
				return formatter.getFilterable(object);
			}
		}, NoEditingCellManipulator.INSTANCE, path);

		final GridColumn tc = column.getColumn();
		tc.setData(COLUMN_HANDLER, this);
		this.column = column;

		if (tooltip != null) {
			column.getColumn().setHeaderTooltip(tooltip);
		}

		column.getColumn().setVisible(false);

		return column;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public void setBlockName(final String blockName, final ColumnType columnType) {
		this.blockManager.setHandlerBlockName(this, blockName, columnType);
	}
}