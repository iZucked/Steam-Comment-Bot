/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeColumn;

public class PackTreeColumnsAction extends Action {

	private TreeViewer viewer;

	public PackTreeColumnsAction(TreeViewer viewer) {
		super("Pack Columns");
		this.viewer = viewer;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			final TreeColumn[] columns = viewer.getTree().getColumns();
			for (final TreeColumn c : columns) {
				c.pack();
			}
		}
	}
}
