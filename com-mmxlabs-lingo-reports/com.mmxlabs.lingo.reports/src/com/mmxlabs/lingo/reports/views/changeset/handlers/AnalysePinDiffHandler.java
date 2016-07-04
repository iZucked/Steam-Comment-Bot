/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

public class AnalysePinDiffHandler {

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
	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_PART) final MPart part) {

		return true;
	}

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_PART) final MPart _part) {
//		if (part == null) {
//			return;
//		}
		// Switch perspective
		final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
		for (final MPerspective p : perspectives) {
			if (p.getElementId().equals("com.mmxlabs.lingo.reports.diff.DiffPerspective")) {
				partService.switchPerspective(p);
			}
		}

		// Activate change set view
		partService.showPart("com.mmxlabs.lingo.reports.views.changeset.ChangeSetView", PartState.ACTIVATE);

		// final Object selection = selectionService.getSelection(part.getElementId());
		// if (selection instanceof IStructuredSelection) {
		// final IStructuredSelection ss = (IStructuredSelection) selection;
		// final Object o = ss.getFirstElement();
		// if (o instanceof ScenarioInstance) {
		// final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
		eventBroker.post(ChangeSetViewEventConstants.EVENT_ANALYSE_CHANGE_SETS, null);
		// }
		// }
	}

}