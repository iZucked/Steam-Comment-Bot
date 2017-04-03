/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;

@NonNullByDefault
public class HeelOptionConsumer implements IHeelOptionConsumer {

	private final IHeelPriceCalculator heelPriceCalculator;
	private final VesselTankState mustEndCold;
	private final long minHeelLimitInM3;
	private final long maxHeelLimitInM3;
	private final int hash;

	public HeelOptionConsumer(final long minHeelLimitInM3, final long maxHeelLimitInM3, final VesselTankState endHeelState, final IHeelPriceCalculator heelUnitPrice) {
		this.minHeelLimitInM3 = minHeelLimitInM3;
		this.maxHeelLimitInM3 = maxHeelLimitInM3;
		this.mustEndCold = endHeelState;
		this.heelPriceCalculator = heelUnitPrice;
		this.hash = Objects.hashCode(minHeelLimitInM3, maxHeelLimitInM3, mustEndCold, heelPriceCalculator);
	}

	@Override
	public IHeelPriceCalculator getHeelPriceCalculator() {
		return heelPriceCalculator;
	}

	@Override
	public VesselTankState getExpectedTankState() {
		return mustEndCold;
	}

	@Override
	public long getMinimumHeelAcceptedInM3() {
		return minHeelLimitInM3;
	}

	@Override
	public long getMaximumHeelAcceptedInM3() {
		return maxHeelLimitInM3;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj instanceof IHeelOptionConsumer) {
			final IHeelOptionConsumer heelOptions = (IHeelOptionConsumer) obj;
			// @formatter:off
			return Objects.equal(minHeelLimitInM3, heelOptions.getMinimumHeelAcceptedInM3())
				&& Objects.equal(maxHeelLimitInM3, heelOptions.getMaximumHeelAcceptedInM3())
				&& Objects.equal(mustEndCold, heelOptions.getExpectedTankState())
				&& Objects.equal(heelPriceCalculator, heelOptions.getHeelPriceCalculator())
				;
			// @formatter:on
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}
}
