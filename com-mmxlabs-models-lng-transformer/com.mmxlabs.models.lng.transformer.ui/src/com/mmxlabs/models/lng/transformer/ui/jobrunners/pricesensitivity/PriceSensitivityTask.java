package com.mmxlabs.models.lng.transformer.ui.jobrunners.pricesensitivity;

import java.time.Instant;
import java.util.HashSet;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.RunnerUtils;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class PriceSensitivityTask {

	private static ToBooleanFunction<IScenarioDataProvider> createValidationFactory(final String scenarioName, final EObject extraTarget) {
		return sdp -> {
			final boolean relaxedValidation = false;
			final boolean optimising = false;
			final boolean displayErrors = System.getProperty("lingo.supress.dialogs") == null;
			return OptimisationHelper.validateScenario(sdp, extraTarget, optimising, displayErrors, relaxedValidation, new HashSet<>());
		};
	}

	private static CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> createParametersFactory(final ScenarioInstance si, final SensitivityModel sensitivityModel) {
		return sdp -> {
			final boolean useDialogs = System.getProperty("lingo.suppress.dialogs") == null;

			PriceSensitivitySettings settings = null;
			{
				final LNGScenarioModel root = sdp.getTypedScenario(LNGScenarioModel.class);
				final UserSettings[] userSettings = new UserSettings[1];
				RunnerHelper.syncExecDisplayOptional(() -> {
					final UserSettings previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(sensitivityModel.getSensitivityModel(), root);
					userSettings[0] = UserSettingsHelper.promptForSandboxUserSettings(root, false, useDialogs, false, null, previousSettings);
					
				});
				if (userSettings[0] != null) {
					settings = new PriceSensitivitySettings();
					settings.setUserSettings(userSettings[0]);
					settings.setSensitivityUUID(sensitivityModel.getUuid());
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

	private static BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> createApplyFactory(final SensitivityModel sensitivityModel, final ScenarioInstance scenarioInstance) {
		return (sdp, result) -> {
			if (result != null) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					final EditingDomain ed = sdp.getEditingDomain();

					final CompoundCommand cmd = new CompoundCommand();
					cmd.append(SetCommand.create(ed, sensitivityModel, AnalyticsPackage.Literals.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, result));
					if (!cmd.isEmpty()) {
						RunnerHelper.asyncExec(() -> {
							ed.getCommandStack().execute(cmd);
//							EMFUtils.checkValidContainment(sdp.getScenario());
						});
					}
				});
			}
		};
	}

	public static void submit(final ScenarioInstance scenarioInstance, final SensitivityModel sensitivityModel, final IJobManager mgr) {
		final String taskName = "Sensitivity " + scenarioInstance.getName();

		final JobDataRecord jobDataRecord = new JobDataRecord();
		jobDataRecord.setCreationDate(Instant.now());

		jobDataRecord.setScenarioInstance(scenarioInstance);
		jobDataRecord.setScenarioName(scenarioInstance.getName());
		jobDataRecord.setScenarioUUID(scenarioInstance.getUuid());

		jobDataRecord.setType(PriceSensitivityJobRunner.JOB_TYPE);

		jobDataRecord.setComponentUUID(sensitivityModel.getUuid());

		mgr.submit(taskName, jobDataRecord, createParametersFactory(scenarioInstance, sensitivityModel), createValidationFactory(scenarioInstance.getName(), sensitivityModel),
				createApplyFactory(sensitivityModel, scenarioInstance));
	}
}
