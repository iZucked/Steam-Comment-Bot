/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Joiner;
import com.mmxlabs.lingo.reports.services.ChangeSetViewCreatorService;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class OpenInsertionPlansHandler extends AbstractHandler {

	@Override
	public void setEnabled(final Object appContext) {

		final ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, appContext);

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
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
					try (ModelReference ref = scenarioInstance.getReference("OpenInsertionPlansHandler:1")) {
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

						setBaseEnabled(!analyticsModel.getInsertionOptions().isEmpty());
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
				try (ModelReference ref = scenarioInstance.getReference("OpenInsertionPlansHandler:2")) {
					final EObject rootObject = ref.getInstance();
					if (!(rootObject instanceof LNGScenarioModel)) {
						return null;
					}

					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
					if (analyticsModel == null) {
						return null;
					}

					if (analyticsModel.getInsertionOptions().size() == 1) {
						final SlotInsertionOptions plan = analyticsModel.getInsertionOptions().get(0);
						openPlan(scenarioInstance, plan);
					} else {
						final LocalMenuHelper helper = new LocalMenuHelper(HandlerUtil.getActiveShell(event));

						for (final SlotInsertionOptions plan : analyticsModel.getInsertionOptions()) {
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

	private String generateName(final SlotInsertionOptions plan) {

		final List<String> names = new LinkedList<String>();
		for (final Slot s : plan.getSlotsInserted()) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private void openPlan(final ScenarioInstance scenarioInstance, final SlotInsertionOptions plan) {

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post(ChangeSetViewCreatorService.ChangeSetViewCreatorService_Topic, new AnalyticsSolution(scenarioInstance, plan, generateName(plan)));
	}
}