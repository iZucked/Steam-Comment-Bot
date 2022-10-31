/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VolumeTierContractConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_THRESHOLD = "VolumeTierContractConstraint.Threshold";
	public static final String KEY_VOLUME_UNITS = "VolumeTierContractConstraint.VolumeUnits";
	public static final String KEY_LOW_EXPRESSION = "VolumeTierContractConstraint.LowExpression";
	public static final String KEY_HIGH_EXPRESSION = "VolumeTierContractConstraint.HighExpression";

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		EObject target = ctx.getTarget();

		if (target instanceof VolumeTierPriceParameters pricingParams) {

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withName(ScenarioElementNameHelper.getContainerName(pricingParams).getFirst());

			PriceExpressionUtils.validatePriceExpression(ctx, statuses, baseFactory, pricingParams, CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION, true, KEY_LOW_EXPRESSION);
			PriceExpressionUtils.validatePriceExpression(ctx, statuses, baseFactory, pricingParams, CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION, true, KEY_HIGH_EXPRESSION);

			if (pricingParams.getVolumeLimitsUnit() == null) {

				baseFactory.copyName() //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT) //
						.withMessage("Missing volume units") //
						.withConstraintKey(KEY_VOLUME_UNITS) //
						.make(ctx, statuses);
			}

			if (pricingParams.getThreshold() <= 0.0) {

				baseFactory.copyName() //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD) //
						.withMessage("Threshold should be a positive number") //
						.withConstraintKey(KEY_THRESHOLD) //
						.make(ctx, statuses);
			}
		}
	}
}