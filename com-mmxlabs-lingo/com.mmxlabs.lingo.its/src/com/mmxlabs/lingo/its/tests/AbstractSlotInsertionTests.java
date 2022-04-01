/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public abstract class AbstractSlotInsertionTests {

	protected void runTest(final URL scenarioURL, final LocalDate periodStart, final YearMonth periodEnd, final int iterations,
			final Function<IScenarioDataProvider, List<EObject>> objectInsertionGetter, final BiConsumer<LNGScenarioRunner, IMultiStateResult> solutionChecker) throws Exception {
		final LiNGOTestDataProvider provider = new LiNGOTestDataProvider(scenarioURL);
		provider.execute(originalScenario -> runScenario(originalScenario, periodStart, periodEnd, iterations, objectInsertionGetter, solutionChecker));
	}

	protected void runScenario(final IScenarioDataProvider scenarioDataProvider, final LocalDate periodStart, final YearMonth periodEnd, final int iterations,
			final Function<IScenarioDataProvider, List<EObject>> objectInsertionGetter, final BiConsumer<LNGScenarioRunner, IMultiStateResult> solutionChecker) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();

		if (periodStart != null) {
			userSettings.setPeriodStartDate(periodStart);
		}
		if (periodEnd != null) {
			userSettings.setPeriodEnd(periodEnd);
		}

		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setWithSpotCargoMarkets(true);

		final List<EObject> objectsToInsert = objectInsertionGetter.apply(scenarioDataProvider);

		final List<Slot<?>> targetSlots = new LinkedList<>();
		final List<VesselEvent> targetEvents = new LinkedList<>();
		for (final EObject obj : objectsToInsert) {
			if (obj instanceof Slot<?> slot) {
				targetSlots.add(slot);
			} else if (obj instanceof VesselEvent vesselEvent) {
				targetEvents.add(vesselEvent);
			}
		}

		final LNGSchedulerInsertSlotJobRunner runner = new LNGSchedulerInsertSlotJobRunner(null, scenarioDataProvider, LNGSchedulerJobUtils.createLocalEditingDomain(), userSettings, targetSlots,
				targetEvents, null, null);

		runner.setIterations(iterations);
		runner.prepare();

		final IMultiStateResult results = runner.runInsertion(new SlotInsertionOptimiserLogger(), new NullProgressMonitor());

		solutionChecker.accept(runner.getLNGScenarioRunner(), results);

	}

	protected SlotInsertionOptimiserUnit getSlotInserter(final LNGScenarioRunner scenarioRunner) {
		final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		final @NonNull LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		final InsertionOptimisationStage stage = ScenarioUtils.createDefaultInsertionSettings();

		return new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), stage, scenarioRunner.getJobExecutorFactory(), dataTransformer.getInitialSequences(),
				dataTransformer.getInitialResult(), Collections.emptyList());
	}
}
