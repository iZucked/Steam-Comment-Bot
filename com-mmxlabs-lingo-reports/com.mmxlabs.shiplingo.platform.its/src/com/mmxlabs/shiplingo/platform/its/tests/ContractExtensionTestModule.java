/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformerWrapper;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.fitness.NonOptionalSlotFitnessCoreFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SlotGroupCountConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponentProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.trading.integration.StandardContractBuilderFactory;
import com.mmxlabs.trading.integration.StandardContractTransformer;
import com.mmxlabs.trading.integration.TradingOptimiserModuleService;
import com.mmxlabs.trading.integration.TradingTransformerExtension;
import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponentProvider;

public class ContractExtensionTestModule extends AbstractModule {

	@Override
	protected void configure() {

		if (!Platform.isRunning()) {

			final TradingOptimiserModuleService mod = new TradingOptimiserModuleService();
			install(mod.requestModule());

			bind(IFitnessFunctionRegistry.class).toInstance(createFitnessFunctionRegistry());
			bind(IConstraintCheckerRegistry.class).toInstance(createConstraintCheckerRegistry());
			bind(IEvaluationProcessRegistry.class).toInstance(createEvaluationProcessRegistry());

			final List<ContractTransformer> transformers = new ArrayList<ContractTransformer>();
			final List<IBuilderExtensionFactory> buildFactories = new ArrayList<IBuilderExtensionFactory>();
			{
				final SimpleContractTransformer sct = new SimpleContractTransformer();
				final ContractTransformer transformer = new ContractTransformerWrapper(sct, sct.getContractEClasses());
				transformers.add(transformer);
				buildFactories.add(new StandardContractBuilderFactory());
			}
			{
				final StandardContractTransformer sct = new StandardContractTransformer();
				final ContractTransformer transformer = new ContractTransformerWrapper(sct, sct.getContractEClasses());
				transformers.add(transformer);
				buildFactories.add(new StandardContractBuilderFactory());
			}
			{
				final TradingTransformerExtension sct = new TradingTransformerExtension();
				final ContractTransformer transformer = new ContractTransformerWrapper(sct, Collections.<EClass>emptyList());
				transformers.add(transformer);
			}
			bind(TypeLiterals.iterable(ContractTransformer.class)).toInstance(transformers);

			bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toInstance(Collections.singleton(new TradingOptimiserModuleService()));
			bind(TypeLiterals.iterable(IBuilderExtensionFactory.class)).toInstance(buildFactories);
			bind(TypeLiterals.iterable(ICargoFitnessComponentProvider.class)).toInstance(Collections.singleton(new ProfitAndLossAllocationComponentProvider()));

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
		final FitnessFunctionRegistry fitnessFunctionRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();
		factory.setExternalComponentProviders(Collections.<ICargoFitnessComponentProvider> singleton(new ProfitAndLossAllocationComponentProvider()));
		fitnessFunctionRegistry.registerFitnessCoreFactory(factory);
		fitnessFunctionRegistry.registerFitnessCoreFactory(new NonOptionalSlotFitnessCoreFactory());

		return fitnessFunctionRegistry;
	}

	/**
	 * Creates a constraint checker registry used by this {@link OptimisationTransformer} instance.
	 * 
	 * @param data
	 * @return
	 */
	public IConstraintCheckerRegistry createConstraintCheckerRegistry() {
		final ConstraintCheckerRegistry constraintCheckerRegistry = new ConstraintCheckerRegistry();
		{
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(SchedulerConstants.DCP_orderedElementsProvider);
			constraintCheckerRegistry.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider);
			constraintCheckerRegistry.registerConstraintCheckerFactory(constraintFactory);
		}

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, SchedulerConstants.DCP_portSlotsProvider,
				SchedulerConstants.DCP_vesselProvider));

		constraintCheckerRegistry.registerConstraintCheckerFactory(new TravelTimeConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory(SchedulerConstants.DCP_portExclusionProvider, SchedulerConstants.DCP_vesselProvider,
				SchedulerConstants.DCP_portProvider));

		constraintCheckerRegistry.registerConstraintCheckerFactory(new VirtualVesselConstraintCheckerFactory(SchedulerConstants.DCP_vesselProvider, SchedulerConstants.DCP_portSlotsProvider));

		constraintCheckerRegistry.registerConstraintCheckerFactory(new SlotGroupCountConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new TimeSortConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, SchedulerConstants.DCP_portSlotsProvider,
				SchedulerConstants.DCP_vesselProvider));

		return constraintCheckerRegistry;
	}
}
