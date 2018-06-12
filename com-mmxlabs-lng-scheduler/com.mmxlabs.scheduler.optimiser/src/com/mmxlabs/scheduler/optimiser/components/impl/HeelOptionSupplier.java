/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;

public class HeelOptionSupplier implements IHeelOptionSupplier {

	private final @NonNull IHeelPriceCalculator heelPriceCalculator;
	private final int heelCVValue;
	private final long minHeelLimitInM3;
	private final long maxHeelLimitInM3;
	private final int hash;

	public HeelOptionSupplier(final long minHeelLimitInM3, final long maxHeelLimitInM3, final int heelCVValue, final @NonNull IHeelPriceCalculator heelUnitPrice) {
		this.minHeelLimitInM3 = minHeelLimitInM3;
		this.maxHeelLimitInM3 = maxHeelLimitInM3;
		this.heelCVValue = heelCVValue;
		this.heelPriceCalculator = heelUnitPrice;
		this.hash = Objects.hashCode(minHeelLimitInM3, maxHeelLimitInM3, heelCVValue, heelPriceCalculator);
	}

	@Override
	public @NonNull IHeelPriceCalculator getHeelPriceCalculator() {
		return heelPriceCalculator;
	}

	@Override
	public int getHeelCVValue() {
		return heelCVValue;
	}

	@Override
	public long getMinimumHeelAvailableInM3() {
		return minHeelLimitInM3;
	}

	@Override
	public long getMaximumHeelAvailableInM3() {
		return maxHeelLimitInM3;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof IHeelOptionSupplier) {
			final IHeelOptionSupplier heelOptions = (IHeelOptionSupplier) obj;
			// @formatter:off
			return Objects.equal(minHeelLimitInM3, heelOptions.getMinimumHeelAvailableInM3())
				&& Objects.equal(maxHeelLimitInM3, heelOptions.getMaximumHeelAvailableInM3())
				&& Objects.equal(heelCVValue, heelOptions.getHeelCVValue())
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
