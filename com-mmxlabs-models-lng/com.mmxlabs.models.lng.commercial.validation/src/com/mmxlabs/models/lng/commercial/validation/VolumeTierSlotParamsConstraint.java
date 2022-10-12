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
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VolumeTierSlotParamsConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_THRESHOLD = "VolumeTierSlotParamsConstraint.Threshold";
	public static final String KEY_VOLUME_UNITS = "VolumeTierSlotParamsConstraint.VolumeUnits";
	public static final String KEY_LOW_EXPRESSION = "VolumeTierSlotParamsConstraint.LowExpression";
	public static final String KEY_HIGH_EXPRESSION = "VolumeTierSlotParamsConstraint.HighExpression";
	
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		EObject target = ctx.getTarget();

		if (target instanceof VolumeTierSlotParams slotParams) {
			if (!slotParams.isOverrideContract()) {
				return;
			}

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withName(ScenarioElementNameHelper.getContainerName(slotParams).getFirst());

			PriceExpressionUtils.validatePriceExpression(ctx, statuses, baseFactory, slotParams, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION, false, KEY_LOW_EXPRESSION);
			PriceExpressionUtils.validatePriceExpression(ctx, statuses, baseFactory, slotParams, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION, false, KEY_HIGH_EXPRESSION);

			if (slotParams.getVolumeLimitsUnit() == null) {

				baseFactory.copyName() //
						.withObjectAndFeature(slotParams, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT) //
						.withMessage("Missing volume units") //
						.withConstraintKey(KEY_VOLUME_UNITS) //
						.make(ctx, statuses);
			}

			if (slotParams.getThreshold() <= 0.0) {

				baseFactory.copyName() //
						.withObjectAndFeature(slotParams, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD) //
						.withMessage("Threshold should be a positive number") //
						.withConstraintKey(KEY_THRESHOLD)
						.make(ctx, statuses);
			}
		}
	}
}
