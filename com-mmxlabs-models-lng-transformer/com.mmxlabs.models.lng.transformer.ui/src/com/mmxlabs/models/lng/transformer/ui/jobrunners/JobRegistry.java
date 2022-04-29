package com.mmxlabs.models.lng.transformer.ui.jobrunners;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.transformer.jobs.IJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.actionableplan.ActionablePlanJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.actionableplan.ActionablePlanTask;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimisationJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimiserTask;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserTask;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.pricesensitivity.PriceSensitivityJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxTask;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class JobRegistry {
	public static final JobRegistry INSTANCE = new JobRegistry();

	private Map<String, Supplier<IJobRunner>> runnerFactories = new HashMap<>();
	private Map<String, TriFunction<ScenarioInstance, IScenarioDataProvider, JobDataRecord, BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet>>> taskResumeFactories = new HashMap<>();
	private Map<String, String> displayNames = new HashMap<>();

	private JobRegistry() {
		// Match cloud names
		register(SandboxJobRunner.JOB_TYPE, "Sandbox", SandboxJobRunner::new);
		register(OptimisationJobRunner.JOB_TYPE, "Optimisation", OptimisationJobRunner::new);
		register(OptioniserJobRunner.JOB_TYPE, "Optioniser", OptioniserJobRunner::new);
		register(ActionablePlanJobRunner.JOB_TYPE, "Actionable Plan", ActionablePlanJobRunner::new);
		register(PriceSensitivityJobRunner.JOB_TYPE, "Price Sensitivity", PriceSensitivityJobRunner::new);

		taskResumeFactories.put(SandboxJobRunner.JOB_TYPE, SandboxTask::createResumedApplyFactory);
		taskResumeFactories.put(OptimisationJobRunner.JOB_TYPE, OptimiserTask::createResumedApplyFactory);
		taskResumeFactories.put(OptioniserJobRunner.JOB_TYPE, OptioniserTask::createResumedApplyFactory);
		taskResumeFactories.put(ActionablePlanJobRunner.JOB_TYPE, ActionablePlanTask::createResumedApplyFactory);
	}

	private void register(String type, String displayName, Supplier<IJobRunner> factory) {
		runnerFactories.put(type, factory);
		displayNames.put(type, displayName);
	}

	public IJobRunner newRunner(String type) {
		return runnerFactories.get(type).get();
	}

	public String getDisplayName(String lbl) {
		return displayNames.getOrDefault(lbl, lbl);
	}

	public BiConsumer<IScenarioDataProvider, AbstractSolutionSet> createTaskApply(String type, JobDataRecord job, IScenarioDataProvider sdp, ScenarioInstance scenarioInstance) {
		return taskResumeFactories.get(type).apply(scenarioInstance, sdp, job);
	}
}
