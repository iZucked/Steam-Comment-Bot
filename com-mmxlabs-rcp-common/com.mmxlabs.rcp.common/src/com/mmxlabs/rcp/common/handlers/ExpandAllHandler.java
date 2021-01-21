/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.osgi.framework.FrameworkUtil;

/**
 * Eclipse e4 command handler to expand all {@link AbstractTreeViewer} nodes.
 * 
 * @author Simon Goodall
 * 
 */
public class ExpandAllHandler {

	public static final String COMMAND_ID = "com.mmxlabs.rcp.common.commands.expandall";

	/**
	 * Create an Eclipse e4 tool item for this command;
	 * 
	 * @return
	 */
	public static MHandledToolItem createExpandAllAction(final String commandId) {

		final MCommand command = MCommandsFactory.INSTANCE.createCommand();
		command.setCommandName("Expand All");
		command.setElementId(commandId);

		final MHandledToolItem element = MMenuFactory.INSTANCE.createHandledToolItem();
		element.setLabel("Expand All");
		element.setIconURI(FrameworkUtil.getBundle(ExpandAllHandler.class).getEntry("/icons/expandall.gif").toString());
		final String location = FrameworkUtil.getBundle(ExpandAllHandler.class).getSymbolicName();
		element.setContributorURI(location);
		element.setCommand(command);

		return element;
	}

	@CanExecute
	public boolean hasAbstractTreeViewer(@Active final MPart part, final IAdapterManager adapterManager) {
		final AbstractTreeViewer viewer = adaptToAbstractTreeViewer(part, adapterManager);
		return (viewer != null);
	}

	@Execute
	public static void executeExpandAll(@Active final MPart part, final IAdapterManager adapterManager) {

		final AbstractTreeViewer viewer = adaptToAbstractTreeViewer(part, adapterManager);
		if (viewer != null) {
			viewer.expandAll();
		}
	}

	protected static AbstractTreeViewer adaptToAbstractTreeViewer(final MPart part, final IAdapterManager adapterManager) {
		AbstractTreeViewer viewer = null;
		if (part.getObject() instanceof IAdaptable) {
			final IAdaptable adaptable = (IAdaptable) part.getObject();
			viewer = adaptable.getAdapter(AbstractTreeViewer.class);
		}
		if (viewer == null) {
			viewer = adapterManager.getAdapter(part.getObject(), AbstractTreeViewer.class);
		}
		return viewer;
	}
}
