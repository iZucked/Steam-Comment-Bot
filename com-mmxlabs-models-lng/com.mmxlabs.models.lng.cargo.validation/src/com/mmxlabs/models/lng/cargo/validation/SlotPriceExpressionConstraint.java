/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 */
public class SlotPriceExpressionConstraint extends AbstractModelMultiConstraint {
	private static double minExpressionValue = 0.0;
	private static double maxExpressionValue = 90.0;

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			// final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			if (slot.isSetPriceExpression()) {
				final String priceExpression = slot.getPriceExpression();
				boolean checkExpression = true;
				if ("??".equals("priceExpression")) {
					if (!(slot.eContainer() instanceof CargoModel)) {
						// Special "changable price" expression for sandbox. Not expected to be in main scenario
						checkExpression = false;
					}
				} else if ("?".equals(priceExpression)) {
					// Permit break even marker
					checkExpression = false;
				}

				if (checkExpression) {
					final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, priceExpression);
					if (!result.isOk()) {
						final String message = String.format("[Slot|'%s']%s", slot.getName(), result.getErrorDetails());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
						failures.add(dsd);
					}
					final ZonedDateTime start;
					if (slot.isSetPricingDate()) {
						start = slot.getPricingDateAsDateTime();
					} else {
						// Not strictly correct, may differ on pricing event and actual scheduled date
						start = slot.getWindowStartWithSlotOrPortTime();
					}
					if (start != null) {

						final YearMonth key = YearMonth.from(start);
						PriceExpressionUtils.constrainPriceExpression(ctx, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, priceExpression, minExpressionValue, maxExpressionValue, key, failures);

						if (priceExpression != null && !priceExpression.trim().isEmpty()) {
							PriceExpressionUtils.checkExpressionAgainstPricingDate(ctx, priceExpression, slot, start.toLocalDate(), CargoPackage.Literals.SLOT__PRICE_EXPRESSION, failures);
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

}
