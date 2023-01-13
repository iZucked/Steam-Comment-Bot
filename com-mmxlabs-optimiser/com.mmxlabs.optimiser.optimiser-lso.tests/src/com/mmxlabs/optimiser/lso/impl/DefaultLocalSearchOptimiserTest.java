/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.scenario.PhaseOptimisationData;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

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

		final List<String> constraintCheckerNames = new ArrayList<>(checkerRegistry.getConstraintCheckerNames());
		final List<String> fitnessComponentNames = new ArrayList<>(fitnessRegistry.getFitnessComponentNames());

		final List<String> evaluationProcessNames = new ArrayList<>(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluatedStateConstraintCheckerNames = new ArrayList<>(evaluatedStateConstraintCheckerRegistry.getConstraintCheckerNames());

		// Create an initial set of sequences.

		final IResource r1 = OptimiserTestUtil.makeResource("r1");
		final IResource r2 = OptimiserTestUtil.makeResource("r2");

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4), r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences sequences = new ModifiableSequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final OptimisationData data = new OptimisationData();

		final PhaseOptimisationData pData = new PhaseOptimisationData();

		final OptimisationContext context = new OptimisationContext(sequences, fitnessComponentNames, fitnessRegistry, constraintCheckerNames, checkerRegistry, evaluationProcessNames,
				evaluationProcessRegistry, evaluatedStateConstraintCheckerNames, evaluatedStateConstraintCheckerRegistry);

		final IOptimiserProgressMonitor monitor = new SystemOutProgressMonitor();

		final Pair<SingleThreadLocalSearchOptimiser, Injector> p  = GeneralTestUtils.buildOptimiser(context, data, pData, random, numberOfIterations, 1, monitor);
		SingleThreadLocalSearchOptimiser lso = p.getFirst();
		Injector parent = p.getSecond();
		
		ILookupManager nullLookupManager =	new ILookupManager() {
			@Override
			public void createLookup(@NonNull ISequences sequences) {

			}

			@Override
			public void updateLookup(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources) {

			}

			@Override
			public void updateLookup(@NonNull ISequences sequences, IResource... changedResources) {

			}

			@Override
			public @Nullable Pair<@Nullable IResource, @NonNull Integer> lookup(@NonNull ISequenceElement element) {
				return null;
			}

			@Override
			public @NonNull ISequences getRawSequences() {
				throw new IllegalStateException();
			}
		};
		
		Injector c = parent.createChildInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ILookupManager.class).toInstance(nullLookupManager);
				bind(ISequencesManipulator.class).to(NullSequencesManipulator.class);
				bind(boolean.class).annotatedWith(Names.named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)).toInstance(Boolean.FALSE);
				bind(int.class).annotatedWith(Names.named(SingleThreadLocalSearchOptimiser.RESTART_ITERATIONS_THRESHOLD)).toInstance(-1);
				
				
				ISequences seq = new ModifiableSequences(Collections.emptyList());
				bind(ISequences.class).annotatedWith(Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)).toInstance(seq);
//				bind(IFitnessCombiner.class).toInstance(((LinearSimulatedAnnealingFitnessEvaluator)lso.getFitnessEvaluator()).getFitnessCombiner());
			}

		});

		c.injectMembers(lso);
		
		lso.setRandom(random);
		// Perform the optimisation
		lso.optimise(context, null);

		// TODO: Validate run -- use progress monitor output?
	}

}
