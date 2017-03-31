package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

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

	private final @NonNull String expression;

	/**
	 * The curve is used for data. The expression String is used for equality
	 * 
	 * @param expression
	 * @param expressionCurve
	 */
	public ExpressionHeelPriceCalculator(final @NonNull String expression, final @NonNull ICurve expressionCurve) {
		this.expression = expression;
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

	@Override
	public int hashCode() {
		return expression.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof ExpressionHeelPriceCalculator) {
			final ExpressionHeelPriceCalculator other = (ExpressionHeelPriceCalculator) obj;
			return Objects.equals(expression, other.expression);

		}
		return false;
	}
}
