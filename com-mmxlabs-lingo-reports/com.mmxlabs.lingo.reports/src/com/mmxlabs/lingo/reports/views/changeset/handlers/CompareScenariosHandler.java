/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.ReportsConstants;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class CompareScenariosHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(CompareScenariosHandler.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 2) {
				List<?> list = ss.toList();
				Object first = list.get(0);
				Object second = list.get(1);
				if (first != second) {
					if (first instanceof ScenarioInstance && second instanceof ScenarioInstance) {
						ScenarioInstance pin = (ScenarioInstance) first;
						ScenarioInstance other = (ScenarioInstance) second;

						RunnerHelper.asyncExec(() -> {

							 {
								final EPartService partService = PlatformUI.getWorkbench().getService(EPartService.class);
								final EModelService modelService = PlatformUI.getWorkbench().getService(EModelService.class);
								final MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);

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
										log.error("Unable to open compare perspective", e);
									}
								}
								{
									IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
									try {
										// No colon in id strings
										IViewPart part = activePage.showView(ReportsConstants.VIEW_COMPARE_SCENARIOS_ID, null, IWorkbenchPage.VIEW_ACTIVATE);

										return;
									} catch (PartInitException e) {
										e.printStackTrace();
									}
								}

							}
						});
						// Split call to allow some time to display the view before triggering the selection. There probably is still a race-condition here
						RunnerHelper.asyncExec(() -> {

							ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class,
									(Consumer<IScenarioServiceSelectionProvider>) provider -> provider.setPinnedPair(new ScenarioResultImpl(pin), new ScenarioResultImpl(other), true));
						});
					}
				}
			}
		}
		return null;
	}

}