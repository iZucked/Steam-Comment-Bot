/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 */
public class EntityTransformerUtils {

	public static StepwiseIntegerCurve createTaxCurve(final List<TaxRate> taxRates, final DateAndCurveHelper dateAndCurveHelper, final ZonedDateTime earliestDate) {
		final StepwiseIntegerCurve taxCurve = new StepwiseIntegerCurve();
		taxCurve.setDefaultValue(0);
		for (final TaxRate taxRate : taxRates) {
			final int convertedDate = dateAndCurveHelper.convertTime(earliestDate, taxRate.getDate().atStartOfDay(ZoneId.of("UTC")));
			taxCurve.setValueAfter(convertedDate, (int) (taxRate.getValue() * Calculator.ScaleFactor));
		}
		return taxCurve;

	}
}
