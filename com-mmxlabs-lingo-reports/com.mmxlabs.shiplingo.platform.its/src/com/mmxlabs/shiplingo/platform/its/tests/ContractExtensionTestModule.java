/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
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
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SlotGroupCountConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponentProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.SchedulerComponentsInjectorService;
import com.mmxlabs.trading.integration.TradingOptimiserModuleService;
import com.mmxlabs.trading.integration.factories.EntityTransformerExtensionFactory;
import com.mmxlabs.trading.integration.factories.StandardContractTransformerExtensionFactory;
import com.mmxlabs.trading.integration.factories.TradingExporterExtensionFactory;
import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponentProvider;

public class ContractExtensionTestModule extends AbstractModule {

	@Override
	protected void configure() {

		if (!Platform.isRunning()) {

			bind(IFitnessFunctionRegistry.class).toInstance(createFitnessFunctionRegistry());
			bind(IConstraintCheckerRegistry.class).toInstance(createConstraintCheckerRegistry());
			bind(IEvaluationProcessRegistry.class).toInstance(createEvaluationProcessRegistry());

			final List<IOptimiserInjectorService> injectorServices = Lists.newArrayList(new SchedulerComponentsInjectorService(), new TradingOptimiserModuleService());

			bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toInstance(injectorServices);
			bind(TypeLiterals.iterable(ICargoFitnessComponentProvider.class)).toInstance(Collections.singleton(new ProfitAndLossAllocationComponentProvider()));

			final List<IBuilderExtensionFactory> builderExtensionFactories = new ArrayList<IBuilderExtensionFactory>();
			bind(TypeLiterals.iterable(IBuilderExtensionFactory.class)).toInstance(builderExtensionFactories);

			final List<ITransformerExtensionFactory> transformerExtensionFactories = new ArrayList<ITransformerExtensionFactory>();
			transformerExtensionFactories.add(new EntityTransformerExtensionFactory());
			transformerExtensionFactories.add(new SimpleContractTransformerFactory());
			transformerExtensionFactories.add(new StandardContractTransformerExtensionFactory());
			transformerExtensionFactories.add(new RestrictedElementsTransformerFactory());
			transformerExtensionFactories.add(new ShippingTypeRequirementTransformerFactory());
			bind(TypeLiterals.iterable(ITransformerExtensionFactory.class)).toInstance(transformerExtensionFactories);

			final List<IExporterExtensionFactory> exporterExtensionFactories = new ArrayList<IExporterExtensionFactory>();
			exporterExtensionFactories.add(new TradingExporterExtensionFactory());
			bind(TypeLiterals.iterable(IExporterExtensionFactory.class)).toInstance(exporterExtensionFactories);
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

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortTypeConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new TravelTimeConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new VirtualVesselConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new SlotGroupCountConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new TimeSortConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new RestrictedElementsConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new ContractCvConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new ShippingTypeRequirementConstraintCheckerFactory());

		return constraintCheckerRegistry;
	}
}
