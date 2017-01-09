/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

import com.mmxlabs.rcp.common.internal.Activator;

public class PackTableColumnsAction extends Action {

	private final TableViewer viewer;

	private final boolean onlyResizable;

	public PackTableColumnsAction(final TableViewer viewer) {
		this(viewer, false);
	}

	/**
	 * Create the Action, specifying whether to pack() all {@link TableColumn}s or only those where {@link TableColumn#getResizable()} returns true.
	 * 
	 */
	public PackTableColumnsAction(final TableViewer viewer, final boolean onlyResizable) {
		super("Pack Columns");
		setImageDescriptor(Activator.getImageDescriptor("/icons/pack.gif"));
		this.viewer = viewer;
		this.onlyResizable = onlyResizable;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			final TableColumn[] columns = viewer.getTable().getColumns();
			for (final TableColumn c : columns) {
				if (!onlyResizable || c.getResizable()) {
					c.pack();
				}
			}
		}
	}
}
