/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.ReportsConstants;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.rcp.common.RunnerHelper;

public class ChangeSetViewCreatorService {

	private static final Logger log = LoggerFactory.getLogger(ChangeSetViewCreatorService.class);
	private final EventHandler openEventHandler = event -> {
		try {
			final AnalyticsSolution solution = (AnalyticsSolution) event.getProperty(IEventBroker.DATA);
			openView(solution, false);
		} catch (final Exception e) {
			log.error("Error handling create change set view event", e);
		}
	};
	private final EventHandler openAndSwitchEventHandler = event -> {
		try {
			final AnalyticsSolution solution = (AnalyticsSolution) event.getProperty(IEventBroker.DATA);
			openView(solution, true);
		} catch (final Exception e) {
			log.error("Error handling create change set view event", e);
		}
	};

	private IEventBroker eventBroker;

	public void start() {

		new Thread() {
			@Override
			public void run() {
				while (!PlatformUI.isWorkbenchRunning()) {
					try {
						Thread.sleep(1_000);
					} catch (final InterruptedException e) {
						log.error(e.getMessage(), e);
						Thread.currentThread().interrupt();
					}
				}

				// Here the workbench has been marked as running. But it may not be really.
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49316
				// This suggests running on the display thread to guarantee this.
				// Note: The previous code got to a point where the workbench service locator was null during an ITS run.
				RunnerHelper.asyncExec(() -> {
					eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
					eventBroker.subscribe(AnalyticsSolution.OPEN_RESULTS_VIEW, openEventHandler);
					eventBroker.subscribe(AnalyticsSolution.OPEN_RESULTS_VIEW_WITH_SCREEN, openAndSwitchEventHandler);
				});
			}
		}.start();
	}

	public void stop() {
		if (eventBroker != null) {
			eventBroker.unsubscribe(openAndSwitchEventHandler);
			eventBroker.unsubscribe(openEventHandler);
			eventBroker = null;
		}
	}

	private void openView(final AnalyticsSolution solution, boolean switchPerspective) {
		final EPartService partService = PlatformUI.getWorkbench().getService(EPartService.class);
		final EModelService modelService = PlatformUI.getWorkbench().getService(EModelService.class);
		final MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);

		RunnerHelper.asyncExec(() -> {
			if (switchPerspective) {
				boolean foundPerspective = false;
				final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
				for (final MPerspective p : perspectives) {
					if (p.getElementId().equals(ReportsConstants.PERSPECTIVE_COMPARE_ID)) {
						try {
							partService.switchPerspective(p);
						} catch (final IllegalStateException e) {
							// SG: I have seen this happen when we have a modal dialog open (it was the optimisation params).
							// log.error("Unable to open compare perspective", e)
						}
						foundPerspective = true;
						break;
					}
				}
				if (!foundPerspective) {
					// Fallback to eclipse 3.x API to open perspective
					try {
						PlatformUI.getWorkbench().showPerspective(ReportsConstants.PERSPECTIVE_COMPARE_ID, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					} catch (final WorkbenchException e) {
						log.error("Unable to open compare perspective", e);
					}
				}
			}

			{
				final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					// No colon in id strings
					final IViewPart part = activePage.showView(ReportsConstants.VIEW_COMPARE_SCENARIOS_ID, null, IWorkbenchPage.VIEW_VISIBLE);
					if (part instanceof ChangeSetView) {
						final ChangeSetView changeSetView = (ChangeSetView) part;
						changeSetView.openAnalyticsSolution(solution);

					}
					return;
				} catch (final PartInitException e) {
					log.error(e.getMessage(), e);
				}
			}
		});
	}
}
