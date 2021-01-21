/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.YearMonth;
import java.util.function.Consumer;

import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

public class DataCurveBuilder<T extends AbstractYearMonthCurve> {
	private final T curve;
	private final Consumer<T> buildAction;

	public DataCurveBuilder(final T curve, final Consumer<T> buildAction) {
		this.curve = curve;
		this.buildAction = buildAction;
	}

	public DataCurveBuilder<T> addIndexPoint(final YearMonth date, final double value) {
		final YearMonthPoint pt = PricingFactory.eINSTANCE.createYearMonthPoint();
		pt.setDate(date);
		pt.setValue(value);

		curve.getPoints().add(pt);

		return this;
	}

	public T build() {
		buildAction.accept(curve);
		return curve;
	}
}
