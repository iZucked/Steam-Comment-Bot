/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final @NonNull ModelRecord modelRecord) {

		modelRecord.execute(modelReference -> {

			final EObject object = modelReference.getInstance();
			if (object instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) object;
				final ExecutorService executorService = Executors.newSingleThreadExecutor();
				try {
					final OptimisationPlan p = OptimisationHelper.getOptimiserSettings(scenarioModel, true, null, false, false);

					if (p == null) {
						return;
					}
					final EditingDomain editingDomain = modelReference.getEditingDomain();
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EcoreUtil.copy(p.getUserSettings())));
					RunnerHelper.syncExecDisplayOptional(() -> {
						editingDomain.getCommandStack().execute(cmd);
					});
					modelReference.setLastEvaluationFailed(true);

					final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, scenarioModel, null, p, editingDomain, (IRunnerHook) null, true);
					runner.evaluateInitialState();
					modelReference.setLastEvaluationFailed(false);
				} finally {
					executorService.shutdown();
				}
			}
		});
	}
}
