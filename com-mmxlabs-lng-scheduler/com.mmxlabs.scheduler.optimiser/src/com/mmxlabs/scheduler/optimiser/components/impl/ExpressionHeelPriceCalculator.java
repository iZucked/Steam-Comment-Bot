package com.mmxlabs.scheduler.optimiser.components.impl;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class ExpressionHeelPriceCalculator implements IHeelPriceCalculator {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	private final @NonNull ICurve expressionCurve;

	public ExpressionHeelPriceCalculator(final @NonNull ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	public int getHeelPrice(final long heelVolume, final int utcTime) {
		return expressionCurve.getValueAtPoint(utcTime);
	}

	@Override
	public int getHeelPrice(final long heelVolume, final int localTime, final @NonNull IPort port) {
		final int utcTime = timeZoneToUtcOffsetProvider.UTC(localTime, port);
		return getHeelPrice(heelVolume, utcTime);
	}

}
