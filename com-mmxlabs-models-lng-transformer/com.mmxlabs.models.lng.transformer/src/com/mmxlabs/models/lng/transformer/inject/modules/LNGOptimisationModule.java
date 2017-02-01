/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Random;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.OptimiserContextModule;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.movegenerators.impl.CompoundMoveGenerator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.moves.util.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGOptimisationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new OptimiserContextModule());

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		}
		install(new FitnessFunctionInstantiatorModule());

		install(new LocalSearchOptimiserModule());
		install(new LinearFitnessEvaluatorModule());

		bind(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
		bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class);

		bind(ILookupManager.class).to(LookupManager.class);

	}

	@Provides
	@Singleton
	private IMoveGenerator provideMoveGenerator(final ConstrainedMoveGenerator normalMoveGenerator) {

		final CompoundMoveGenerator moveGenerator = new CompoundMoveGenerator();

		moveGenerator.addGenerator(normalMoveGenerator, 1);

		return moveGenerator;
	}

	@Provides
	@Singleton
	private ConstrainedMoveGenerator provideConstrainedMoveGenerator(final Injector injector) {

		final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator();
		injector.injectMembers(cmg);
		// cmg.init();
		return cmg;
	}

	@Provides
	// @Singleton
	private InstrumentingMoveGenerator provideInstrumentingMoveGenerator(final IMoveGenerator moveGenerator) {

		final InstrumentingMoveGenerator instrumentingMoveGenerator = LocalSearchOptimiserModule.instrumenting ? new InstrumentingMoveGenerator(moveGenerator, true // profile moves (true) or just rate
		// (false)
				, false // don't log moves to file
		) : null;
		return instrumentingMoveGenerator;

	}

}
