/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
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

	void evaluateMarketabilitySandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, MarketabilityModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, IProgressMonitor progressMonitor);

	void evaluateMTMSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, MTMModel model, //
			IMapperClass mapper, IProgressMonitor progressMonitor);

	void evaluateBreakEvenSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, BreakEvenAnalysisModel model,
			IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, CompoundCommand cmd);

	void evaluateSwapValueMatrixSandbox(@NonNull ScenarioInstance scenarioInstance, @NonNull SwapValueMatrixModel model);

	void runSandbox(@NonNull ScenarioInstance scenarioInstance, @NonNull OptionAnalysisModel model);

	void runPriceSensitivity(ScenarioInstance scenarioInstance, SensitivityModel model);
}
