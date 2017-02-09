/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;

public class DefaultLocalSearchOptimiserTest {

	@Test
	public void testOptimise() {

		// Initialise random number generator
		final Random random = new Random(1);

		// Optimisation params
		final int numberOfIterations = 100;

		final IConstraintCheckerRegistry checkerRegistry = GeneralTestUtils.createConstraintCheckerRegistry();
		final IFitnessFunctionRegistry fitnessRegistry = GeneralTestUtils.createFitnessRegistry();
		final IEvaluationProcessRegistry evaluationProcessRegistry = GeneralTestUtils.createEvaluationProcessRegistry();
		final IEvaluatedStateConstraintCheckerRegistry evaluatedStateConstraintCheckerRegistry = GeneralTestUtils.createEvaluatedStateConstraintCheckerRegistry();

		final List<String> constraintCheckerNames = new ArrayList<String>(checkerRegistry.getConstraintCheckerNames());
		// final ConstraintCheckerInstantiator constraintCheckerInstantiator =
		// new ConstraintCheckerInstantiator();
		// final List<IConstraintChecker> constraintCheckers =
		// constraintCheckerInstantiator
		// .instantiateConstraintCheckers(checkerRegistry,
		// constraintCheckerNames);
		//
		final List<String> fitnessComponentNames = new ArrayList<String>(fitnessRegistry.getFitnessComponentNames());
		// final FitnessComponentInstantiator fitnessComponentInstantiator = new
		// FitnessComponentInstantiator();
		// final List<IFitnessComponent> fitnessComponents =
		// fitnessComponentInstantiator
		// .instantiateFitnesses(fitnessRegistry, fitnessComponentNames);

		final List<String> evaluationProcessNames = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluatedStateConstraintCheckerNames = new ArrayList<String>(evaluatedStateConstraintCheckerRegistry.getConstraintCheckerNames());

		//
		// final LinearSimulatedAnnealingFitnessEvaluator
		// fitnessEvaluator = TestUtils
		// .createLinearSAFitnessEvaluator(1, numberOfIterations,
		// fitnessComponents);
		// final IMoveGenerator moveGenerator = TestUtils
		// .createRandomMoveGenerator(random);
		//
		// final DefaultLocalSearchOptimiser lso = new
		// DefaultLocalSearchOptimiser();

		// lso.setNumberOfIterations(numberOfIterations);
		// lso.setSequenceManipulator(new NullSequencesManipulator());
		// lso.setMoveGenerator(moveGenerator);
		// lso.setFitnessEvaluator(fitnessEvaluator);
		// lso.setConstraintCheckers(constraintCheckers);

		// Create an initial set of sequences.

		final IResource r1 = OptimiserTestUtil.makeResource("r1");
		final IResource r2 = OptimiserTestUtil.makeResource("r2");

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4), r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences sequences = new ModifiableSequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final OptimisationData data = new OptimisationData();

		final OptimisationContext context = new OptimisationContext(data, sequences, fitnessComponentNames, fitnessRegistry, constraintCheckerNames, checkerRegistry, evaluationProcessNames,
				evaluationProcessRegistry, evaluatedStateConstraintCheckerNames, evaluatedStateConstraintCheckerRegistry);

		final IOptimiserProgressMonitor monitor = new SystemOutProgressMonitor();

		final LocalSearchOptimiser lso = GeneralTestUtils.buildOptimiser(context, data, random, numberOfIterations, 1, monitor);
		lso.setLookupManager(new ILookupManager() {
			@Override
			public void createLookup(@NonNull ISequences sequences) {

			}

			@Override
			public @Nullable Pair<@Nullable IResource, @NonNull Integer> lookup(@NonNull ISequenceElement element) {
				return null;
			}

			@Override
			public @NonNull ISequences getRawSequences() {
				throw new IllegalStateException();
			}
		});
		// Perform the optimisation
		lso.optimise(context);

		// TODO: Validate run -- use progress monitor output?
	}

}
