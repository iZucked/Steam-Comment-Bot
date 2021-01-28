/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface IAnalyticsScenarioEvaluator {
	enum BreakEvenMode {
		POINT_TO_POINT, PORTFOLIO
	}

	// void evaluateBaseCase(@NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, boolean fork, String forkName);

	// void multiEvaluate(@Nullable ScenarioInstance scenarioInstance, EditingDomain editingDomain, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings,
	// long targetProfitAndLoss, BreakEvenMode breakEvenMode, List<Pair<BaseCase, ScheduleSpecification>> baseCases, IMapperClass mapper, BiConsumer<BaseCase, Schedule> resultHandler);

	ScenarioInstance exportResult(ScenarioResult result, String name);

	void evaluateViabilitySandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, ViabilityModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap);

	void evaluateMTMSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, MTMModel model, IMapperClass mapper);

	void evaluateBreakEvenSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, BreakEvenAnalysisModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, CompoundCommand cmd);

	// void multiEvaluate(ScenarioInstance scenarioInstance, EditingDomain editingDomain, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings,
	// Long targetProfitAndLoss, BreakEvenMode breakEvenMode, boolean dualPNLMode, Pair<BaseCase, ScheduleSpecification> baseCase, List<Pair<BaseCase, ScheduleSpecification>> baseCases,
	// SandboxResult plan, IMapperClass mapper, BiConsumer<BaseCase, ResultSet> resultHandler);

	void runSandboxOptions(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, Consumer<AbstractSolutionSet> action, boolean runAsync);

	void runSandboxInsertion(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, Consumer<AbstractSolutionSet> action, boolean runAsync);

	void runSandboxOptimisation(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, Consumer<AbstractSolutionSet> action,
			boolean runAsync);

	// AbstractSolutionSet runSandboxOptimisation(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings,
	// OptionAnalysisModel model, IMapperClass mapper);
}
