/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.Random;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.RestartingLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.ParallelLSOConstants;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;

public class LNGParameters_AnnealingSettingsModule extends AbstractModule {

	@NonNull
	private final AnnealingSettings settings;

	private int seed;

	public LNGParameters_AnnealingSettingsModule(final int seed, @NonNull final AnnealingSettings settings) {
		this.seed = seed;
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	private IThresholder provideThresholder(@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {
		// For now we are just going to generate a self-calibrating thresholder

		final IThresholder thresholder = new GeometricThresholder(new Random(seed), settings.getEpochLength(), settings.getInitialTemperature(), settings.getCooling());
		return thresholder;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long getRandomSeed() {
		return seed;
	}

	@Provides
	@Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)
	private boolean enableFourOpt2() {
		return true;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)
	private int getNumberOfIterations() {
		return settings.getIterations();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS)
	private int getNumberOfSolutionImprovementIterations() {
		return settings.getIterations();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)
	private boolean isLSORestarting() {
		return settings.isRestarting();
	}

	@Provides
	@Named(RestartingLocalSearchOptimiser.RESTART_ITERATIONS_THRESHOLD)
	private int getRestartIterationsThreshold() {
		return settings.getRestartIterationsThreshold();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.NEW_SIMILARITY_OPTIMISER)
	private boolean isUsingNewSimilarityModule() {
		return false;
	}

	@Provides
	@Named(ParallelLSOConstants.PARALLEL_LSO_BATCH_SIZE)
	private int getParallelLSOBatchSize() {
		return 100;
	}

	@Provides
	@Named(ParallelLSOConstants.PARALLEL_MOO_BATCH_SIZE)
	private int getParallelMOOBatchSize() {
		return 100;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.OPTIMISER_DEBUG_MODE)
	private boolean getDebugMode() {
		return false;
	}

}