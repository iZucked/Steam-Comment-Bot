/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.OptimisationTransformer;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.SchedulerModule;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGTransformerModule extends AbstractModule {

	/**
	 */
	public static final String Parser_Commodity = "Commodity";
	/**
	 */
	public static final String Parser_BaseFuel = "BaseFuel";
	/**
	 */
	public static final String Parser_Charter = "Charter";

	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	private final LNGScenarioModel scenario;

	private final OptimiserSettings optimiserSettings;

	/**
	 */
	public LNGTransformerModule(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings) {
		this.scenario = scenario;
		this.optimiserSettings = optimiserSettings;
	}

	@Override
	protected void configure() {
		install(new OptimiserCoreModule());
		install(new ScheduleBuilderModule());
		install(new SequencesManipulatorModule());
		install(new SchedulerModule());

		bind(LNGScenarioModel.class).toInstance(scenario);
		bind(OptimiserSettings.class).toInstance(optimiserSettings);

		// Parser for each section
		final SeriesParser commodityParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_Commodity)).toInstance(commodityParser);
		final SeriesParser charterParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_Charter)).toInstance(charterParser);
		final SeriesParser baseFuelParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_BaseFuel)).toInstance(baseFuelParser);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(DateAndCurveHelper.class).in(Singleton.class);

		bind(ModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		
		bind(IVolumeAllocator.class).to(UnconstrainedVolumeAllocator.class).in(Singleton.class);

		bind(VoyagePlanOptimiser.class);

		bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);
		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
			bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
			bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		}

	}

	@Provides
	IVoyagePlanOptimiser provideVoyagePlanOptimiser(final VoyagePlanOptimiser delegate) {
		final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
		return cachingVoyagePlanOptimiser;
	}

	@Provides
	@Singleton
	IOptimisationData provideOptimisationData(final LNGScenarioTransformer lngScenarioTransformer, final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {
		final IOptimisationData optimisationData = lngScenarioTransformer.createOptimisationData(modelEntityMap);

		return optimisationData;
	}

	// @Provides
	// @Singleton
	// /**
	// * Utility method for getting the current optimisation settings from this scenario. TODO maybe put this in another file/model somewhere else.
	// *
	// * @return
	// */
	// OptimiserSettings getOptimisationSettings(LNGScenarioModel rootObject) {
	// final ParametersModel om = rootObject.getParametersModel();
	// if (om != null) {
	// // select settings
	// final OptimiserSettings x = om.getActiveSetting();
	// if (x != null)
	// return x;
	// }
	// // if (defaultSettings == null) {
	// OptimiserSettings defaultSettings = ScenarioUtils.createDefaultSettings();
	// if (om != null) {
	// om.getSettings().add(defaultSettings);
	// om.setActiveSetting(defaultSettings);
	// }
	// // }
	// return defaultSettings;
	// }

	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideInitialSequences(final IOptimisationTransformer optimisationTransformer, final IOptimisationData data, final ModelEntityMap modelEntityMap) {

		final ISequences sequences = optimisationTransformer.createInitialSequences(data, modelEntityMap);

		return sequences;
	}
}
