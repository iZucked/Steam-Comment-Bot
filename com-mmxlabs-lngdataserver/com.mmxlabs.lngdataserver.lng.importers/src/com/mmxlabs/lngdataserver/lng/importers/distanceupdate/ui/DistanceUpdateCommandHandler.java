/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.ui;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LingoDistanceUpdater;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DistanceUpdateCommandHandler extends AbstractHandler {

	protected IStructuredSelection getSelectionToUse(final ExecutionEvent event) {

		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		}
		return selectionToPass;
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IStructuredSelection selection = getSelectionToUse(event);

		if (selection.size() == 1) {

			final Object obj = selection.getFirstElement();
			if (obj instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) obj;
				try {
					LingoDistanceUpdater.importLocalIntoScenario(scenarioInstance);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
