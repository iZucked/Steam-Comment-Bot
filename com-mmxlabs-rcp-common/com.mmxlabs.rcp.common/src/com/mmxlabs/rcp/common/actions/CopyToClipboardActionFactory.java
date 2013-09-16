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
 * @since 4.2
 * 
 */
public final class CopyToClipboardActionFactory {

	private CopyToClipboardActionFactory() {

	}

	public CopyGridToClipboardAction createCopyToClipboardAction(final Grid grid) {
		return new CopyGridToClipboardAction(grid);
	}

	public CopyTreeToClipboardAction createCopyToClipboardAction(final TreeViewer viewer) {
		return new CopyTreeToClipboardAction(viewer.getTree());
	}

	public CopyTreeToClipboardAction createCopyToClipboardAction(final Tree tree) {
		return new CopyTreeToClipboardAction(tree);
	}

	public CopyTableToClipboardAction createCopyToClipboardAction(final TableViewer viewer) {
		return new CopyTableToClipboardAction(viewer.getTable());
	}

	public CopyTableToClipboardAction createCopyToClipboardAction(final Table table) {
		return new CopyTableToClipboardAction(table);
	}

	public CopyGridToClipboardAction createCopyToClipboardAction(final GridTreeViewer viewer) {
		return new CopyGridToClipboardAction(viewer.getGrid());
	}

	public CopyGridToClipboardAction createCopyToClipboardAction(final GridTableViewer viewer) {
		return new CopyGridToClipboardAction(viewer.getGrid());
	}
}
