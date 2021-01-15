/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ShippingExpressionConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof SimpleVesselCharterOption) {
			final SimpleVesselCharterOption shippingOption = (SimpleVesselCharterOption) target;
			final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, shippingOption, AnalyticsPackage.eINSTANCE.getSimpleVesselCharterOption_HireCost(), shippingOption.getHireCost(),
					PriceIndexType.CHARTER);
			if (!result.isOk()) {
				final String message = String.format("%s", result.getErrorDetails());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(shippingOption, AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST);
				failures.add(dsd);
			}
			if (target instanceof OptionalSimpleVesselCharterOption) {
				OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption = (OptionalSimpleVesselCharterOption) target;
				if (optionalAvailabilityShippingOption.getStart() != null) {
					LocalDate date = optionalAvailabilityShippingOption.getStart();
					if (date.getYear() < 1900) {
						final String message = String.format("Availability start date has invalid year");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(optionalAvailabilityShippingOption, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START);
						failures.add(dsd);
					}
				}
				if (optionalAvailabilityShippingOption.getEnd() != null) {
					LocalDate date = optionalAvailabilityShippingOption.getEnd();
					if (date.getYear() > 2025) {
						final String message = String.format("Availability end date has invalid year");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(optionalAvailabilityShippingOption, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END);
						failures.add(dsd);
					}
				}
				final ValidationResult ballastBonus = PriceExpressionUtils.validatePriceExpression(ctx, optionalAvailabilityShippingOption, AnalyticsPackage.eINSTANCE.getOptionalSimpleVesselCharterOption_BallastBonus(), optionalAvailabilityShippingOption.getBallastBonus(),
						PriceIndexType.CHARTER);
				if (!ballastBonus.isOk()) {
					final String message = String.format("%s", ballastBonus.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dsd.addEObjectAndFeature(shippingOption, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS);
					failures.add(dsd);
				}
				final ValidationResult repositioningFee = PriceExpressionUtils.validatePriceExpression(ctx, optionalAvailabilityShippingOption, AnalyticsPackage.eINSTANCE.getOptionalSimpleVesselCharterOption_RepositioningFee(), optionalAvailabilityShippingOption.getBallastBonus(),
						PriceIndexType.CHARTER);
				if (!repositioningFee.isOk()) {
					final String message = String.format("%s", repositioningFee.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					
					dsd.addEObjectAndFeature(shippingOption, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE);
					failures.add(dsd);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
