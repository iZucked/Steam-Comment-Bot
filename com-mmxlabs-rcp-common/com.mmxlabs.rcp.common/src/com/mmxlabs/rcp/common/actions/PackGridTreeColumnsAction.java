/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.rcp.common.internal.Activator;

/**
 */
public class PackGridTreeColumnsAction extends Action {

	private final GridTreeViewer viewer;

	public PackGridTreeColumnsAction(final GridTreeViewer viewer) {
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
