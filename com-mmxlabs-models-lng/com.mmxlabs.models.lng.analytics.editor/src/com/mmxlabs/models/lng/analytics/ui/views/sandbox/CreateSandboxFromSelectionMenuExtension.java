/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.SelectionToSandboxUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class CreateSandboxFromSelectionMenuExtension implements ITradesTableContextMenuExtension {

	private static final Logger log = LoggerFactory.getLogger(CreateSandboxFromSelectionMenuExtension.class);

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SANDBOX)) {
			return;
		}

		ADPModel adpModel = ScenarioModelUtil.getADPModel(scenarioEditingLocation.getScenarioDataProvider());
		if (adpModel != null) {
			// Cannot use sandbox with ADP
			return;
		}

		if (SelectionToSandboxUtil.canSelectionBeUsed(new StructuredSelection(slot))) {
			RunnableAction action = new RunnableAction("Create sandbox", () -> {
				SelectionToSandboxUtil.selectionToSandbox(new StructuredSelection(slot), true, scenarioEditingLocation.getScenarioDataProvider());
			});
			menuManager.add(action);
		}

	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SANDBOX)) {
			return;
		}

		ADPModel adpModel = ScenarioModelUtil.getADPModel(scenarioEditingLocation.getScenarioDataProvider());
		if (adpModel != null) {
			// Cannot use sandbox with ADP
			return;
		}

		if (SelectionToSandboxUtil.canSelectionBeUsed(selection)) {
			RunnableAction action = new RunnableAction("Create sandbox", () -> {
				SelectionToSandboxUtil.selectionToSandbox(selection, true, scenarioEditingLocation.getScenarioDataProvider());
			});
			menuManager.add(action);
		}
	}
}
