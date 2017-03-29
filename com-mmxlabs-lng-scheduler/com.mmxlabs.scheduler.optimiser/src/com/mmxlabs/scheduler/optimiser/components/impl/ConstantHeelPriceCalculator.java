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

}
