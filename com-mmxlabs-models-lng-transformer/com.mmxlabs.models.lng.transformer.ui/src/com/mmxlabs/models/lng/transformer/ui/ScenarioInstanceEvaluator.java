/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final @NonNull ScenarioModelRecord modelRecord) {

		modelRecord.executeWithProvider(scenarioDataProvider -> {

			final EObject object = scenarioDataProvider.getScenario();
			if (object instanceof LNGScenarioModel) {

				final LNGScenarioModel scenarioModel = (LNGScenarioModel) object;
				final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService(1);
				try {
					scenarioDataProvider.setLastEvaluationFailed(true);

					final OptimisationPlan p = OptimisationHelper.getOptimiserSettings(scenarioModel, true, null, false, false, null);

					if (p == null) {
						return;
					}
					final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EcoreUtil.copy(p.getUserSettings())));
					RunnerHelper.syncExecDisplayOptional(() -> {
						editingDomain.getCommandStack().execute(cmd);
					});
					scenarioDataProvider.setLastEvaluationFailed(true);

					final LNGScenarioRunner runner = LNGScenarioRunner.make(executorService, scenarioDataProvider, null, p, editingDomain, (IRunnerHook) null, true);
					runner.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} finally {
					executorService.shutdown();
				}
			}
		});
	}
}
