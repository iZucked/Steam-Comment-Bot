/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.rcp.common.internal.Activator;

public class PackGridTableColumnsAction extends Action {

	private final GridTableViewer viewer;

	public PackGridTableColumnsAction(final GridTableViewer viewer) {
		super("Pack Columns");
		setImageDescriptor(Activator.getImageDescriptor("/icons/pack.gif"));
		this.viewer = viewer;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			final GridColumn[] columns = viewer.getGrid().getColumns();
			for (final GridColumn c : columns) {
				if (c.getResizeable()) {
					c.pack();
				}
			}
		}
	}
}
