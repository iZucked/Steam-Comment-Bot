/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.INullMove;

public class LSOMover extends AbstractLSOMover {

	public LSOJobState search(@NonNull IModifiableSequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random, @NonNull IMoveGenerator moveGenerator, long seed,
			boolean failedInitialConstraintCheckers) {
		final IMove move = moveGenerator.generateMove(rawSequences, stateManager, random);

		// Make sure the generator was able to generate a move
		if (move == null || move instanceof INullMove) {
			return new LSOJobState(rawSequences, null, Long.MAX_VALUE, LSOJobStatus.NullMoveFail, null, seed, move, null);
		}

		// Test move is valid against data.
		if (!move.validate(rawSequences)) {
			return new LSOJobState(rawSequences, null, Long.MAX_VALUE, LSOJobStatus.CannotValidateFail, null, seed, move, null);
		}

		move.apply(rawSequences);

		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = new ModifiableSequences(rawSequences);
		sequencesManipulator.manipulate(potentialFullSequences);

		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: search", this.getClass().getName()));
		// check hard constraints
		for (final IConstraintChecker checker : constraintCheckers) {
			// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks until we accept a valid
			// solution
			final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();
			if (!checker.checkConstraints(potentialFullSequences, changedResources, messages)) {
				if(!messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				return new LSOJobState(rawSequences, null, Long.MAX_VALUE, LSOJobStatus.ConstraintFail, null, seed, move, checker);
			}
		}

		final IEvaluationState evaluationState = new EvaluationState();

		// check against evaluation processes
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
				return new LSOJobState(rawSequences, null, Long.MAX_VALUE, LSOJobStatus.EvaluationProcessFail, null, seed, move, evaluationProcess);
			}
		}

		// check against evaluated constraints
		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			if (!checker.checkConstraints(rawSequences, potentialFullSequences, evaluationState)) {
				return new LSOJobState(rawSequences, null, Long.MAX_VALUE, LSOJobStatus.EvaluatedConstraintFail, null, seed, move, checker);
			}
		}

		// now evaluate
		long fitness = evaluateSequencesInTurn(potentialFullSequences, evaluationState, move.getAffectedResources());

		return new LSOJobState(rawSequences, potentialFullSequences, fitness, LSOJobStatus.Pass, evaluationState, seed, move, null);

	}

}
