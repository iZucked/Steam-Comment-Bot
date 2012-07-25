/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests;

import java.util.Collections;

import org.eclipse.core.runtime.Platform;
import org.ops4j.peaberry.ServiceRegistry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformerWrapper;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

public class ContractExtensionTestModule extends AbstractModule {

	@Override
	protected void configure() {


		if (!Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toInstance(createFitnessFunctionRegistry());
			bind(IConstraintCheckerRegistry.class).toInstance(createConstraintCheckerRegistry());
			bind(IEvaluationProcessRegistry.class).toInstance(createEvaluationProcessRegistry());

		final SimpleContractTransformer sct = new SimpleContractTransformer();

		final ContractTransformer transformer = new ContractTransformerWrapper(sct, sct.getContractEClasses());
		bind(TypeLiterals.iterable(ContractTransformer.class)).toInstance(Collections.singleton(transformer));
		}
	}

	private IEvaluationProcessRegistry createEvaluationProcessRegistry() {
		return new EvaluationProcessRegistry();
	}

	/**
	 * Creates a fitness function registry used by this {@link OptimisationTransformer} instance.
	 * 
	 * @param data
	 * @return
	 */
	public IFitnessFunctionRegistry createFitnessFunctionRegistry() {
		FitnessFunctionRegistry fitnessFunctionRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessFunctionRegistry.registerFitnessCoreFactory(factory);
		return fitnessFunctionRegistry;
	}

	/**
	 * Creates a constraint checker registry used by this {@link OptimisationTransformer} instance.
	 * 
	 * @param data
	 * @return
	 */
	public IConstraintCheckerRegistry createConstraintCheckerRegistry() {
		ConstraintCheckerRegistry constraintCheckerRegistry = new ConstraintCheckerRegistry();
		{
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(SchedulerConstants.DCP_orderedElementsProvider);
			constraintCheckerRegistry.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider);
			constraintCheckerRegistry.registerConstraintCheckerFactory(constraintFactory);
		}

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, SchedulerConstants.DCP_vesselProvider));

		constraintCheckerRegistry.registerConstraintCheckerFactory(new TravelTimeConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory(SchedulerConstants.DCP_portExclusionProvider, SchedulerConstants.DCP_vesselProvider,
				SchedulerConstants.DCP_portProvider));

		return constraintCheckerRegistry;
	}
}
