/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * 
 * Helper class to create the various copy to clipboard action classes
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public final class CopyToClipboardActionFactory {

	private CopyToClipboardActionFactory() {

	}

	public static CopyGridToHtmlClipboardAction createCopyToHtmlClipboardAction(final GridTableViewer viewer, boolean includeRowHeaders) {
		return new CopyGridToHtmlClipboardAction(viewer.getGrid(), includeRowHeaders);
	}

	public static CopyGridToClipboardAction createCopyToClipboardAction(final Grid grid) {
		return new CopyGridToClipboardAction(grid);
	}

	public static CopyTreeToClipboardAction createCopyToClipboardAction(final TreeViewer viewer) {
		return new CopyTreeToClipboardAction(viewer.getTree());
	}

	public static CopyTreeToClipboardAction createCopyToClipboardAction(final Tree tree) {
		return new CopyTreeToClipboardAction(tree);
	}

	public static CopyTableToClipboardAction createCopyToClipboardAction(final TableViewer viewer) {
		return new CopyTableToClipboardAction(viewer.getTable());
	}

	public static CopyTableToClipboardAction createCopyToClipboardAction(final Table table) {
		return new CopyTableToClipboardAction(table);
	}

	public static CopyGridToClipboardAction createCopyToClipboardAction(final GridTreeViewer viewer) {
		return new CopyGridToClipboardAction(viewer.getGrid());
	}

	public static CopyGridToClipboardAction createCopyToClipboardAction(final GridTableViewer viewer) {
		return new CopyGridToClipboardAction(viewer.getGrid());
	}

	public static String generateCSV(Object obj) {
		if (obj instanceof Grid || obj instanceof GridTreeViewer || obj instanceof GridTableViewer)
			return createCopyToClipboardAction((Grid) obj).parseGridIntoStringWriter().toString();
		else if (obj instanceof TreeViewer)
			return createCopyToClipboardAction((TreeViewer) obj).parseTreeIntoStringWriter().toString();
		else if (obj instanceof Tree || obj instanceof TreeViewer)
			return createCopyToClipboardAction((Tree) obj).parseTreeIntoStringWriter().toString();
		else if (obj instanceof Table || obj instanceof TableViewer)
			return createCopyToClipboardAction((Table) obj).parseTableIntoStringWriter().toString();

		return "";
	}
}
