/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class WhatIfEvaluator {

	public static void doOptimise(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, boolean isOptionise) {

		// Pin references as they could change during the optimisation run
		EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();

		final long a = System.currentTimeMillis();

		boolean dualPNLMode = model.getBaseCase().isKeepExistingScenario();

		Consumer<AbstractSolutionSet> action = sandboxResult -> {

			final long b = System.currentTimeMillis();
			System.out.printf("Eval %d\n", b - a);

			CompoundCommand cmd = new CompoundCommand();
			cmd.append(SetCommand.create(editingDomain, model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, sandboxResult));

			if (!cmd.isEmpty()) {
				RunnerHelper.asyncExec(() -> {
					editingDomain.getCommandStack().execute(cmd);
					EMFUtils.checkValidContainment(sdp.getScenario());

					if (sandboxResult != null) {
						final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, sandboxResult, sandboxResult.getName());
						data.open();
					}
				});
			}
		};
		Consumer<IAnalyticsScenarioEvaluator> f1 = evaluator -> {
			if (isOptionise) {
				evaluator.runSandboxInsertion(sdp, scenarioInstance, model, action, true);
			} else {
				evaluator.runSandboxOptimisation(sdp, scenarioInstance, model, action, true);
			}
		};

		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}

	public static void evaluate(IScenarioEditingLocation scenarioEditingLocation, OptionAnalysisModel model) {

		// Pin references as they could change during the optimisation run
		EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();
		EObject scenario = sdp.getScenario();

		final long a = System.currentTimeMillis();

		boolean dualPNLMode = false;// model.getBaseCase().isKeepExistingScenario();

		Consumer<AbstractSolutionSet> action = sandboxResult -> {

			final long b = System.currentTimeMillis();
			System.out.printf("Eval %d\n", b - a);

			CompoundCommand cmd = new CompoundCommand();
			cmd.append(SetCommand.create(editingDomain, model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, sandboxResult));

			if (!cmd.isEmpty()) {
				RunnerHelper.asyncExec(() -> {
					editingDomain.getCommandStack().execute(cmd);
					EMFUtils.checkValidContainment(scenario);

					if (sandboxResult != null) {
						final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, sandboxResult, sandboxResult.getName());
						data.open();
					}
				});
			}

		};

		Consumer<IAnalyticsScenarioEvaluator> f1 = evaluator -> {
			evaluator.runSandboxOptions(sdp, scenarioInstance, model, action, true);
		};

		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}
}
