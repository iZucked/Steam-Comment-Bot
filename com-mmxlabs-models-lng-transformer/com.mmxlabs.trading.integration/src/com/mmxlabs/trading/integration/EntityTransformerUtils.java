package com.mmxlabs.trading.integration;

import java.util.Date;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * @since 2.0
 */
public class EntityTransformerUtils {
	public static StepwiseIntegerCurve createTaxCurve(LegalEntity entity, DateAndCurveHelper dateAndCurveHelper, Date earliestDate) {
		final StepwiseIntegerCurve taxCurve = new StepwiseIntegerCurve();  
		taxCurve.setDefaultValue(0);
		for (final TaxRate taxRate: entity.getTaxRates()) {
			final int convertedDate = dateAndCurveHelper.convertTime(earliestDate, taxRate.getDate());  
			taxCurve.setValueAfter(convertedDate, (int) (taxRate.getValue() * Calculator.ScaleFactor));
		}
		return taxCurve;
		
	}
}
