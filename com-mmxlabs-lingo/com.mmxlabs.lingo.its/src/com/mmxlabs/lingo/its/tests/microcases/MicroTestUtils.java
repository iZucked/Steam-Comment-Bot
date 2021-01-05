/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.EndEvent;
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
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;

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
			if (!checker.checkConstraints(fullSequences, null, new ArrayList<>())) {
				failedCheckers.add(checker);
			}
		}
		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static List<IConstraintChecker> validateConstraintCheckersWithoutNewUnit(@NonNull final Injector injector, @NonNull final ISequences rawSequences) {

		final List<IConstraintChecker> constraintCheckers = injector.getInstance(Key.get(new TypeLiteral<List<IConstraintChecker>>() {
		}));
		final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);

		final List<IConstraintChecker> failedCheckers = new LinkedList<>();
		// // Apply sequence manipulators
		final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(fullSequences, null, new ArrayList<>())) {
				//checker.checkConstraints(fullSequences, null, new ArrayList<>());
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
	 * Returns null on success, or returns the failing evaluated state constraint
	 * checkers.
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
					Assertions.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
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
					Assertions.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
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
	 * Returns null on success, or returns the failing evaluated state constraint
	 * checkers.
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

	/**
	 * Returns null on success, or returns the failing evaluated state constraint
	 * checkers.
	 * 
	 * @param rawSequences
	 * @return metrics
	 */
	public static long[] evaluateMetrics(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());
		final Injector injector = evaluationTransformerUnit.getInjector();

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);

			// Apply initial state (store initial lateness etc)
			// // Apply sequence manipulators
			final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

			long[] metrics = evaluationHelper.evaluateState(rawSequences, fullSequences, null, false, false, null, null);

			return metrics;
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
		Assertions.fail(String.format("Unable to find %s", cls.getName()));
		throw new IllegalStateException();
	}

	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static boolean evaluateLSOSequences(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings(), false);

		final LNGLSOOptimiserTransformerUnit unit = new LNGLSOOptimiserTransformerUnit(dataTransformer, IRunnerHook.STAGE_LSO, 0, dataTransformer.getUserSettings(), stage,
				dataTransformer.getInitialSequences(), dataTransformer.getInitialSequences(), dataTransformer.getHints());

		return true;
	}

	public static @NonNull VesselEventVisit findVesselEventVisit(@NonNull final VesselEvent vesselEvent, @NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Assertions.assertNotNull(schedule);

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
		Assertions.fail("Event not found");
		throw new IllegalStateException();
	}

	public static @NonNull SlotVisit findSlotVisit(@NonNull final Slot slot, @NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Assertions.assertNotNull(schedule);

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
		Assertions.fail("Slot not found");
		throw new IllegalStateException();
	}

	public static @NonNull EndEvent findVesselEndEvent(@NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Assertions.assertNotNull(schedule);

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof EndEvent) {
					return (EndEvent) event;
				}
			}
		}
		Assertions.fail("End Event not found");
		throw new IllegalStateException();
	}
	
	/**
	 * Compares two given lists for equality, entry-by-entry, field-by-field. Uses myClass to determine fields. 
	 * Ignores fields with LingoIgnore annotation.
	 * @param <T>
	 * @param etalons
	 * @param generated
	 * @param myClass
	 * @return
	 */
	public static <T> boolean compareTLists(List<T> etalons, List<T> generated, Class<T> myClass) {
		if (etalons.size() != generated.size()) {
			return false;
		}
		
		int size = etalons.size();
		int strike = 0;
		final List<Field> fields = new ArrayList<Field>();
		for (final Field field : myClass.getFields()) {
			if (field.getAnnotation(LingoIgnore.class) != null) {
				continue;
			}
			fields.add(field);
		}
		
		for(final T gen : generated) {
			for (final T et : etalons) {
		
				boolean allFieldsEqual = true;
				for (final Field f : fields) {
					try {
						final Object genValue = f.get(gen);
						final Object etValue = f.get(et);
						if (!Objects.equal(genValue, etValue)) {
							allFieldsEqual = false;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (allFieldsEqual) {
					strike++;
				}
			}
		}
		
		return size == strike;
	}
	
	/**
	 * Locates given fileName JSON file at urlRoot and tries to return a list of given typeRef
	 * @param <T>
	 * @param urlRoot full path to the folder
	 * @param fileName JSON file name
	 * @param typeRef
	 * @return List of given typeRef
	 */
	public static <T> List<T> getEtalonTList(final String urlRoot, final String fileName, final TypeReference<List<T>> typeRef) {
		List<T> etalons = Collections.emptyList();
		final String urlString = String.format("%s/%s", urlRoot, fileName);
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		
		InputStream inputStream = null;
		
		try {
			inputStream = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			return Collections.emptyList();
		}
		
		if (inputStream != null) {
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			
			try {	
				etalons = objectMapper.readValue(inputStream, typeRef);
			} catch (final Exception e) {
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
		return etalons;
	}
}