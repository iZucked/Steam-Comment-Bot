/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.analytics.AnalyticsScenarioEvaluator;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class HeadlessSandboxRunner {

	public AbstractSolutionSet run(final HeadlessSandboxOptions options, final ScenarioModelRecord scenarioModelRecord, @NonNull final IScenarioDataProvider sdp,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, final IProgressMonitor monitor) {
		return run(options, scenarioModelRecord, sdp, completedHook, monitor, false);
	}

	public AbstractSolutionSet run(final HeadlessSandboxOptions options, final ScenarioModelRecord scenarioModelRecord, @NonNull final IScenarioDataProvider sdp,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, final IProgressMonitor monitor, boolean directExecute) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = options.userSettings;

		OptionAnalysisModel model = null;
		for (final OptionAnalysisModel oam : lngScenarioModel.getAnalyticsModel().getOptionModels()) {
			// Prefer UUID
			if (options.sandboxUUID != null) {
				if (Objects.equals(options.sandboxUUID, oam.getUuid())) {
					model = oam;
					break;
				}
			} else if (options.sandboxName != null) {
				if (Objects.equals(options.sandboxName, oam.getName())) {
					model = oam;
					break;
				}
			}
		}
		if (model == null) {
			if (options.sandboxUUID != null) {
				throw new IllegalArgumentException("Missing sandbox " + options.sandboxUUID);
			} else if (options.sandboxName != null) {
				throw new IllegalArgumentException("Missing sandbox " + options.sandboxName);
			}
			throw new IllegalArgumentException("Missing sandbox");
		}

		final AnalyticsScenarioEvaluator evaluator = new AnalyticsScenarioEvaluator();
		final EditingDomain editingDomain = sdp.getEditingDomain();

		final OptionAnalysisModel pModel = model;
		final Consumer<AbstractSolutionSet> action = sandboxResult -> {

			final CompoundCommand cmd = new CompoundCommand();
			cmd.append(SetCommand.create(editingDomain, pModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, sandboxResult));

			if (!cmd.isEmpty()) {
				// Execute directly as e.g. running in ITS and not on display thread
				if (directExecute) {
					cmd.execute();
				} else {
					editingDomain.getCommandStack().execute(cmd);
				}
			}
		};
		final int mode = model.getMode();

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		switch (mode) {
		case SandboxModeConstants.MODE_OPTIONISE -> evaluator.runSandboxInsertion(sdp, null, model, userSettings, action, false, subMonitor);
		case SandboxModeConstants.MODE_OPTIMISE -> evaluator.runSandboxOptimisation(sdp, null, model, userSettings, action, false, subMonitor);
		case SandboxModeConstants.MODE_DERIVE -> evaluator.runSandboxOptions(sdp, null, model, userSettings, action, false, subMonitor);

		}

		return model.getResults();

	}

}
