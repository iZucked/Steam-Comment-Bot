/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

public class PackTableColumnsAction extends Action {

	private TableViewer viewer;

	public PackTableColumnsAction(TableViewer viewer) {
		super("Pack Columns");
		this.viewer = viewer;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			TableColumn[] columns = viewer.getTable().getColumns();
			for (TableColumn c : columns) {
				c.pack();
			}
		}
	}
}
