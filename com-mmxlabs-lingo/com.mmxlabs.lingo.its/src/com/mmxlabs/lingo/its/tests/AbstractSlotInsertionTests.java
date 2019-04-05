/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class AbstractSlotInsertionTests {

	protected void runTest(final URL scenarioURL, final LocalDate periodStart, final YearMonth periodEnd, final int iterations,
			final Function<IScenarioDataProvider, List<EObject>> objectInsertionGetter, final BiConsumer<LNGScenarioRunner, IMultiStateResult> solutionChecker) throws Exception {
		final LiNGOTestDataProvider provider = new LiNGOTestDataProvider(scenarioURL);
		provider.execute(originalScenario -> {
			runScenario(originalScenario, periodStart, periodEnd, iterations, objectInsertionGetter, solutionChecker);
		});
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

		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setWithSpotCargoMarkets(true);

		final List<EObject> objectsToInsert = objectInsertionGetter.apply(scenarioDataProvider);

		final List<Slot<?>> targetSlots = new LinkedList<>();
		final List<VesselEvent> targetEvents = new LinkedList<>();
		for (final Object obj : objectsToInsert) {
			if (obj instanceof Slot) {
				final Slot<?> slot = (Slot<?>) obj;
				targetSlots.add(slot);
			} else if (obj instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) obj;
				targetEvents.add(vesselEvent);
			}
		}

		final LNGSchedulerInsertSlotJobRunner runner = new LNGSchedulerInsertSlotJobRunner(null, scenarioDataProvider, LNGSchedulerJobUtils.createLocalEditingDomain(), userSettings, targetSlots,
				targetEvents, null, null);
		try {
			runner.setIteration(iterations);
			runner.prepare();

			final IMultiStateResult results = runner.runInsertion(new NullProgressMonitor());

			solutionChecker.accept(runner.getLNGScenarioRunner(), results);
		} finally {
			runner.dispose();
		}
	}

	protected SlotInsertionOptimiserUnit getSlotInserter(final LNGScenarioRunner scenarioRunner) {
		@NonNull
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		final InsertionOptimisationStage stage = ScenarioUtils.createDefaultInsertionSettings();

		final SlotInsertionOptimiserUnit slotInserter = new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), stage, scenarioRunner.getExecutorService(),
				dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		return slotInserter;
	}
}
