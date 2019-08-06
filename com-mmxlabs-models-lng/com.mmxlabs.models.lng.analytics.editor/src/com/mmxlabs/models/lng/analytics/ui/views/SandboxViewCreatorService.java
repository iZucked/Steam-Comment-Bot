/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;

public class SandboxViewCreatorService {

	public static final String ChangeSetViewCreatorService_Topic = "create-sandbox-view";

	private static final Logger log = LoggerFactory.getLogger(SandboxViewCreatorService.class);
	private final EventHandler eventHandler = event -> {
		try {
			final SandboxScenario solution = (SandboxScenario) event.getProperty(IEventBroker.DATA);
			openView(solution);
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
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Here the workbench has been marked as running. But it may not be really.
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49316
				// This suggests running on the display thread to guarantee this.
				// Note: The previous code got to a point where the workbench service locator was null during an ITS run.
				RunnerHelper.asyncExec(() -> {
					eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
					eventBroker.subscribe(ChangeSetViewCreatorService_Topic, eventHandler);
				});
			};
		}.start();
	}

	public void stop() {
		if (eventBroker != null) {
			eventBroker.unsubscribe(eventHandler);
			eventBroker = null;
		}
	}

	private void openView(final SandboxScenario sandboxScenario) {
		final EPartService partService = PlatformUI.getWorkbench().getService(EPartService.class);
		final EModelService modelService = PlatformUI.getWorkbench().getService(EModelService.class);
		final MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);

		RunnerHelper.asyncExec(() -> {

			final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				// No colon in id strings
				final IViewPart part = activePage.showView(OptionModellerView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				if (part instanceof OptionModellerView) {
					final OptionModellerView sandboxView = (OptionModellerView) part;
					sandboxView.openSandboxScenario(sandboxScenario);
				}
				return;
			} catch (final PartInitException e) {
				e.printStackTrace();
			}
		});
	}
}
