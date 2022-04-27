/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.LinkedList;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class PriceCurveSetInsertionUnit {
	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private JobExecutorFactory jobExecutorFactory;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	private LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private final IScenarioDataProvider originalScenarioDataProvider;

	private final EditingDomain originalEditingDomain;
	private final OptionAnalysisModel model;
	private final UserSettings userSettings;

	public PriceCurveSetInsertionUnit(@NonNull final LNGDataTransformer dataTransformer, @Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider,
			@NonNull final UserSettings userSettings, @NonNull final JobExecutorFactory jobExecutorFactory, final OptionAnalysisModel model) {
		this.scenarioInstance = scenarioInstance;
		this.originalScenarioDataProvider = scenarioDataProvider;
		this.originalEditingDomain = scenarioDataProvider.getEditingDomain();
		this.userSettings = userSettings;
		this.model = model;
		this.dataTransformer = dataTransformer;
		this.jobExecutorFactory = jobExecutorFactory;
		injector = dataTransformer.getInjector().createChildInjector(new LinkedList<>());
	}

	

}
