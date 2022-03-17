/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
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
public class StartHeelOptionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {

		final EObject object = ctx.getTarget();
		if (object instanceof StartHeelOptions heelOptions) {

			if (heelOptions.getMinVolumeAvailable() > heelOptions.getMaxVolumeAvailable()) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min volume is less that max volume"));
				dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getStartHeelOptions_MinVolumeAvailable());
				dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getStartHeelOptions_MaxVolumeAvailable());
				failures.add(dsd);
			}

			if (heelOptions.getMaxVolumeAvailable() > 0) {
				if (heelOptions.getCvValue() < 0.001) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A CV value should be specified"));
					dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getStartHeelOptions_CvValue());
					failures.add(dsd);
				}
				if (heelOptions.getCvValue() > 40.0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high"));
					dsd.addEObjectAndFeature(heelOptions, CommercialPackage.eINSTANCE.getStartHeelOptions_CvValue());
					failures.add(dsd);
				}

				if (heelOptions.getPriceExpression() != null && !heelOptions.getPriceExpression().isEmpty()) {

					ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, heelOptions, CommercialPackage.Literals.START_HEEL_OPTIONS__PRICE_EXPRESSION,
							heelOptions.getPriceExpression());
					if (!result.isOk()) {
						String message = String.format("[Heel Price] %s", result.getErrorDetails());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(heelOptions, CommercialPackage.Literals.START_HEEL_OPTIONS__PRICE_EXPRESSION);
						failures.add(dcsd);
					}
				}
			}
		}

	}
}
