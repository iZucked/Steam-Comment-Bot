/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.multiobjective.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.impl.AbstractLSOMover;
import com.mmxlabs.optimiser.lso.impl.LSOJobStatus;
import com.mmxlabs.optimiser.lso.multiobjective.modules.MultiObjectiveOptimiserModule;

@NonNullByDefault
public class SimpleMultiObjectiveLSOMover extends AbstractLSOMover {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleMultiObjectiveLSOMover.class);

	@Inject
	private IMultiObjectiveFitnessEvaluator multiObjectiveFitnessEvaluator;

	@Inject
	@Named(MultiObjectiveOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_NAMES)
	private List<String> fitnessComponentsNames;

	private List<IFitnessComponent> fitnessComponents = new LinkedList<>();

	@Inject
	public void initSearchProcesses(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialRawSequences) {
		// Filter the list of fitness components from the evaluator, but keep the same
		// instances!
		// TODO: Maybe the fitness evaluator should be doing this?
		this.fitnessComponents = MultiObjectiveOptimiserModule.getMultiObjectiveFitnessComponents(multiObjectiveFitnessEvaluator, fitnessComponentsNames);
		super.initSearchProcesses(initialRawSequences);
	}

	@Override
	public MultiObjectiveJobState search(IModifiableSequences rawSequences, ILookupManager stateManager, Random random, IMoveGenerator moveGenerator, long seed,
			boolean failedInitialConstraintCheckers) {
		final IMove move = moveGenerator.generateMove(rawSequences, stateManager, random);

		// Make sure the generator was able to generate a move
		if (move == null || move instanceof INullMove) {
			return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.NullMoveFail, null, seed, move, null);
		}

		// Test move is valid against data.
		if (!move.validate(rawSequences)) {
			return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.CannotValidateFail, null, seed, move, null);
		}

		// Update potential sequences
		move.apply(rawSequences);

		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = new ModifiableSequences(rawSequences);
		sequencesManipulator.manipulate(potentialFullSequences);
		final List<String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: search", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			// For constraint checker changed resources functions, if initial solution is
			// invalid, we want to always perform a full constraint checker set of checks
			// until we accept a valid
			// solution
			final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();
			if (!checker.checkConstraints(potentialFullSequences, changedResources, messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				// Break out
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.ConstraintFail, null, seed, move, checker);
			}
		}

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.EvaluationProcessFail, null, seed, move, null);
			}
		}

		// Apply hard constraint checkers
		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			if (!checker.checkConstraints(rawSequences, potentialFullSequences, evaluationState)) {
				return new MultiObjectiveJobState(rawSequences, null, null, LSOJobStatus.EvaluatedConstraintFail, null, seed, move, checker);
			}
		}

		// now evaluate
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(rawSequences, potentialFullSequences, evaluationState,
				move.getAffectedResources(), getFitnessComponents());

		return new MultiObjectiveJobState(rawSequences, potentialFullSequences, fitnesses, LSOJobStatus.Pass, evaluationState, seed, move, null);
	}

	public IMultiObjectiveFitnessEvaluator getMultiObjectiveFitnessEvaluator() {
		return multiObjectiveFitnessEvaluator;
	}

	@Override
	public List<IFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}
}
