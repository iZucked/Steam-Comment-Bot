package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.List;
import java.util.Random;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;

public class SchedulerModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	private ConstrainedMoveGenerator provideConstrainedMoveGenerator(final Injector injector, final IOptimisationContext context, @Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {

		final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator(context);
		cmg.setRandom(new Random(seed));
		injector.injectMembers(cmg);
		cmg.init();
		return cmg;
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(@Named(OptimiserCoreModule.ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames,
			final IConstraintCheckerRegistry constraintCheckerRegistry) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(constraintCheckerRegistry.getConstraintCheckerFactories(enabledConstraintNames));

		return builder;
	}
}
