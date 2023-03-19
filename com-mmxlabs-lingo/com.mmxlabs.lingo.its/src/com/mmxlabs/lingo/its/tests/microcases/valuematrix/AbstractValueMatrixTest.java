/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.valuematrix;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.analytics.SwapValueMatrixUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.valuematrix.DefaultValueMatrixPairsGenerator;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.valuematrix.ValueMatrixTask;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

public abstract class AbstractValueMatrixTest extends AbstractMicroTestCase {

	protected void evaluateValueMatrix(final @NonNull SwapValueMatrixModel valueMatrixModel, final boolean withCharterLength) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setWithCharterLength(withCharterLength);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		lngScenarioModel.setUserSettings(userSettings);
		final IMapperClass mapper = new Mapper(lngScenarioModel, false);

		@NonNull
		final Pair<@NonNull LoadSlot, @NonNull DischargeSlot> swapCargo = ValueMatrixTask.buildFullScenario(lngScenarioModel, valueMatrixModel, mapper);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		final List<String> hints = new LinkedList<>();
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);

		final JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();
		helper.generateWith(null, userSettings, scenarioDataProvider.getEditingDomain(), hints, bridge -> {
			bridge.getFullDataTransformer().getLifecyleManager().startPhase("value-matrix", hints);
			final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
			final SwapValueMatrixUnit unit = new SwapValueMatrixUnit(scenarioDataProvider, dataTransformer, "swap-value-matrix", userSettings, constraintAndFitnessSettings, jobExecutorFactory,
					dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
			unit.run(valueMatrixModel, swapCargo, mapper, new NullProgressMonitor(), bridge, new DefaultValueMatrixPairsGenerator(valueMatrixModel));
		});
	}

	protected void evaluateValueMatrixWithOrder(final @NonNull SwapValueMatrixModel valueMatrixModel, final @NonNull Iterable<Pair<Integer, Integer>> order, final boolean sequential, final boolean withCharterLength) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setWithCharterLength(withCharterLength);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		lngScenarioModel.setUserSettings(userSettings);
		final IMapperClass mapper = new Mapper(lngScenarioModel, false);

		@NonNull
		final Pair<@NonNull LoadSlot, @NonNull DischargeSlot> swapCargo = ValueMatrixTask.buildFullScenario(lngScenarioModel, valueMatrixModel, mapper);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		final List<String> hints = new LinkedList<>();
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);
		final JobExecutorFactory jobExecutorFactory;
		if (sequential) {
			jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService(1);
		} else {
			jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();
		}
		helper.generateWith(null, userSettings, scenarioDataProvider.getEditingDomain(), hints, bridge -> {
			final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
			bridge.getFullDataTransformer().getLifecyleManager().startPhase("value-matrix", hints);
			final SwapValueMatrixUnit unit = new SwapValueMatrixUnit(scenarioDataProvider, dataTransformer, "swap-value-matrix", userSettings, constraintAndFitnessSettings, jobExecutorFactory,
					dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
			unit.run(valueMatrixModel, swapCargo, mapper, new NullProgressMonitor(), bridge, order);
		});
	}
}
