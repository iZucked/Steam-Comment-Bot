/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class PanamaCanalTariffHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);

		final IWorkbenchPart activePart = HandlerUtil.getActivePart(event);

		final IScenarioEditingLocation scenarioEditingLocation = (IScenarioEditingLocation) activePart.getAdapter(IScenarioEditingLocation.class);
		if (scenarioEditingLocation == null) {
			return null;
		}

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}

		final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
		final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);
		final PanamaCanalTariff panamaCanalTariff = costModel.getPanamaCanalTariff();
		if (panamaCanalTariff == null) {
			return null;
		}
		if (scenarioEditingLocation.isLocked() == false) {
			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.claim();
				scenarioEditingLocation.setDisableUpdates(true);
				DetailCompositeDialog dialog = new DetailCompositeDialog(activeShell, scenarioEditingLocation.getDefaultCommandHandler());
				dialog.open(scenarioEditingLocation, lngScenarioModel, Collections.singletonList(costModel.getPanamaCanalTariff()));
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				editorLock.release();
			}
		}

		return null;
	}
}
