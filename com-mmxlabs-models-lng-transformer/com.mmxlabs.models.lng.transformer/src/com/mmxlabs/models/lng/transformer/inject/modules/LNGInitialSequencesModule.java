/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures. This is a {@link PrivateModule} to avoid "leakage" into the parent injector
 * 
 */
public class LNGInitialSequencesModule extends PrivateModule {
	@NonNull
	public static final String KEY_GENERATED_RAW_SEQUENCES = "generated-raw-sequences";

	@NonNull
	public static final String KEY_GENERATED_SOLUTION_PAIR = "generated-solution-pair";

	@Override
	protected void configure() {
		// if (Platform.isRunning()) {
		// bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
		// }
		//
		// install(new ConstraintCheckerInstantiatorModule());

		bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(@NonNull final Injector injector, @NonNull final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(pairwiseCheckers);
		injector.injectMembers(builder);
		return builder;
	}

	@Provides
	@Singleton
	@Named(KEY_GENERATED_RAW_SEQUENCES)
	@Exposed
	private ISequences provideInitialSequences(@NonNull final IOptimisationTransformer optimisationTransformer, @NonNull final IOptimisationData data, @NonNull final ModelEntityMap modelEntityMap) {

		final ISequences sequences = optimisationTransformer.createInitialSequences(data, modelEntityMap);

		return sequences;
	}

	@Provides
	@Singleton
	@Named(KEY_GENERATED_SOLUTION_PAIR)
	@Exposed
	private Pair<ISequences, IAnnotatedSolution> provideSolutionPair(@NonNull final Injector injector, @NonNull @Named(KEY_GENERATED_RAW_SEQUENCES) final ISequences sequences) {

		final IAnnotatedSolution annotatedSolution = LNGSchedulerJobUtils.evaluateCurrentState(injector, injector.getInstance(IOptimisationData.class), sequences);

		return new Pair<ISequences, IAnnotatedSolution>(sequences, annotatedSolution);
	}
}
