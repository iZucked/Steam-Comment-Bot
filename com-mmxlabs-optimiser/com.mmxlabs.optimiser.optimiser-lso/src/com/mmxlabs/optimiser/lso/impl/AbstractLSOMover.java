/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
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
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

@NonNullByDefault
public abstract class AbstractLSOMover {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLSOMover.class);

	@Inject
	protected ISequencesManipulator sequencesManipulator;

	@Inject
	protected List<IConstraintChecker> constraintCheckers;

	@Inject
	protected List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers;

	@Inject
	protected List<IEvaluationProcess> evaluationProcesses;

	@Inject
	private IFitnessHelper fitnessHelper;

	@Inject
	private IFitnessCombiner fitnessCombiner;

	protected void initSearchProcesses(final ISequences initialRawSequences) {

		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = sequencesManipulator.createManipulatedSequences(initialRawSequences);

		final List<String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: initSearchProcesses", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(potentialFullSequences, null, messages)) {
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
			if (!checker.checkConstraints(initialRawSequences, potentialFullSequences, evaluationState)) {
			}
		}

		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && messages != null && !messages.isEmpty()) {
			messages.stream().forEach(LOG::debug);
		}
		// now evaluate
		long fitness = evaluateSequencesInTurn(potentialFullSequences, evaluationState, potentialFullSequences.getResources());

	}

	protected long evaluateSequencesInTurn(final ISequences sequences, final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

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

	public abstract ILSOJobState search(IModifiableSequences rawSequences, ILookupManager stateManager, Random random, IMoveGenerator moveGenerator, long seed,
			boolean failedInitialConstraintCheckers);

	public abstract List<IFitnessComponent> getFitnessComponents();
}
