/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;

public class HeelOptions implements IHeelOptions {

	private final int heelUnitPrice;
	private final int heelCVValue;
	private final long heelLimit;

	public HeelOptions() {
		this(0, 0, 0);
	}

	public HeelOptions(final long heelLimitInM3, final int heelCVValue, final int heelUnitPrice) {
		this.heelLimit = heelLimitInM3;
		this.heelCVValue = heelCVValue;
		this.heelUnitPrice = heelUnitPrice;
	}

	@Override
	public int getHeelUnitPrice() {
		return heelUnitPrice;
	}

	@Override
	public long getHeelLimit() {
		return heelLimit;
	}

	@Override
	public int getHeelCVValue() {
		return heelCVValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IHeelOptions) {
			IHeelOptions heelOptions = (IHeelOptions) obj;
			// @formatter:off
			return Objects.equal(heelLimit, heelOptions.getHeelLimit())
				&& Objects.equal(heelCVValue, heelOptions.getHeelCVValue())
				&& Objects.equal(heelUnitPrice, heelOptions.getHeelUnitPrice())
				;
			// @formatter:on
		}
		return false;
	}
}
