/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
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
	protected static final Logger LOG = LoggerFactory.getLogger(ActionableSetMover.class);
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
	public void initSearchProcesses(final @NonNull @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialRawSequences) {

		// Apply sequence manipulators
		final IModifiableSequences currentFullSequences = new ModifiableSequences(initialRawSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		failedInitialConstraintCheckers = false;
		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: initSearchProcesses", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(currentFullSequences, null, messages)) {
				failedInitialConstraintCheckers = true;
				break;
			}
		}
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
			messages.stream().forEach(LOG::debug);

		@Nullable
		final IEvaluationState evaluationState = evaluationHelper.evaluateSequence(currentFullSequences);
		if (evaluationState == null) {
			throw new IllegalStateException("Unable to evaluate initial state");
		}

		// now evaluate to store initial fitness state in fitness functions (side effect!)
		fitnessCalculator.evaluateSequencesFitness(currentFullSequences, evaluationState, null);

	}

	public ActionableSetJobState search(@NonNull final ActionableSetJobState state, final long seed) {

		final IModifiableSequences rawSequences = new ModifiableSequences(state.getRawSequences());
		// String note = (String.format("s: %s move: %s", seed, move));
		final String note = null;
		final Random random = new Random(seed);
		// For seed 0->4095 this will always return true, so kick it now it start introducing "more randomness"..
		random.nextBoolean();

		final GuideMoveGeneratorOptions options = new GuideMoveGeneratorOptions();
		// Only return moves which do not increase lateness/capacity (NOTE - may still allow floating about)
		options.setCheckingMove(true);
		options.setExtendSearch(random.nextBoolean());
		options.setIgnoreUsedElements(random.nextBoolean());
		options.setStrictOptional(random.nextBoolean());
		options.setPermitPartialSegments(false);// random.nextBoolean());
		options.setInsertCanRemove(random.nextBoolean());
		options.setNum_tries(random.nextInt(10));

		final MoveResult p = moveGenerator.generateMove(rawSequences, random, state.getUsedElements(), state.getMetrics(), options);
		if (p == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}
		final IMove move = p.getMove();
		final HintManager mgr = p.getHintManager();
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

		// Apply sequence manipulators
		final IModifiableSequences currentFullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();

		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: search", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(currentFullSequences, changedResources, messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
			}
		}

		final long @Nullable [] newMetrics = p.getMetrics();
		if (newMetrics == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);

		}
		@Nullable
		final IEvaluationState evaluationState = evaluationHelper.evaluateSequence(currentFullSequences);
		if (evaluationState == null) {
			return new ActionableSetJobState(rawSequences, Long.MAX_VALUE, null, ActionableSetJobState.Status.Fail, seed, note, state);
		}

		final long fitness = fitnessCalculator.evaluateSequencesFitness(currentFullSequences, evaluationState, move.getAffectedResources());

		final ActionableSetJobState s = new ActionableSetJobState(rawSequences, fitness, newMetrics, ActionableSetJobState.Status.Pass, seed, note, state);
		mgr.getUsedElements().forEach(e -> s.addUsedElements(e));
		return s;
	}

}
