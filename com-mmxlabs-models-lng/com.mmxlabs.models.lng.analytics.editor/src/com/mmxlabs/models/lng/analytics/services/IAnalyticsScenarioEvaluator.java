/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
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

	ScenarioInstance exportResult(ScenarioResult result, String name);

	void evaluateViabilitySandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, ViabilityModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, IProgressMonitor progressMonitor);

	void evaluateMTMSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, MTMModel model, //
			IMapperClass mapper, IProgressMonitor progressMonitor);

	void evaluateBreakEvenSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, BreakEvenAnalysisModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, CompoundCommand cmd);

//<<<<<<< HEAD
//	void runSandboxOptions(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, @Nullable final UserSettings userSettings,
//			Consumer<AbstractSolutionSet> action, boolean runAsync, IProgressMonitor monitor);
//
//	void runSandboxInsertion(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, @Nullable final UserSettings userSettings,
//			Consumer<AbstractSolutionSet> action, boolean runAsync, IProgressMonitor monitor);
//
//	void runSandboxOptimisation(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, @Nullable final UserSettings userSettings,
//			Consumer<AbstractSolutionSet> action, boolean runAsync, IProgressMonitor monitor);

	void runSandboxPriceSensitivity(@NonNull IScenarioDataProvider scenarioDataProvider, ScenarioInstance scenarioInstance, OptionAnalysisModel model, @Nullable final UserSettings userSettings,
			Consumer<AbstractSolutionSet> action, boolean runAsync, IProgressMonitor monitor);
	
	@Nullable
	AbstractSolutionSet runSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull OptionAnalysisModel model,
			@Nullable Consumer<AbstractSolutionSet> action, boolean runAsync, @NonNull IProgressMonitor monitor);

}
