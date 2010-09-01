package com.mmxlabs.jobcontroller.emf.optimisationsettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import scenario.optimiser.LSOSettings;
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
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.LegalInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.RandomInitialSequenceBuilder;

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
		List<String> checkers = createConstraintCheckerList(checkerRegistry);
		List<String> components = createFitnessComponentList(componentRegistry);
		return new OptimisationContext<ISequenceElement>(data, sequences, components, componentRegistry, checkers, checkerRegistry);
	}

	public Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> createOptimiserAndContext(IOptimisationData<ISequenceElement> data) {
		IOptimisationContext<ISequenceElement> context = createOptimisationContext(data);
		
		LSOConstructor lsoConstructor = new LSOConstructor((LSOSettings) settings);
		
		return new Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>>
			(context, lsoConstructor.buildOptimiser(context));
	}
	
	public List<String> createFitnessComponentList(IFitnessFunctionRegistry registry) {
		return new ArrayList<String>(registry.getFitnessComponentNames());
	}

	public List<String> createConstraintCheckerList(IConstraintCheckerRegistry registry) {
		return new ArrayList<String>(registry.getConstraintCheckerNames());
	}

	/**
	 * Creates a fitness function registry; currently doesn't use the settings
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
	 * Creates a constraint checker registry; ideally will use the EMF to decide which constraints are enabled, but currently just turns on the standard ones.
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

		return constraintRegistry;
	}

	/**
	 * Create initial sequences; currently random, ideally will use the optimisation settings.
	 * @param data
	 * @return
	 */
	public ISequences<ISequenceElement> createInitialSequences(
			IOptimisationData<ISequenceElement> data) {
		IInitialSequenceBuilder<ISequenceElement> builder = new LegalInitialSequenceBuilder<ISequenceElement>();
		//TODO add a parameter for this to the model, if we create some better initial schedule builders
		return builder.createInitialSequences(data);
	}
	
}
