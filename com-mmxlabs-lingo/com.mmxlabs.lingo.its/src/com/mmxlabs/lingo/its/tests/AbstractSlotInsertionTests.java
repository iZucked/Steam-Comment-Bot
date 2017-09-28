/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;

public class AbstractSlotInsertionTests {

	protected void runTest(final URL scenarioURL, final LocalDate periodStart, final YearMonth periodEnd, final int iterations,
			final Function<IScenarioDataProvider, List<EObject>> objectInsertionGetter, final Consumer<IMultiStateResult> solutionChecker) throws Exception {
		final LiNGOTestDataProvider provider = new LiNGOTestDataProvider(scenarioURL);
		provider.execute(originalScenario -> {
			runScenario(originalScenario, periodStart, periodEnd, iterations, objectInsertionGetter, solutionChecker);
		});
	}

	protected void runScenario(final IScenarioDataProvider scenarioDataProvider, final LocalDate periodStart, final YearMonth periodEnd, final int iterations,
			final Function<IScenarioDataProvider, List<EObject>> objectInsertionGetter, final Consumer<IMultiStateResult> solutionChecker) {
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

		final List<Slot> targetSlots = new LinkedList<>();
		final List<VesselEvent> targetEvents = new LinkedList<>();
		for (final Object obj : objectsToInsert) {
			if (obj instanceof Slot) {
				final Slot slot = (Slot) obj;
				targetSlots.add(slot);
			} else if (obj instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) obj;
				targetEvents.add(vesselEvent);
			}
		}

		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			final LNGSchedulerInsertSlotJobRunner runner = new LNGSchedulerInsertSlotJobRunner(executorService, null, scenarioDataProvider, LNGSchedulerJobUtils.createLocalEditingDomain(),
					userSettings, targetSlots, targetEvents);

			runner.prepare();

			final IMultiStateResult results = runner.runInsertion(iterations, new NullProgressMonitor());

			solutionChecker.accept(results);
		} finally {
			executorService.shutdownNow();
		}
	}

	protected SlotInsertionOptimiserUnit getSlotInserter(final LNGScenarioRunner scenarioRunner) {
		@NonNull
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}

		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		final SlotInsertionOptimiserUnit slotInserter = new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
				scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		return slotInserter;
	}
}
