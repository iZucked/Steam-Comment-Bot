/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.emf.optimisationsettings;

import java.util.ArrayList;
import java.util.List;

import scenario.optimiser.Constraint;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimisationSettings;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorUtil;

/**
 * Utility for taking an OptimisationSettings from the EMF and starting an optimiser accordingly. At the moment, it's pretty much 
 * just what was in TestUtils.
 * @author hinton
 *
 */
public class OptimisationTransformer {
	private OptimisationSettings settings;

	public OptimisationTransformer(OptimisationSettings settings) {
		this.settings = settings;
	}
	
	public IOptimisationContext<ISequenceElement> createOptimisationContext(IOptimisationData<ISequenceElement> data) {
		ISequences<ISequenceElement> sequences = createInitialSequences(data);
		IConstraintCheckerRegistry checkerRegistry = createConstraintCheckerRegistry();
		IFitnessFunctionRegistry componentRegistry = createFitnessFunctionRegistry();
		List<String> checkers = getEnabledConstraintNames();
		List<String> components = getEnabledFitnessFunctionNames();
		return new OptimisationContext<ISequenceElement>(data, sequences, components, componentRegistry, checkers, checkerRegistry);
	}

	public Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> createOptimiserAndContext(IOptimisationData<ISequenceElement> data) {
		IOptimisationContext<ISequenceElement> context = createOptimisationContext(data);
		
		LSOConstructor lsoConstructor = new LSOConstructor((LSOSettings) settings);
		
		return new Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>>
			(context, lsoConstructor.buildOptimiser(context,
				SequencesManipulatorUtil.createDefaultSequenceManipulators(data)
			));
	}
	
	private List<String> getEnabledConstraintNames() {
		List<String> result = new ArrayList<String>();
		
		for (Constraint c : settings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}
		
		return result;
	}
	
	private List<String> getEnabledFitnessFunctionNames() {
		List<String> result = new ArrayList<String>();
		
		for (Objective o : settings.getObjectives()) {
			if (o.getWeight() > 0) {
				result.add(o.getName());
			}
		}
		
		return result;	
	}

	/**
	 * Creates a fitness function registry
	 * @param data
	 * @return
	 */
	public IFitnessFunctionRegistry createFitnessFunctionRegistry() {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	/**
	 * Creates a constraint checker registry
	 * @param data
	 * @return
	 */
	public IConstraintCheckerRegistry createConstraintCheckerRegistry() {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		{
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
					SchedulerConstants.DCP_orderedElementsProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(
					SchedulerConstants.DCP_resourceAllocationProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}

		constraintRegistry.registerConstraintCheckerFactory(
				new PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider,
						SchedulerConstants.DCP_vesselProvider));
		
		constraintRegistry.registerConstraintCheckerFactory(
				new TravelTimeConstraintCheckerFactory());
		
		constraintRegistry.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory(
				SchedulerConstants.DCP_portExclusionProvider, 
				SchedulerConstants.DCP_vesselProvider, 
				SchedulerConstants.DCP_portProvider));	
		
		return constraintRegistry;
	}

	/**
	 * Create initial sequences; currently random, ideally will use the optimisation settings.
	 * @param data
	 * @return
	 */
	public ISequences<ISequenceElement> createInitialSequences(
			IOptimisationData<ISequenceElement> data) {
		//Create the sequenced constraint checkers here
		IConstraintCheckerRegistry registry = createConstraintCheckerRegistry();
		
		IInitialSequenceBuilder<ISequenceElement> builder = 
			new ConstrainedInitialSequenceBuilder<ISequenceElement>(
				registry.getConstraintCheckerFactories(getEnabledConstraintNames())		
			);

		return builder.createInitialSequences(data);
	}
	
}
