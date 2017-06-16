/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;

public class MicroTestUtils {
	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static List<IConstraintChecker> validateConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());
		final List<IConstraintChecker> constraintCheckers = evaluationTransformerUnit.getInjector().getInstance(Key.get(new TypeLiteral<List<IConstraintChecker>>() {
		}));
		final ISequencesManipulator sequencesManipulator = evaluationTransformerUnit.getInjector().getInstance(ISequencesManipulator.class);

		final List<IConstraintChecker> failedCheckers = new LinkedList<>();
		// // Apply sequence manipulators
		final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(fullSequences, null)) {
				failedCheckers.add(checker);
			}
		}
		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	public static Injector createEvaluationInjector(@NonNull final LNGDataTransformer dataTransformer) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());

		return evaluationTransformerUnit.getInjector();
	}

	/**
	 * Returns null on success, or returns the failing evaluated state constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static List<@NonNull IEvaluatedStateConstraintChecker> validateEvaluatedStateConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());
		final Injector injector = evaluationTransformerUnit.getInjector();

		final List<@NonNull IEvaluatedStateConstraintChecker> constraintCheckers = injector.getInstance(Key.get(new TypeLiteral<List<IEvaluatedStateConstraintChecker>>() {
		}));

		final List<IEvaluatedStateConstraintChecker> failedCheckers = new LinkedList<>();

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			// Apply initial state (store initial lateness etc)
			{
				// // Apply sequence manipulators
				final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(dataTransformer.getInitialSequences());
				final EvaluationState evaluationState = new EvaluationState();
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);
				for (final IEvaluationProcess.Phase phase : IEvaluationProcess.Phase.values()) {
					Assert.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
				}
				// Apply hard constraint checkers
				for (final IEvaluatedStateConstraintChecker checker : constraintCheckers) {
					checker.checkConstraints(rawSequences, fullSequences, evaluationState);
				}
			}
			// Apply to current
			{
				// // Apply sequence manipulators
				final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);
				final EvaluationState evaluationState = new EvaluationState();
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);
				for (final IEvaluationProcess.Phase phase : IEvaluationProcess.Phase.values()) {
					Assert.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
				}
				// Apply hard constraint checkers
				for (final IEvaluatedStateConstraintChecker checker : constraintCheckers) {
					if (!checker.checkConstraints(rawSequences, fullSequences, evaluationState)) {
						failedCheckers.add(checker);
					}
				}
			}
		}

		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	/**
	 * Returns null on success, or returns the failing evaluated state constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static void evaluateState(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences, BiConsumer<Injector, AnnotatedSolution> action) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());
		final Injector injector = evaluationTransformerUnit.getInjector();

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			// Apply initial state (store initial lateness etc)
			{
				// // Apply sequence manipulators
				final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);
				final EvaluationState evaluationState = new EvaluationState();
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);

				final AnnotatedSolution solution = new AnnotatedSolution(fullSequences, evaluationState);
				process.annotate(fullSequences, evaluationState, solution);

				action.accept(injector, solution);
			}
		}
	}

	public static Pair<LNGEvaluationTransformerUnit, List<IConstraintChecker>> getConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());
		final List<IConstraintChecker> constraintCheckers = evaluationTransformerUnit.getInjector().getInstance(Key.get(new TypeLiteral<List<IConstraintChecker>>() {
		}));

		return new Pair<>(evaluationTransformerUnit, constraintCheckers);
	}

	public static <T extends IConstraintChecker> T getChecker(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Class<T> cls) {
		final Pair<LNGEvaluationTransformerUnit, List<IConstraintChecker>> constraintCheckers = MicroTestUtils.getConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer());

		for (final IConstraintChecker checker : constraintCheckers.getSecond()) {
			if (cls.isInstance(checker)) {
				return cls.cast(checker);
			}

		}
		Assert.fail(String.format("Unable to find %s", cls.getName()));
		throw new IllegalStateException();
	}

	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static boolean evaluateLSOSequences(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());

		final LNGLSOOptimiserTransformerUnit unit = new LNGLSOOptimiserTransformerUnit(dataTransformer, IRunnerHook.STAGE_LSO, 0, dataTransformer.getUserSettings(), stage,
				dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(), dataTransformer.getHints());

		return true;
	}

	public static @NonNull VesselEventVisit findVesselEventVisit(@NonNull final VesselEvent vesselEvent, @NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Assert.assertNotNull(schedule);

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
					if (vesselEventVisit.getVesselEvent() == vesselEvent) {
						return vesselEventVisit;
					}

				}
			}
		}
		Assert.fail("Event not found");
		throw new IllegalStateException();
	}

	public static @NonNull SlotVisit findSlotVisit(@NonNull final Slot slot, @NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Assert.assertNotNull(schedule);

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					if (slotVisit.getSlotAllocation().getSlot() == slot) {
						return slotVisit;
					}

				}
			}
		}
		Assert.fail("Slot not found");
		throw new IllegalStateException();
	}
}