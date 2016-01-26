/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * Eclipse e4 command handler to collapse all {@link AbstractTreeViewer} nodes.
 * 
 * @author Simon Goodall
 * 
 */
public class CollapseAllHandler {

	public static final String COMMAND_ID = "com.mmxlabs.rcp.common.commands.collapseall";

	/**
	 * Create an Eclipse e4 tool item for this command;
	 * 
	 * @return
	 */
	public static MHandledToolItem createCollapseAllAction(final String commandId) {

		final MCommand command = MCommandsFactory.INSTANCE.createCommand();
		command.setCommandName("Collapse All");
		command.setElementId(commandId);

		final MHandledToolItem element = MMenuFactory.INSTANCE.createHandledToolItem();
		element.setLabel("Collapse All");
		element.setIconURI(FrameworkUtil.getBundle(CollapseAllHandler.class).getEntry("/icons/collapseall.gif").toString());
		final String location = FrameworkUtil.getBundle(CollapseAllHandler.class).getSymbolicName();
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
	public static void executeCollapseAll(@Active final MPart part, final IAdapterManager adapterManager) {

		final AbstractTreeViewer viewer = adaptToAbstractTreeViewer(part, adapterManager);
		if (viewer != null) {
			viewer.collapseAll();
		}
	}

	protected static AbstractTreeViewer adaptToAbstractTreeViewer(final MPart part, final IAdapterManager adapterManager) {
		AbstractTreeViewer viewer = null;
		if (part.getObject() instanceof IAdaptable) {
			final IAdaptable adaptable = (IAdaptable) part.getObject();
			viewer = (AbstractTreeViewer) adaptable.getAdapter(AbstractTreeViewer.class);
		}
		if (viewer == null) {
			viewer = (AbstractTreeViewer) adapterManager.getAdapter(part.getObject(), AbstractTreeViewer.class);
		}
		return viewer;
	}
}
