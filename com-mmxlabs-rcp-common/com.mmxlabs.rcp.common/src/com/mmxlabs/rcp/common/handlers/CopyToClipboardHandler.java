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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.grid.Grid;
import org.osgi.framework.FrameworkUtil;

import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;

/**
 * Eclipse e4 command handler to copy {@link Grid} data to the clipboard
 * 
 * @author Simon Goodall
 * 
 */
public class CopyToClipboardHandler {

	public static final String COMMAND_ID = "org.eclipse.ui.edit.copy";

	/**
	 * Create an Eclipse e4 tool item for this command;
	 * 
	 * @return
	 */
	public static MHandledToolItem createCopyAction() {

		final MCommand command = MCommandsFactory.INSTANCE.createCommand();
		command.setCommandName("Copy data to clipboard");
		command.setElementId(COMMAND_ID);

		final MHandledToolItem element = MMenuFactory.INSTANCE.createHandledToolItem();
		element.setLabel("Pack");
		element.setIconURI(FrameworkUtil.getBundle(CopyToClipboardHandler.class).getEntry("/icons/copy.gif").toString());
		final String location = FrameworkUtil.getBundle(CopyToClipboardHandler.class).getSymbolicName();
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
	public static void copyGrid(@Active final MPart part, final IAdapterManager adapterManager) {

		final Grid grid = adaptToGrid(part, adapterManager);
		if (grid != null) {
			copyGrid(grid);
		}
	}

	protected static void copyGrid(@NonNull final Grid grid) {
		if (!grid.isDisposed()) {
			CopyGridToClipboardAction action = new CopyGridToClipboardAction(grid);
			action.run();
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
