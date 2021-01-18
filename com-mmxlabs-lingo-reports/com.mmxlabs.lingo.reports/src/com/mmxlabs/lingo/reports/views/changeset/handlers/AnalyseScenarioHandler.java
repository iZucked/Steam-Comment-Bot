/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.ReportsConstants;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class AnalyseScenarioHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AnalyseScenarioHandler.class);

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

		if (!LicenseFeatures.isPermitted("features:optimisation-actionset")) {
			return false;
		}

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

					// Check forks
					for (final Container c : scenarioInstance.getElements()) {
						String name = c.getName();
						if (name != null && name.toLowerCase().startsWith("actionset")) {
							return true;
						}
					}

					// Check siblings.
					final Container parent = scenarioInstance.getParent();
					for (final Container c : parent.getElements()) {
						String name = c.getName();
						if (name != null && name.toLowerCase().startsWith("actionset")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_PART) final MPart part) {
		if (part == null) {
			return;
		}
		// Switch perspective
		boolean foundPerspective = false;
		final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
		for (final MPerspective p : perspectives) {
			if (p.getElementId().equals(ReportsConstants.PERSPECTIVE_COMPARE_ID)) {
				partService.switchPerspective(p);
				foundPerspective = true;
				break;
			}
		}
		if (!foundPerspective) {
			// Fallback to eclipse 3.x API to open perspective
			try {
				PlatformUI.getWorkbench().showPerspective(ReportsConstants.PERSPECTIVE_COMPARE_ID, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			} catch (final WorkbenchException e) {
				LOG.error("Unable to open compare perspective", e);
			}
		}
		{
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {

				// No colon in id strings
				IViewPart actionSetPart = activePage.showView(ReportsConstants.VIEW_COMPARE_SCENARIOS_ID, null, IWorkbenchPage.VIEW_ACTIVATE);
				if (actionSetPart instanceof ChangeSetView) {
					ChangeSetView changeSetView = (ChangeSetView) actionSetPart;

					final Object selection = selectionService.getSelection(part.getElementId());
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ss = (IStructuredSelection) selection;
						final Object o = ss.getFirstElement();
						if (o instanceof ScenarioInstance) {
							final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
							RunnerHelper.asyncExec(() -> changeSetView.openOldStlyeActionSets(scenarioInstance));
						}
					}

				}
				return;
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

}