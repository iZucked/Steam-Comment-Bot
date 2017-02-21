/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class OpenInsertionPlansHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OpenInsertionPlansHandler.class);

	@Inject
	private IEventBroker eventBroker;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private EPartService partService;

	@Inject
	private EModelService modelService;

	@Inject
	private MApplication application;

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_PART) final MPart part) {
		//
		// if (!LicenseFeatures.isPermitted("features:optimisation-actionset")) {
		// return false;
		// }

		if (part == null) {
			return false;
		}
		final Object selection = selectionService.getSelection(part.getElementId());
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				final Object o = ss.getFirstElement();
				if (o instanceof ScenarioInstance) {
					final ScenarioInstance scenarioInstance = (ScenarioInstance) o;

					try (ModelReference ref = scenarioInstance.getReference("OpenInsertionPlansHandler:1")) {
						final EObject rootObject = ref.getInstance();
						if (!(rootObject instanceof LNGScenarioModel)) {
							return false;
						}

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
						if (analyticsModel == null) {
							return false;
						}

						return !analyticsModel.getInsertionOptions().isEmpty();
					}
				}
			}
		}
		return false;
	}

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_PART) final MPart part, final Shell shell) {
		if (part == null) {
			return;
		}

		final Object selection = selectionService.getSelection(part.getElementId());
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Object o = ss.getFirstElement();
			if (o instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) o;

				try (ModelReference ref = scenarioInstance.getReference("OpenInsertionPlansHandler:2")) {
					final EObject rootObject = ref.getInstance();
					if (!(rootObject instanceof LNGScenarioModel)) {
						return;
					}

					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
					if (analyticsModel == null) {
						return;
					}

					if (analyticsModel.getInsertionOptions().size() == 1) {
						final SlotInsertionOptions plan = analyticsModel.getInsertionOptions().get(0);
						openPlan(scenarioInstance, plan);
					} else {
						final LocalMenuHelper helper = new LocalMenuHelper(shell);

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