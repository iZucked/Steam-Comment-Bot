/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.navigator.framelist.GoIntoAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

/**
 * Copied from org.eclipse.ui.navigator.resources to avoid pulling in all of it's additional deps into the application.
 * 
 */
@SuppressWarnings("restriction")
public class GoIntoActionProvider extends CommonActionProvider {

	private GoIntoAction goIntoAction;

	@Override
	public void init(ICommonActionExtensionSite anActionSite) {
		anActionSite.getViewSite().getShell();
		CommonViewer viewer = (CommonViewer) anActionSite.getStructuredViewer();
		goIntoAction = new GoIntoAction(viewer.getFrameList());
	}

	@Override
	public void dispose() {
		goIntoAction.dispose();
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(IWorkbenchActionConstants.GO_INTO, goIntoAction);
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		menu.appendToGroup("group.new", goIntoAction); //$NON-NLS-1$
	}

	@Override
	public void updateActionBars() {
		goIntoAction.update();
	}

}
