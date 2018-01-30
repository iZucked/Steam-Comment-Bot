package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Collection;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.INullMove;

public class SimpleMultiObjectiveLSOMover extends AbstractLSOMover {

	@Inject
	IMultiObjectiveFitnessEvaluator multiObjectiveFitnessEvaluator;
	
	@Override
	public MultiObjectiveJobState search(@NonNull IModifiableSequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random, @NonNull IMoveGenerator moveGenerator, long seed,
			boolean failedInitialConstraintCheckers) {
		final IMove move = moveGenerator.generateMove(rawSequences, stateManager, random);
		String note = null;
		// Make sure the generator was able to generate a move
		if (move == null || move instanceof INullMove) {
			return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.Fail, null, seed, note);
		}

		// Test move is valid against data.
		if (!move.validate(rawSequences)) {
			return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.Fail, null, seed, note);
		}

		// Update potential sequences
		move.apply(rawSequences);

		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = new ModifiableSequences(rawSequences);
		sequencesManipulator.manipulate(potentialFullSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks until we accept a valid
			// solution
			final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();
			if (checker.checkConstraints(potentialFullSequences, changedResources) == false) {
				// Break out
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.Fail, null, seed, note);
			}
		}

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.Fail, null, seed, note);
			}
		}
		
		// Apply hard constraint checkers
		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			if (checker.checkConstraints(rawSequences, potentialFullSequences, evaluationState) == false) {
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.Fail, null, seed, note);
			}
		}

		
		// now evaluate
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(rawSequences, potentialFullSequences, evaluationState,
				move.getAffectedResources(), getFitnessComponents());

		return new MultiObjectiveJobState(rawSequences, potentialFullSequences, fitnesses, LSOJobStatus.Pass, evaluationState, seed, note);
	}
	
	public IMultiObjectiveFitnessEvaluator getMultiObjectiveFitnessEvaluator() {
		return multiObjectiveFitnessEvaluator;
	}
}
