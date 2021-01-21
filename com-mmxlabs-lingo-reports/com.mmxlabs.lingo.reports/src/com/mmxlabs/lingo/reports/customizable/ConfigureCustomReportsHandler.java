/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureCustomReportsHandler extends AbstractHandler {

	private static Logger logger = LoggerFactory.getLogger(ConfigureCustomReportsHandler.class);
	
	//com.mmxlabs.lingo.reports.views.schedule.ConfigureCustomReportsHandler
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		try {
			final IWorkbench wb = PlatformUI.getWorkbench();
			final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			openCustomReportsManagerDialog(win);
		}
		catch (Exception ex) {
			logger.error("Problem opening Custom Reports Manager dialog: ", ex);
		}

		return null;
	}
	
	private void openCustomReportsManagerDialog(final IWorkbenchWindow win) {	
		final CustomReportsManagerDialog dialog = new CustomReportsManagerDialog(win.getShell());
		dialog.open();
		dialog.dispose();
	}
}
