/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint which checks {@link StartHeelOptions}
 * 
 * @author Simon Goodall
 */
public class EndHeelOptionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof EndHeelOptions heelOptions) {

			if (heelOptions.getMinimumEndHeel() > heelOptions.getMaximumEndHeel()) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min volume is less that max volume"));
				dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getEndHeelOptions_MinimumEndHeel());
				dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getEndHeelOptions_MaximumEndHeel());
				failures.add(dsd);
			}

			if (heelOptions.getPriceExpression() != null && !heelOptions.getPriceExpression().isEmpty()) {

				ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, heelOptions, CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION,
						heelOptions.getPriceExpression());
				if (!result.isOk()) {
					String message = String.format("[Heel Price] %s", result.getErrorDetails());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(heelOptions, CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION);
					failures.add(dcsd);
				}
			}

			if (heelOptions.getTankState() == EVesselTankState.MUST_BE_WARM) {
				if (heelOptions.getMinimumEndHeel() > 0 || heelOptions.getMaximumEndHeel() > 0) {
					String message = String.format("Heel range must be 0 when tanks should be warm");
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					if (heelOptions.getMinimumEndHeel() > 0) {
						dcsd.addEObjectAndFeature(heelOptions, CommercialPackage.Literals.END_HEEL_OPTIONS__MINIMUM_END_HEEL);
					}
					if (heelOptions.getMaximumEndHeel() > 0) {
						dcsd.addEObjectAndFeature(heelOptions, CommercialPackage.Literals.END_HEEL_OPTIONS__MAXIMUM_END_HEEL);
					}
					failures.add(dcsd);
				}

			}
		}
	}
}
