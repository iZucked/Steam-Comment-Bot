package com.mmxlabs.models.lng.transformer.ui.jobrunners.evaluate;

import java.util.Collections;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelReferenceThread;

public class EvaluateTask {

	public static void submit(final ScenarioInstance scenarioInstance) {
		submit(scenarioInstance, true);
	}

	public static void submit(final ScenarioInstance scenarioInstance, boolean withUI) {
		final ScenarioModelRecord scenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);
		submit(scenarioModelRecord, withUI);

	}

	public static void submit(final ScenarioModelRecord scenarioModelRecord, boolean withUI) {

		// We need a special unique thread for exclusive locking.
		final ScenarioModelReferenceThread thread = new ScenarioModelReferenceThread("Evaluate", scenarioModelRecord, sdp -> {
			final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
			scenarioLock.withTryLock(2_000, () -> {
				final boolean useDialogs = withUI && System.getProperty("lingo.suppress.dialogs") == null;

				final UserSettings userSettings;
				if (useDialogs) {
					userSettings = RunnerHelper.syncExecFunc(display -> OptimisationHelper.getEvaluationSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, true));
				} else {
					userSettings = OptimisationHelper.getEvaluationSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, true);
				}

				if (userSettings == null) {
					return;
				}

				RunnerHelper.syncExecDisplayOptional(() -> {
					final EditingDomain editingDomain = sdp.getEditingDomain();
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, sdp.getTypedScenario(LNGScenarioModel.class), LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(),
							EMFCopier.copy(userSettings)));
					editingDomain.getCommandStack().execute(cmd);
				});

				boolean relaxedValidation = "Period Scenario".equals(scenarioModelRecord.getName());
				// New optimisation, so check there are no validation errors.
				if (!OptimisationHelper.validateScenario(sdp, null, false, true, relaxedValidation, Collections.emptySet())) {
					return;
				}

				final LNGOptimisationBuilder runnerBuilder = LNGOptimisationBuilder.begin(sdp) //
						.withUserSettings(userSettings);

				final LNGScenarioRunner runner = runnerBuilder //
						.buildDefaultRunner() //
						.getScenarioRunner();

				sdp.setLastEvaluationFailed(true);
				runner.evaluateInitialState();
				sdp.setLastEvaluationFailed(false);

			});

		});
		thread.start();
	}
}
