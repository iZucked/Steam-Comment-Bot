/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper.NameProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.RunnerUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class OptimiserTask {

	private static ToBooleanFunction<IScenarioDataProvider> createValidationFactory(final String scenarioName) {
		return sdp -> {
			// New optimisation, so check there are no validation errors.
			final boolean relaxedValidation = "Period Scenario".equals(scenarioName);
			final EObject extraTarget = null;
			final boolean optimising = true;
			final boolean displayErrors = System.getProperty("lingo.suppress.dialogs") == null;
			final Set<String> extraCategories = Collections.emptySet();
			return OptimisationHelper.validateScenario(sdp, extraTarget, optimising, displayErrors, relaxedValidation, extraCategories);
		};
	}

	private static CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> createParametersFactory(final ScenarioInstance si) {
		final boolean applySettingToScenario = true;
		return sdp -> {
			final boolean useDialogs = System.getProperty("lingo.suppress.dialogs") == null;

			final OptimisationSettings settings;
			{

				final Set<String> existingNames = new HashSet<>();
				if (si != null) {
					si.getFragments().forEach(f -> existingNames.add(f.getName()));
					si.getElements().forEach(f -> existingNames.add(f.getName()));
				}
				final NameProvider np = new NameProvider("Optimisation", existingNames);

				final Pair<String, UserSettings> p;
				if (useDialogs) {
					p = RunnerHelper.syncExecFunc(display -> OptimisationHelper.getOptimiserSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, useDialogs, np));
				} else {
					p = OptimisationHelper.getOptimiserSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, useDialogs, np);
				}

				if (p == null) {
					return null;
				}

				settings = new OptimisationSettings();
				settings.setResultName(p.getFirst());
				settings.setUserSettings(p.getSecond());
			}

			if (applySettingToScenario) {
				// Save current user settings.
				RunnerHelper.syncExecDisplayOptional(() -> {
					final UserSettings userSettings = settings.getUserSettings();
					if (userSettings != null) {
						final EditingDomain editingDomain = sdp.getEditingDomain();
						final CompoundCommand cmd = new CompoundCommand("Update settings");
						cmd.append(SetCommand.create(editingDomain, sdp.getTypedScenario(LNGScenarioModel.class), LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(),
								EMFCopier.copy(userSettings)));
						RunnerHelper.syncExecDisplayOptional(() -> editingDomain.getCommandStack().execute(cmd));
					}
				});
			}

			// Convert to JSON
			final ObjectMapper mapper = RunnerUtils.createObjectMapper();
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(settings);
		};
	}

	private static BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> createApplyFactory() {
		return (sdp, result) -> {
			if (result != null) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					final EditingDomain ed = sdp.getEditingDomain();
					final AnalyticsModel am = ScenarioModelUtil.getAnalyticsModel(sdp);
					ed.getCommandStack().execute(AddCommand.create(ed, am, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS, result));
				});
			}
		};
	}

	public static void submit(final ScenarioInstance scenarioInstance, final IJobManager mgr) {

		final String taskName = "Optimise " + scenarioInstance.getName();

		final JobDataRecord jobDataRecord = new JobDataRecord();

		jobDataRecord.setCreationDate(Instant.now());

		jobDataRecord.setScenarioInstance(scenarioInstance);
		jobDataRecord.setScenarioName(scenarioInstance.getName());
		jobDataRecord.setScenarioUUID(scenarioInstance.getUuid());

		jobDataRecord.setType(OptimisationJobRunner.JOB_TYPE);

		mgr.submit(taskName, jobDataRecord, createParametersFactory(scenarioInstance), createValidationFactory(scenarioInstance.getName()), createApplyFactory());
	}

	public static void resume(final ScenarioInstance scenarioInstance, final JobDataRecord job, final IJobManager mgr) {
		final String taskName = "Optimise " + scenarioInstance.getName();

		final JobDataRecord jobDataRecord = new JobDataRecord();

		jobDataRecord.setCreationDate(Instant.now());

		jobDataRecord.setScenarioInstance(scenarioInstance);
		jobDataRecord.setScenarioName(scenarioInstance.getName());
		jobDataRecord.setScenarioUUID(scenarioInstance.getUuid());

		jobDataRecord.setType(OptimisationJobRunner.JOB_TYPE);

		mgr.submit(taskName, jobDataRecord, null, null, createApplyFactory());
	}

	public static BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> createResumedApplyFactory(final ScenarioInstance scenarioInstance, final IScenarioDataProvider sdp,
			final JobDataRecord jobDataRecord) {
		return createApplyFactory();
	}
}
