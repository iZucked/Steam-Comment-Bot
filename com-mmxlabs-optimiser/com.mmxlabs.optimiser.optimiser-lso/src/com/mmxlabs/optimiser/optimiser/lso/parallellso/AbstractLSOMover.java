/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

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
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

public abstract class AbstractLSOMover {
	@Inject
	@NonNull
	protected ISequencesManipulator sequencesManipulator;

	@Inject
	@NonNull
	protected List<IConstraintChecker> constraintCheckers;

	@Inject
	@NonNull
	protected List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers;

	@Inject
	@NonNull
	protected List<IEvaluationProcess> evaluationProcesses;

	@Inject
	@NonNull
	private List<IFitnessComponent> fitnessComponents;

	@Inject
	@NonNull
	private IFitnessHelper fitnessHelper;

	@Inject
	@NonNull
	private IFitnessCombiner fitnessCombiner;
	
	@Inject
	public void initSearchProcesses(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialRawSequences) {

		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = new ModifiableSequences(initialRawSequences);
		sequencesManipulator.manipulate(potentialFullSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker.checkConstraints(potentialFullSequences, null) == false) {
			}
		}
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
				assert false;
			}
		}
		
		// Apply hard constraint checkers
		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			if (checker.checkConstraints(initialRawSequences, potentialFullSequences, evaluationState) == false) {
			}
		}
		
		// now evaluate
		long fitness = evaluateSequencesInTurn(potentialFullSequences, evaluationState, potentialFullSequences.getResources());

	}
	protected long evaluateSequencesInTurn(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, getFitnessComponents())) {
				return Long.MAX_VALUE;
			}
		} else {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, getFitnessComponents(), affectedResources)) {
				return Long.MAX_VALUE;
			}
		}

		return fitnessCombiner.calculateFitness(getFitnessComponents());
	}

	public abstract ILSOJobState search(@NonNull IModifiableSequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random, @NonNull IMoveGenerator moveGenerator, long seed, boolean failedInitialConstraintCheckers);
	
	public List<IFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}
	public void setFitnessComponents(List<IFitnessComponent> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}
}
