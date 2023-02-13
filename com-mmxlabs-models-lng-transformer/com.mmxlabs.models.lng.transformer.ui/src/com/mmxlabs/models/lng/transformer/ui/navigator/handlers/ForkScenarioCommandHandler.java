/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * @author Simon Goodall
 * 
 */
public class ForkScenarioCommandHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception[] exceptions = new Exception[1];

		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object element = itr.next();

						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;

							try {
								final String finalNewName = ScenarioServiceModelUtils.getNewForkName(instance, false);
								if (finalNewName != null) {

									final ScenarioInstance fork = ScenarioServiceModelUtils.fork(instance, finalNewName, new NullProgressMonitor());

									stripScenario(fork);

									OpenScenarioUtils.openScenarioInstance(fork);
								}
							} catch (final Exception e) {
								exceptions[0] = e;
							}
						}
					}
				}
			}

		});

		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to fork scenario: " + exceptions[0], exceptions[0]);
		}

		return null;

	}

	private void stripScenario(final ScenarioInstance scenarioInstance) throws IOException {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ForkScenarioCommandHandler:stripScenario")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
			// Strip optimisation result
			LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

			scenarioDataProvider.getModelReference().save();
		}
	}
}
