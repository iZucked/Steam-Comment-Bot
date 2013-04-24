/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.Date;

import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * @since 3.0
 */
public class EntityTransformerUtils {
	public static StepwiseIntegerCurve createTaxCurve(final LegalEntity entity, final DateAndCurveHelper dateAndCurveHelper, final Date earliestDate) {
		final StepwiseIntegerCurve taxCurve = new StepwiseIntegerCurve();
		taxCurve.setDefaultValue(0);
		for (final TaxRate taxRate : entity.getTaxRates()) {
			final int convertedDate = dateAndCurveHelper.convertTime(earliestDate, taxRate.getDate());
			taxCurve.setValueAfter(convertedDate, (int) (taxRate.getValue() * Calculator.ScaleFactor));
		}
		return taxCurve;

	}
}
