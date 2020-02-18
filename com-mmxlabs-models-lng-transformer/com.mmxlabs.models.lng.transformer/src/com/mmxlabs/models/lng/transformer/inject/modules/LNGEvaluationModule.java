/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Collection;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluatedStateConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.scheduler.optimiser.exposures.ExposuresCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanner;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.moves.util.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.schedule.CapacityViolationChecker;
import com.mmxlabs.scheduler.optimiser.schedule.IdleTimeChecker;
import com.mmxlabs.scheduler.optimiser.schedule.LatenessChecker;
import com.mmxlabs.scheduler.optimiser.schedule.ProfitAndLossCalculator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterLengthEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.CharterLengthEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.CleanStateIdleTimeEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.DefaultGeneratedCharterOutEvaluator;

/**
 * This {@link Module} configures the default schedule optimisation classes.
 * 
 */
public class LNGEvaluationModule extends AbstractModule {

	public static final @NonNull String HINT_PORTFOLIO_BREAKEVEN = "LNGEvaluationModule-hint-portfolio-breakeven";
	@NonNull
	private final Collection<String> hints;

	public LNGEvaluationModule(@NonNull final Collection<String> hints) {
		this.hints = hints;
	}

	@Override
	protected void configure() {

		install(new SequencesManipulatorModule());

		bind(VoyagePlanner.class).in(Singleton.class);
		bind(IVoyagePlanner.class).to(VoyagePlanner.class);
		bind(IdleTimeChecker.class);
		bind(LatenessChecker.class);
		bind(CapacityViolationChecker.class);
		bind(ProfitAndLossCalculator.class);
		bind(ExposuresCalculator.class);
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
			if (!isCleanState && LicenseFeatures.isPermitted("features:optimisation-charter-out-generation") && hints.contains(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS)) {
				bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
			}

			if (LicenseFeatures.isPermitted("features:break-evens") && !hints.contains(HINT_PORTFOLIO_BREAKEVEN)) {
				bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
			}

			if (LicenseFeatures.isPermitted("features:charter-length") && hints.contains(LNGTransformerHelper.HINT_CHARTER_LENGTH)) {
				// See LNGTransformerModule for parameters
				bind(IGeneratedCharterLengthEvaluator.class).to(CharterLengthEvaluator.class);
			}
		}
		// Needed for LegalSequencingChecker
		if (Platform.isRunning()) {
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