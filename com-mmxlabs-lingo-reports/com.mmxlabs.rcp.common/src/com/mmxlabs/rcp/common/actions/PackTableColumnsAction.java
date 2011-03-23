/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

import com.mmxlabs.rcp.common.Activator;

public class PackTableColumnsAction extends Action {

	private TableViewer viewer;

	public PackTableColumnsAction(TableViewer viewer) {
		super("Pack Columns");
		setImageDescriptor(Activator.getImageDescriptor("/icons/pack.gif"));
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
