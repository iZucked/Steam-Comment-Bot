/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.function.Consumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class WhatIfEvaluator {

	public static void runSandbox(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		// Pin references as they could change during the optimisation run
		EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();

		final long a = System.currentTimeMillis();

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
		Consumer<IAnalyticsScenarioEvaluator> f1 = evaluator -> evaluator.runSandbox(sdp, scenarioInstance, model, action, true, new NullProgressMonitor());
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}

	public static void doPriceSensitivity(IScenarioEditingLocation scenarioEditingLocation, SensitivityModel sensitivityModel) {

		// Pin references as they could change during the optimisation run
		EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();
		EObject scenario = sdp.getScenario();

		final long a = System.currentTimeMillis();

		boolean dualPNLMode = false;// model.getBaseCase().isKeepExistingScenario();

		Consumer<SensitivitySolutionSet> action = sensitivityResult -> {

			final long b = System.currentTimeMillis();
			System.out.printf("Eval %d\n", b - a);

			CompoundCommand cmd = new CompoundCommand();
			cmd.append(SetCommand.create(editingDomain, sensitivityModel, AnalyticsPackage.Literals.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, sensitivityResult));

			if (!cmd.isEmpty()) {
				RunnerHelper.asyncExec(() -> {
					editingDomain.getCommandStack().execute(cmd);
					EMFUtils.checkValidContainment(scenario);
				});
			}
		};

		Consumer<IAnalyticsScenarioEvaluator> f1 = evaluator -> {
			final EObject object = sdp.getScenario();
			if (object instanceof final LNGScenarioModel root) {
				final OptionAnalysisModel model = sensitivityModel.getSensitivityModel();
				final UserSettings previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(model, root);
				final boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;
				final UserSettings userSettings = UserSettingsHelper.promptForSandboxUserSettings(root, false, promptUser, false, null, previousSettings);
				if (userSettings != null) {
					evaluator.runSandboxPriceSensitivity(sdp, scenarioInstance, model, userSettings, action, true, new NullProgressMonitor());
				}
			}
		};

		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}
}
