/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Collection;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.DefaultGeneratedCharterOutEvaluator;

/**
 * This {@link Module} configures the default schedule optimisation classes.
 * 
 */
public class LNGEvaluationModule extends AbstractModule {
	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	@NonNull
	private final Collection<String> hints;

	public LNGEvaluationModule(@NonNull final Collection<String> hints) {
		this.hints = hints;
	}

	@Override
	protected void configure() {

		install(new SequencesManipulatorModule());


		bind(DirectRandomSequenceScheduler.class).in(PerChainUnitScope.class);
//		bind(DirectRandomSequenceScheduler.class).in(Singleton.class);
		bind(ISequenceScheduler.class).to(DirectRandomSequenceScheduler.class);


		if (hints != null) {
			if (LicenseFeatures.isPermitted("features:optimisation-charter-out-generation")) {

				for (final String hint : hints) {
					if (LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS.equals(hint)) {
						bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
						break;
					}
				}
			}
		}
		if (LicenseFeatures.isPermitted("features:break-evens")) {
			bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
		}

		// Needed for LegalSequencingChecker
		if (Platform.isRunning()) {
			// bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
			bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
			bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		}

		install(new ConstraintCheckerInstantiatorModule());
		install(new EvaluationProcessInstantiatorModule());
		bind(LegalSequencingChecker.class);
	}

	@Provides
	@PerChainUnitScope
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(final VoyagePlanOptimiser delegate) {
		final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
		return cachingVoyagePlanOptimiser;
	}
}