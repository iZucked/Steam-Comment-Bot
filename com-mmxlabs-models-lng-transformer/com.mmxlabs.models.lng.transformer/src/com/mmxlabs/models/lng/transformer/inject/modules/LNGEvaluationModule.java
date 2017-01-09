/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Collection;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluatedStateConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.PortTimesPlanner;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.CleanStateIdleTimeEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.DefaultGeneratedCharterOutEvaluator;

/**
 * This {@link Module} configures the default schedule optimisation classes.
 * 
 */
public class LNGEvaluationModule extends AbstractModule {

	public static final String HINT_PORTFOLIO_BREAKEVEN = "LNGEvaluationModule-hint-portfolio-breakeven";
	@NonNull
	private final Collection<String> hints;

	public LNGEvaluationModule(@NonNull final Collection<String> hints) {
		this.hints = hints;
	}

	@Override
	protected void configure() {

		install(new SequencesManipulatorModule());

		bind(DirectRandomSequenceScheduler.class).in(PerChainUnitScope.class);
		bind(ISequenceScheduler.class).to(DirectRandomSequenceScheduler.class);

		bind(VoyagePlanner.class);
		bind(PortTimesPlanner.class);
		bind(ScheduleCalculator.class);
		
		if (hints != null) {

			boolean isCleanState = false;
			for (final String hint : hints) {
				if (LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR.equals(hint)) {
					bind(IGeneratedCharterOutEvaluator.class).to(CleanStateIdleTimeEvaluator.class);
					isCleanState = true;
					break;

				}
			}

			// GCO and Clean state are not compatible with each other
			if (!isCleanState && LicenseFeatures.isPermitted("features:optimisation-charter-out-generation")) {

				for (final String hint : hints) {
					if (LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS.equals(hint)) {
						bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
						break;
					}
				}
			}
		}
		if (LicenseFeatures.isPermitted("features:break-evens") && !hints.contains(HINT_PORTFOLIO_BREAKEVEN)) {
			bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
		}

		// Needed for LegalSequencingChecker
		if (Platform.isRunning()) {
			// bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
			bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
			bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
			bind(IEvaluatedStateConstraintCheckerRegistry.class).toProvider(service(IEvaluatedStateConstraintCheckerRegistry.class).single());
		}

		install(new ConstraintCheckerInstantiatorModule());
		install(new EvaluatedStateConstraintCheckerInstantiatorModule());
		install(new EvaluationProcessInstantiatorModule());
		bind(LegalSequencingChecker.class);
	}

	
}