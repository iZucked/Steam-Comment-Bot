/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.chain;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.OptimiserContextModule;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.scheduler.optimiser.lso.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.lso.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveHelperImpl;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.MoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGActionPlanModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new OptimiserContextModule());

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		}
		install(new FitnessFunctionInstantiatorModule());

		// install(new LocalSearchOptimiserModule());
		// install(new LinearFitnessEvaluatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);

		bind(InsertCargoVesselMoveHandler.class).in(Singleton.class);
		bind(InsertDESPurchaseMoveHandler.class).in(Singleton.class);
		bind(InsertFOBSaleMoveHandler.class).in(Singleton.class);
		bind(MoveHandlerHelper.class).in(Singleton.class);
		bind(RemoveSlotMoveHandler.class).in(Singleton.class);
		bind(RemoveCargoMoveHandler.class).in(Singleton.class);
		bind(SwapCargoVesselMoveHandler.class).in(Singleton.class);
		bind(SwapSlotMoveHandler.class).in(Singleton.class);

		bind(GuidedMoveHelperImpl.class).in(Singleton.class);
		bind(IGuidedMoveHelper.class).to(GuidedMoveHelperImpl.class);
		bind(MoveHelper.class).in(Singleton.class);

		bind(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
		bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class);
	}

	@Provides
	// @Singleton
	private IFitnessCombiner createFitnessCombiner(@Named(LinearFitnessEvaluatorModule.LINEAR_FITNESS_WEIGHTS_MAP) final Map<String, Double> weightsMap) {

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);
		return combiner;
	}
	//
	// @Provides
	// @Singleton
	// private IMoveGenerator provideMoveGenerator(final ConstrainedMoveGenerator normalMoveGenerator, @Named(LocalSearchOptimiserModule.RANDOM_SEED) long seed) {
	//
	// final CompoundMoveGenerator moveGenerator = new CompoundMoveGenerator();
	//
	// moveGenerator.addGenerator(normalMoveGenerator, 1);
	// moveGenerator.setRandom(new Random(seed));
	//
	// return moveGenerator;
	// }
	//
	// @Provides
	// @Singleton
	// private ConstrainedMoveGenerator provideConstrainedMoveGenerator(final Injector injector, final IOptimisationContext context, @Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {
	//
	// final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator(context);
	// cmg.setRandom(new Random(seed));
	// injector.injectMembers(cmg);
	// // cmg.init();
	// return cmg;
	// }
	//
	// @Provides
	// // @Singleton
	// private InstrumentingMoveGenerator provideInstrumentingMoveGenerator(final IMoveGenerator moveGenerator) {
	//
	// final InstrumentingMoveGenerator instrumentingMoveGenerator = LocalSearchOptimiserModule.instrumenting ? new InstrumentingMoveGenerator(moveGenerator, true // profile moves (true) or just rate
	// // (false)
	// , false // don't log moves to file
	// ) : null;
	// return instrumentingMoveGenerator;
	//
	// }

}
