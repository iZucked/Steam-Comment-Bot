/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.time.Instant;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.RunnerUtils;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SandboxTask {

	private static ToBooleanFunction<IScenarioDataProvider> createValidationFactory(final String scenarioName, final EObject extraTarget) {
		return sdp -> {
			// New optimisation, so check there are no validation errors.
			final boolean relaxedValidation = "Period Scenario".equals(scenarioName);
			final boolean optimising = true;
			final boolean displayErrors = System.getProperty("lingo.suppress.dialogs") == null;
			final Set<String> extraCategories = Sets.newHashSet(".cargosandbox");
			return OptimisationHelper.validateScenario(sdp, extraTarget, optimising, displayErrors, relaxedValidation, extraCategories);
		};
	}

	private static CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> createParametersFactory(final ScenarioInstance si, final OptionAnalysisModel sandbox) {
		return sdp -> {
			final boolean useDialogs = System.getProperty("lingo.suppress.dialogs") == null;

			SandboxSettings settings = null;
			{
				final LNGScenarioModel root = sdp.getTypedScenario(LNGScenarioModel.class);
				final UserSettings[] userSettings = new UserSettings[1];
				RunnerHelper.syncExecDisplayOptional(() -> {
					final UserSettings previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(sandbox, root);
					if (sandbox.getMode() == SandboxModeConstants.MODE_DEFINE) {
						userSettings[0] = UserSettingsHelper.promptForSandboxUserSettings(root, false, useDialogs, false, null, previousSettings);
					} else if (sandbox.getMode() == SandboxModeConstants.MODE_OPTIMISE) {
						userSettings[0] = UserSettingsHelper.promptForUserSettings(root, false, useDialogs, false, null, previousSettings);
					} else if (sandbox.getMode() == SandboxModeConstants.MODE_OPTIONISE) {
						userSettings[0] = UserSettingsHelper.promptForInsertionUserSettings(root, false, useDialogs, false, null, previousSettings);
					}
				});
				if (userSettings[0] != null) {
					settings = new SandboxSettings();
					settings.setUserSettings(userSettings[0]);
					settings.setSandboxUUID(sandbox.getUuid());
				}
			}
			if (settings == null) {
				return null;
			}

			// Convert to JSON
			final ObjectMapper mapper = RunnerUtils.createObjectMapper();
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(settings);
		};
	}

	private static BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> createApplyFactory(final OptionAnalysisModel sandbox, final ScenarioInstance scenarioInstance,
			final boolean openResult) {
		return (sdp, result) -> {
			if (result != null) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					final EditingDomain ed = sdp.getEditingDomain();
					ed.getCommandStack().execute(SetCommand.create(ed, sandbox, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, result));

					EMFUtils.checkValidContainment(sdp.getScenario());

					if (openResult) {
						final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, result, result.getName());
						data.open();
					}
				});
			}
		};
	}

	private static String getSubType(final OptionAnalysisModel sandbox) {
		if (sandbox.getMode() == SandboxModeConstants.MODE_DEFINE) {
			return "Define";
		} else if (sandbox.getMode() == SandboxModeConstants.MODE_OPTIMISE) {
			return "Optimise";
		} else if (sandbox.getMode() == SandboxModeConstants.MODE_OPTIONISE) {
			return "Optionise";
		}
		throw new IllegalArgumentException("Unknown sandbox mode");
	}

	public static void submit(final ScenarioInstance scenarioInstance, final OptionAnalysisModel sandbox, final IJobManager mgr) {

		final String taskName = "Sandbox " + scenarioInstance.getName();

		final JobDataRecord jobDataRecord = new JobDataRecord();

		jobDataRecord.setCreationDate(Instant.now());

		jobDataRecord.setScenarioInstance(scenarioInstance);
		jobDataRecord.setScenarioName(scenarioInstance.getName());
		jobDataRecord.setScenarioUUID(scenarioInstance.getUuid());

		jobDataRecord.setType(SandboxJobRunner.JOB_TYPE);

		jobDataRecord.setSubType(getSubType(sandbox));
		jobDataRecord.setComponentUUID(sandbox.getUuid());

		mgr.submit(taskName, jobDataRecord, createParametersFactory(scenarioInstance, sandbox), createValidationFactory(scenarioInstance.getName(), sandbox),
				createApplyFactory(sandbox, scenarioInstance, true));
	}

	public static BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> createResumedApplyFactory(ScenarioInstance scenarioInstance, IScenarioDataProvider sdp,
			JobDataRecord jobDataRecord) {

		LNGScenarioModel scenario = sdp.getTypedScenario(LNGScenarioModel.class);
		for (var sandbox : scenario.getAnalyticsModel().getOptionModels()) {
			if (sandbox.getUuid().equals(jobDataRecord.getComponentUUID())) {

				return createApplyFactory(sandbox, scenarioInstance, true);
			}
		}
		throw new IllegalStateException("Sandbox not found");

	}
}
