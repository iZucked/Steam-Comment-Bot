/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class CompareScenariosHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(CompareScenariosHandler.class);

	private static final String viewPartId = "com.mmxlabs.lingo.reports.views.changeset.ChangeSetView";

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

							if (LicenseFeatures.isPermitted("features:difftools")) {
								final EPartService partService = PlatformUI.getWorkbench().getService(EPartService.class);
								final EModelService modelService = PlatformUI.getWorkbench().getService(EModelService.class);
								final MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);

								boolean foundPerspective = false;
								final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
								for (final MPerspective p : perspectives) {
									if (p.getElementId().equals("com.mmxlabs.lingo.reports.diff.DiffPerspective")) {
										partService.switchPerspective(p);
										foundPerspective = true;
										break;
									}
								}
								if (!foundPerspective) {
									// Fallback to eclipse 3.x API to open perspective
									try {
										PlatformUI.getWorkbench().showPerspective("com.mmxlabs.lingo.reports.diff.DiffPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
									} catch (final WorkbenchException e) {
										log.error("Unable to open compare perspective", e);
									}
								}
								MPart viewPart = partService.findPart(viewPartId);
								if (viewPart != null) {
									viewPart = partService.showPart(viewPart, PartState.ACTIVATE); // Show part
								}
							}
							ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class,
									(Consumer<IScenarioServiceSelectionProvider>) provider -> provider.setPinnedPair(new ScenarioResult(pin), new ScenarioResult(other), true));
						});
					}
				}
			}
		}
		return null;
	}

}