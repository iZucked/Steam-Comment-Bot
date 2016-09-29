package com.mmxlabs.models.ui.tabular;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;

import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.RowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.TopLeftRenderer;

public final class GridViewerHelper {

	public static void configureLookAndFeel(final @NonNull GridTableViewer viewer) {
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());
	}

	public static void configureLookAndFeel(final @NonNull GridTreeViewer viewer) {
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());
	}

	public static void configureLookAndFeel(final @NonNull GridViewerColumn column) {
		column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
	}

	public static void configureLookAndFeel(final @NonNull GridColumnGroup group) {
		group.setHeaderRenderer(new ColumnGroupHeaderRenderer());
	}
}
