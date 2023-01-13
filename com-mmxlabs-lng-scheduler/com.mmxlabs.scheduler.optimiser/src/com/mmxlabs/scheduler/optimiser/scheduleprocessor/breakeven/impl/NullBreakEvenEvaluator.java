/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Empty implementation of {@link IBreakEvenEvaluator}
 * 
 * @author Simon Goodall
 * 
 */
public class NullBreakEvenEvaluator implements IBreakEvenEvaluator {

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final IVesselCharter vesselCharter, final VoyagePlan vp, final IPortTimesRecord portTimesRecord,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution) {
		// Does nothing
		return null;
	}

}
