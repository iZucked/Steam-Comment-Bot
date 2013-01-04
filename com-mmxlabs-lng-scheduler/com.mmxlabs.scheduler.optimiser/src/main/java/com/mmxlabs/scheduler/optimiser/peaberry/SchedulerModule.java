package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.List;
import java.util.Random;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;

/**
 * @since 2.0
 */
public class SchedulerModule extends AbstractModule {

	@Override
	protected void configure() {
		// Not singleton as created in different places with different instances of constraint checkers - of which the lateness constraint is fiddled with
		bind(LegalSequencingChecker.class);
	}

	@Provides
	@Singleton
	private ConstrainedMoveGenerator provideConstrainedMoveGenerator(final Injector injector, final IOptimisationContext context, @Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {

		final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator(context);
		cmg.setRandom(new Random(seed));
		injector.injectMembers(cmg);
		// cmg.init();
		return cmg;
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(pairwiseCheckers);

		return builder;
	}
}