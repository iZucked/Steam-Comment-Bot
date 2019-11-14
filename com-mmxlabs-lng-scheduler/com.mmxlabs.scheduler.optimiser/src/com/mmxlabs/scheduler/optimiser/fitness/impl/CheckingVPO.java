/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingVPO implements IVoyagePlanOptimiser {
	private static final Logger LOG = LoggerFactory.getLogger(CheckingVPO.class);

	private final @NonNull IVoyagePlanOptimiser reference;
	private final @NonNull IVoyagePlanOptimiser cache;

	public CheckingVPO(final @NonNull IVoyagePlanOptimiser reference, final @NonNull IVoyagePlanOptimiser cache) {
		super();
		this.reference = reference;
		this.cache = cache;
	}

	@Override
	public VoyagePlan optimise(@Nullable final IResource resource, @NonNull final IVessel vessel, final long[] startHeelRangeInM3, final int[] baseFuelPricesPerMT, final ICharterCostCalculator charterCostCalculator,
			@NonNull final IPortTimesRecord portTimesRecord, @NonNull final List<@NonNull IOptionsSequenceElement> basicSequence, @NonNull final List<@NonNull IVoyagePlanChoice> choices, int startingTime) {

		final VoyagePlan ref = reference.optimise(resource, vessel, startHeelRangeInM3, baseFuelPricesPerMT, charterCostCalculator, portTimesRecord, basicSequence, choices, startingTime);
		final VoyagePlan res = cache.optimise(resource, vessel, startHeelRangeInM3, baseFuelPricesPerMT, charterCostCalculator, portTimesRecord, basicSequence, choices, startingTime);

		@NonNull
		String refStr = getString(ref);
		@NonNull
		String resStr = getString(res);
		if (refStr.equals(resStr) == false) {
			LOG.error("Checking VPO Error: (plans are different)");
			LOG.error("   reference value:" + refStr);
			LOG.error("    delegate value:" + resStr);
		}

		return res;
	}

	private @NonNull String getString(@Nullable final VoyagePlan voyagePlan) {
		if (voyagePlan == null) {
			return "null";
		}
		return voyagePlan.toString();
	}
}
