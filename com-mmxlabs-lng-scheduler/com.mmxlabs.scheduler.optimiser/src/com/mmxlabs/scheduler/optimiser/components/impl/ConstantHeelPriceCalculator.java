package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class ConstantHeelPriceCalculator implements IHeelPriceCalculator {

	public static final @NonNull ConstantHeelPriceCalculator ZERO = new ConstantHeelPriceCalculator(0);

	private final int value;

	public ConstantHeelPriceCalculator(final int value) {
		this.value = value;
	}

	@Override
	public int getHeelPrice(final long heelVolume, final int time) {
		return value;
	}

	@Override
	public int getHeelPrice(final long heelVolume, final int localTime, @NonNull final IPort port) {
		return value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ConstantHeelPriceCalculator) {
			final ConstantHeelPriceCalculator other = (ConstantHeelPriceCalculator) obj;
			return value == other.value;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value;
	}
}
