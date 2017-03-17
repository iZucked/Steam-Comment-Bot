/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.lingo.reports.services.ChangeSetViewCreatorService;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class OpenActionableSetsHandler extends AbstractHandler {

	@Override
	public void setEnabled(final Object appContext) {

		final ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, appContext);

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			setBaseEnabled(false);
			return;
		}
		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			setBaseEnabled(false);
			return;
		}
		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				final Object o = ss.getFirstElement();
				if (o instanceof ScenarioInstance) {
					final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
					if (scenarioInstance.getInstance() == null) {
						setBaseEnabled(false);
						return;
					}
					try (ModelReference ref = scenarioInstance.getReference("OpenNewActionPlansHandler:1")) {
						if (ref == null) {
							setBaseEnabled(false);
							return;
						}
						final EObject rootObject = ref.getInstance();
						if (!(rootObject instanceof LNGScenarioModel)) {
							setBaseEnabled(false);
							return;
						}

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
						if (analyticsModel == null) {
							setBaseEnabled(false);
							return;
						}

						setBaseEnabled(!analyticsModel.getActionableSetPlans().isEmpty());
						return;
					}
				}
			}
		}
		setBaseEnabled(false);
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Object o = ss.getFirstElement();
			if (o instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
				try (ModelReference ref = scenarioInstance.getReference("OpenNewActionPlansHandler:2")) {
					final EObject rootObject = ref.getInstance();
					if (!(rootObject instanceof LNGScenarioModel)) {
						return null;
					}

					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
					if (analyticsModel == null) {
						return null;
					}

					if (analyticsModel.getActionableSetPlans().size() == 1) {
						final ActionableSetPlan plan = analyticsModel.getActionableSetPlans().get(0);
						openPlan(scenarioInstance, plan);
					} else {
						final LocalMenuHelper helper = new LocalMenuHelper(HandlerUtil.getActiveShell(event));

						for (final ActionableSetPlan plan : analyticsModel.getActionableSetPlans()) {
							helper.addAction(new RunnableAction(generateName(plan), () -> {
								openPlan(scenarioInstance, plan);
							}));
						}

						helper.open();
					}
				}
			}
		}
		return null;
	}

	private String generateName(final ActionableSetPlan plan) {

		return "Action Plan:";
	}

	private void openPlan(final ScenarioInstance scenarioInstance, final ActionableSetPlan plan) {

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, plan, generateName(plan));
		data.setCreateDiffToBaseAction(true);
		eventBroker.post(ChangeSetViewCreatorService.ChangeSetViewCreatorService_Topic, data);
	}

}