/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.BasicSlotPNLExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IPostExportProcessorFactory;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.util.OptimisationTransformer;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.EvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SlotGroupCountConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SpotToSpotConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VesselEventConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcessFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class TransformerITSOptimiserInjectorService implements IOptimiserInjectorService {

	@Override
	@Nullable
	public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
		return null;
	}

	@Override
	@Nullable
	public Module requestModule(final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
		if (moduleType == ModuleType.Module_LNGTransformerModule) {

			return new AbstractModule() {

				@Override
				protected void configure() {
					if (!Platform.isRunning()) {
						final List<IBuilderExtensionFactory> builderExtensionFactories = new ArrayList<IBuilderExtensionFactory>();
						bind(TypeLiterals.iterable(IBuilderExtensionFactory.class)).toInstance(builderExtensionFactories);

						final List<ITransformerExtensionFactory> transformerExtensionFactories = new ArrayList<ITransformerExtensionFactory>();
						transformerExtensionFactories.add(new EntityTransformerExtensionFactory());
						transformerExtensionFactories.add(new SimpleContractTransformerFactory());
						// transformerExtensionFactories.add(new StandardContractTransformerExtensionFactory());
						transformerExtensionFactories.add(new RestrictedElementsTransformerFactory());
						transformerExtensionFactories.add(new ShippingTypeRequirementTransformerFactory());
						bind(TypeLiterals.iterable(ITransformerExtensionFactory.class)).toInstance(transformerExtensionFactories);

						// TODO - Remove once we have explicit use of export stage
						final List<IExporterExtensionFactory> exporterExtensionFactories = new ArrayList<IExporterExtensionFactory>();
						exporterExtensionFactories.add(new TradingExporterExtensionFactory());
						exporterExtensionFactories.add(new BasicSlotPNLExporterExtensionFactory());
						bind(TypeLiterals.iterable(IExporterExtensionFactory.class)).toInstance(exporterExtensionFactories);

						final List<IPostExportProcessorFactory> postExportExtensionFactories = new ArrayList<IPostExportProcessorFactory>();
						bind(TypeLiterals.iterable(IPostExportProcessorFactory.class)).toInstance(postExportExtensionFactories);
					}
				}
			};
		} else if (moduleType == ModuleType.Module_Evaluation) {
			return new AbstractModule() {

				@Override
				protected void configure() {

					if (!Platform.isRunning()) {
						bind(IConstraintCheckerRegistry.class).toInstance(createConstraintCheckerRegistry());
						bind(IEvaluatedStateConstraintCheckerRegistry.class).toInstance(createEvaluatedStateConstraintCheckerRegistry());
						bind(IEvaluationProcessRegistry.class).toInstance(createEvaluationProcessRegistry());
					}
					// This bit is always needed for LiNGO ITS
					// TODO: Split this into two classes, one for transformer ITS and one for LiNGO ITS
					// bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
				}
			};
		} else if (moduleType == ModuleType.Module_InitialSolution) {
			return new AbstractModule() {

				@Override
				protected void configure() {
					if (!Platform.isRunning()) {
						bind(IFitnessFunctionRegistry.class).toInstance(createFitnessFunctionRegistry());
					}
				}
			};
		} else if (moduleType == ModuleType.Module_Optimisation) {
			return new AbstractModule() {

				@Override
				protected void configure() {
					if (!Platform.isRunning()) {
						bind(IFitnessFunctionRegistry.class).toInstance(createFitnessFunctionRegistry());
					}
				}
			};
		} else if (moduleType == ModuleType.Module_Export) {
			return new AbstractModule() {

				@Override
				protected void configure() {

					final List<IExporterExtensionFactory> exporterExtensionFactories = new ArrayList<IExporterExtensionFactory>();
					exporterExtensionFactories.add(new TradingExporterExtensionFactory());
					exporterExtensionFactories.add(new BasicSlotPNLExporterExtensionFactory());
					bind(TypeLiterals.iterable(IExporterExtensionFactory.class)).toInstance(exporterExtensionFactories);

					final List<IPostExportProcessorFactory> postExportExtensionFactories = new ArrayList<IPostExportProcessorFactory>();
					bind(TypeLiterals.iterable(IPostExportProcessorFactory.class)).toInstance(postExportExtensionFactories);

				}
			};
		}
		return null;
	}

	private IEvaluationProcessRegistry createEvaluationProcessRegistry() {
		final EvaluationProcessRegistry registry = new EvaluationProcessRegistry();

		registry.registerEvaluationProcessFactory(new SchedulerEvaluationProcessFactory());

		return registry;
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
		// factory.setExternalComponentProviders(Collections.<ICargoFitnessComponentProvider> singleton(new ProfitAndLossAllocationComponentProvider()));
		fitnessFunctionRegistry.registerFitnessCoreFactory(factory);
		fitnessFunctionRegistry.registerFitnessCoreFactory(new NonOptionalSlotFitnessCoreFactory());

		return fitnessFunctionRegistry;
	}

	public IEvaluatedStateConstraintCheckerRegistry createEvaluatedStateConstraintCheckerRegistry() {
		final EvaluatedStateConstraintCheckerRegistry registry = new EvaluatedStateConstraintCheckerRegistry();
		return registry;
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
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory();
			constraintCheckerRegistry.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory();
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
		constraintCheckerRegistry.registerConstraintCheckerFactory(new PortCvCompatibilityConstraintCheckerFactory());
		constraintCheckerRegistry.registerConstraintCheckerFactory(new SpotToSpotConstraintCheckerFactory());
		constraintCheckerRegistry.registerConstraintCheckerFactory(new ShippingTypeRequirementConstraintCheckerFactory());

		constraintCheckerRegistry.registerConstraintCheckerFactory(new RoundTripVesselPermissionConstraintCheckerFactory());
		constraintCheckerRegistry.registerConstraintCheckerFactory(new AllowedVesselPermissionConstraintCheckerFactory());
		constraintCheckerRegistry.registerConstraintCheckerFactory(new FOBDESCompatibilityConstraintCheckerFactory());
		constraintCheckerRegistry.registerConstraintCheckerFactory(new VesselEventConstraintCheckerFactory());

		return constraintCheckerRegistry;
	}

}