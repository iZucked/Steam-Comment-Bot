/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.handlers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;

/**
 * Eclipse e4 command handler to pack columns in parts adapting to a {@link Grid}.
 * 
 * @author Simon Goodall
 * 
 */
public class PackGridHandler {

	@CanExecute
	public boolean hasGrid(@Active final MPart part, final IAdapterManager adapterManager) {
		final Grid grid = adaptToGrid(part, adapterManager);
		return (grid != null);
	}

	@Execute
	public static void packGrid(@Active final MPart part, final IAdapterManager adapterManager) {

		final Grid grid = adaptToGrid(part, adapterManager);
		if (grid != null) {
			packGrid(grid);
		}
	}

	protected static void packGrid(final Grid grid) {
		if (!grid.isDisposed()) {
			final GridColumn[] columns = grid.getColumns();
			for (final GridColumn c : columns) {
				if (c.getResizeable()) {
					c.pack();
				}
			}
		}
	}

	protected static Grid adaptToGrid(final MPart part, final IAdapterManager adapterManager) {
		Grid grid = null;
		if (part.getObject() instanceof IAdaptable) {
			final IAdaptable adaptable = (IAdaptable) part.getObject();
			grid = (Grid) adaptable.getAdapter(Grid.class);
		}
		if (grid == null) {
			grid = (Grid) adapterManager.getAdapter(part.getObject(), Grid.class);
		}
		return grid;
	}
}
