/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.handlers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.osgi.framework.FrameworkUtil;

/**
 * Eclipse e4 command handler to pack columns in parts adapting to a {@link Grid}.
 * 
 * @author Simon Goodall
 * 
 */
public class PackGridHandler {

	public static final String COMMAND_ID = "com.mmxlabs.rcp.common.command.pack";

	/**
	 * Create an Eclipse e4 tool item for this command;
	 * 
	 * @return
	 */
	public static MHandledToolItem createPackAction() {

		final MCommand command = MCommandsFactory.INSTANCE.createCommand();
		command.setCommandName("Pack table data");
		command.setElementId(COMMAND_ID);

		final MHandledToolItem element = MMenuFactory.INSTANCE.createHandledToolItem();
		element.setLabel("Pack");
		element.setIconURI(FrameworkUtil.getBundle(PackGridHandler.class).getEntry("/icons/pack.gif").toString());
		final String location = FrameworkUtil.getBundle(PackGridHandler.class).getSymbolicName();
		element.setContributorURI(location);
		element.setCommand(command);

		return element;
	}

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
