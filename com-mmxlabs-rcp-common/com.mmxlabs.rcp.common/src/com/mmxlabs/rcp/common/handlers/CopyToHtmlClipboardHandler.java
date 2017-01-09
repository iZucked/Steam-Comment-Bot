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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.Grid;
import org.osgi.framework.FrameworkUtil;

import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;

/**
 * Eclipse e4 command handler to copy {@link Grid} data to the clipboard
 * 
 * @author Simon Goodall
 * 
 */
public class CopyToHtmlClipboardHandler {

	public static final String COMMAND_ID = "org.eclipse.ui.edit.copy";

	/**
	 * Create an Eclipse e4 tool item for this command;
	 * 
	 * @return
	 */
	public static MHandledToolItem createPackAction() {

		final MCommand command = MCommandsFactory.INSTANCE.createCommand();
		command.setCommandName("Copy data to clipboard");
		command.setElementId(COMMAND_ID);

		final MHandledToolItem element = MMenuFactory.INSTANCE.createHandledToolItem();
		element.setLabel("Pack");
		element.setIconURI(FrameworkUtil.getBundle(CopyToHtmlClipboardHandler.class).getEntry("/icons/copy.gif").toString());
		final String location = FrameworkUtil.getBundle(CopyToHtmlClipboardHandler.class).getSymbolicName();
		element.setContributorURI(location);
		element.setCommand(command);

		return element;
	}

	@Execute
	public void doPackGrid(@Active final MPart part, final IAdapterManager adapterManager) {

		packGrid(part, adapterManager);
	}

	@CanExecute
	public boolean hasGrid(@Active final MPart part, final IAdapterManager adapterManager) {
		final Grid grid = adaptToGrid(part, adapterManager);
		return (grid != null);
	}

	public static void packGrid(@Active final MPart part, final IAdapterManager adapterManager) {

		final Grid grid = adaptToGrid(part, adapterManager);
		if (grid != null) {
			final IAdditionalAttributeProvider additionalAttributeProvider = adaptToAdditionalAttributeProvider(part, adapterManager);
			copyGrid(grid, additionalAttributeProvider);
		}
	}

	public static void copyGrid(@NonNull final Grid grid, @Nullable IAdditionalAttributeProvider additionalAttributeProvider) {
		if (!grid.isDisposed()) {
			// TODO: This should be a parameterised command
			final CopyGridToHtmlClipboardAction action = new CopyGridToHtmlClipboardAction(grid, true);
			if (additionalAttributeProvider != null) {
				action.setAdditionalAttributeProvider(additionalAttributeProvider);
				action.setShowBackgroundColours(true);
			}
			action.run();
		}
	}

	protected static @Nullable Grid adaptToGrid(final MPart part, final IAdapterManager adapterManager) {
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

	protected static @Nullable IAdditionalAttributeProvider adaptToAdditionalAttributeProvider(final MPart part, final IAdapterManager adapterManager) {
		IAdditionalAttributeProvider additionalAttributeProvider = null;
		if (part.getObject() instanceof IAdaptable) {
			final IAdaptable adaptable = (IAdaptable) part.getObject();
			additionalAttributeProvider = (IAdditionalAttributeProvider) adaptable.getAdapter(IAdditionalAttributeProvider.class);
		}
		if (additionalAttributeProvider == null) {
			additionalAttributeProvider = (IAdditionalAttributeProvider) adapterManager.getAdapter(part.getObject(), IAdditionalAttributeProvider.class);
		}
		return additionalAttributeProvider;
	}
}
