/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator.MoveResult;
import com.mmxlabs.scheduler.optimiser.lso.guided.HintManager;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.FitnessCalculator;

public class ActionableSetMover {

	@Inject
	@NonNull
	private ISequencesManipulator sequencesManipulator;

	@Inject
	@NonNull
	private List<IConstraintChecker> constraintCheckers;

	@Inject
	@NonNull
	private GuidedMoveGenerator moveGenerator;

	private boolean failedInitialConstraintCheckers;

	@Inject
	private EvaluationHelper evaluationHelper;

	@Inject
	@NonNull
	private FitnessCalculator fitnessCalculator;

	@Inject
	public void initSearchProcesses(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialRawSequences) {

		// Apply sequence manipulators
		final IModifiableSequences currentFullSequences = new ModifiableSequences(initialRawSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		failedInitialConstraintCheckers = false;

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker.checkConstraints(currentFullSequences, null) == false) {
				failedInitialConstraintCheckers = true;
				break;
			}
		}

		@Nullable
		IEvaluationState evaluationState = evaluationHelper.evaluateSequence(currentFullSequences);
		if (evaluationState == null) {
			throw new IllegalStateException("Unable to evaluate initial state");
		}

		// now evaluate to store initial fitness state in fitness functions (side effect!)
		fitnessCalculator.evaluateSequencesFitness(currentFullSequences, evaluationState, null);

	}

	public ActionableSetJobState search(@NonNull ActionableSetJobState state, @NonNull ILookupManager stateManager, long seed) {

		IModifiableSequences rawSequences = new ModifiableSequences(state.getRawSequences());
		stateManager.createLookup(rawSequences);
		// String note = (String.format("s: %s move: %s", seed, move));
		String note = null;
		final Random random = new Random(seed);
		// For seed 0->4095 this will always return true, so kick it now it start introducing "more randomness"..
		random.nextBoolean();

		GuideMoveGeneratorOptions options = new GuideMoveGeneratorOptions();
		// Only return moves which do not increase lateness/capacity (NOTE - may still allow floating about)
		options.setCheckingMove(true);
		options.setExtendSearch(random.nextBoolean());
		options.setIgnoreUsedElements(random.nextBoolean());
		options.setStrictOptional(random.nextBoolean());
		options.setPermitPartialSegments(false);// random.nextBoolean());
		options.setInsertCanRemove(random.nextBoolean());
		options.setNum_tries(random.nextInt(10));

		final MoveResult p = moveGenerator.generateMove(rawSequences, stateManager, random, state.getUsedElements(), state.getMetrics(), options);
		if (p == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}
		final IMove move = p.getMove();
		HintManager mgr = p.getHintManager();
		// Make sure the generator was able to generate a move
		if (move == null || move instanceof INullMove) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}

		// Test move is valid against data.
		if (!move.validate(rawSequences)) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}

		// Update potential sequences
		move.apply(rawSequences);
		final String moveName = move.getClass().getName();

		// Apply sequence manipulators
		final IModifiableSequences currentFullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker.checkConstraints(currentFullSequences, changedResources) == false) {
				return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
			}
		}

		long @Nullable [] newMetrics = p.getMetrics();
		if (newMetrics == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);

		}
		@Nullable
		IEvaluationState evaluationState = evaluationHelper.evaluateSequence(currentFullSequences);
		if (evaluationState == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}

		long fitness = fitnessCalculator.evaluateSequencesFitness(currentFullSequences, evaluationState, move.getAffectedResources());

		ActionableSetJobState s = new ActionableSetJobState(rawSequences, fitness, newMetrics, ActionableSetJobState.Status.Pass, seed, note, state);
		mgr.getUsedElements().forEach(e -> s.addUsedElements(e));
		return s;
	}

}
