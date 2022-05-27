/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.util;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public class SequenceEvaluationUtils {

	/**
	 * Checks to see whether the sequence should be ignored in an evaluation.
	 * For example, if an optional vessel is empty.
	 * @param sequence
	 * @return
	 */
	public static final boolean shouldIgnoreSequence(ISequence sequence, IVesselCharter vesselCharter) {
		switch (vesselCharter.getVesselInstanceType()) {
		case TIME_CHARTER:
		case FLEET:
		case SPOT_CHARTER:	
			return sequence.size() < 3 && vesselCharter.isOptional();
		case FOB_SALE:
		case DES_PURCHASE:
		case ROUND_TRIP:
			return sequence.size() < 2;
		default:
			return false;
		}
	}
}
