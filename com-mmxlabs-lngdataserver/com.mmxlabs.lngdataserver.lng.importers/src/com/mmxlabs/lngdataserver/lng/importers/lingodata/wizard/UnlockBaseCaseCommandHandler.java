/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.lng.importers.menus.ScenarioServicePublishAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class UnlockBaseCaseCommandHandler extends AbstractHandler {

	@Override
	public void setEnabled(final Object evaluationContext) {
		setBaseEnabled(UpstreamUrlProvider.INSTANCE.isAvailable() && BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception exceptions[] = new Exception[1];

		final Display display = HandlerUtil.getActiveShellChecked(event).getDisplay();
		BusyIndicator.showWhile(display, () -> {
			try {
				BaseCaseServiceClient.INSTANCE.unlock();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		if (exceptions[0] != null)

		{
			throw new ExecutionException("Unable to unlock basecase: " + exceptions[0], exceptions[0]);
		}

		return null;
	}

}
