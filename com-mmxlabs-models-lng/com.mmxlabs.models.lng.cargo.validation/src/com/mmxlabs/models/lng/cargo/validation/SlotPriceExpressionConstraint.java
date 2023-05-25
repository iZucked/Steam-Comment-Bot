/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
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
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof final Slot<?> slot) {

			final ZonedDateTime start;
			if (slot.isSetPricingDate()) {
				start = slot.getPricingDateAsDateTime();
			} else {
				// Not strictly correct, may differ on pricing event and actual scheduled date
				start = slot.getSchedulingTimeWindow().getStart();
			}

			final TriConsumer<EAttribute, EStructuralFeature, String> action = (ownerFeature, validationFeature, priceExpression) -> {
				final PriceIndexType indexType = PriceExpressionUtils.getPriceIndexType(ownerFeature);
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, validationFeature, priceExpression, indexType);
				if (!result.isOk()) {
					final String message = String.format("[Slot|'%s']%s", slot.getName(), result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, validationFeature);
					failures.add(dsd);
				}

				if (start != null) {
					if (indexType == PriceIndexType.COMMODITY) {
						final YearMonth key = YearMonth.from(start);
						PriceExpressionUtils.constrainPriceExpression(ctx, slot, validationFeature, priceExpression, minExpressionValue, maxExpressionValue, key, failures, indexType);
					}
				}
			};

			if (slot.isSetPriceExpression() && SlotContractParamsHelper.isSlotExpressionUsed(slot)) {
				final String priceExpression = slot.getPriceExpression();
				boolean checkExpression = true;
				if ("??".equals(priceExpression)) {
					if (!(slot.eContainer() instanceof CargoModel)) {
						// Special "changable price" expression for sandbox. Not expected to be in main
						// scenario
						checkExpression = false;
					}
				} else if ("?".equals(priceExpression)) {
					// Permit break even marker
					checkExpression = false;
				}

				if (checkExpression) {
					final String name = slot.getName();
					if (slot.eIsSet(CargoPackage.Literals.SLOT__PRICE_EXPRESSION)) {
						validatePrice(ctx, failures, slot.getPriceExpression(), PriceIndexType.COMMODITY, name, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
					}
				}
			} else if (slot instanceof SpotSlot spotSlot && spotSlot.getMarket() != null) {
				final EObject pricParams = spotSlot.getMarket().getPriceInfo();
				if (pricParams != null) {
					for (final EAttribute attrib : pricParams.eClass().getEAllAttributes()) {
						if (attrib.getEType() == EcorePackage.Literals.ESTRING) {
							if (PriceExpressionUtils.hasPriceAnnotation(attrib)) {
								final String priceExpression = (String) pricParams.eGet(attrib);
								action.accept(attrib, CargoPackage.Literals.SLOT__CARGO, priceExpression);
							}
						}
						
					}
				}

			}
		}
	}
	
	private void validatePrice(final IValidationContext ctx, final List<IStatus> failures, final String price, final PriceIndexType type, final String targetName, //
			final Slot<?> slot, final EStructuralFeature feature) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, feature, price, type);
		if (!result.isOk()) {
			final String message = String.format("[Slot|'%s']%s", slot.getName(), result.getErrorDetails());
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(slot, feature);
			failures.add(dsd);
		}
		final ZonedDateTime start;
		if (slot.isSetPricingDate()) {
			start = slot.getPricingDateAsDateTime();
		} else {
			// Not strictly correct, may differ on pricing event and actual scheduled date
			start = slot.getSchedulingTimeWindow().getStart();
		}
		if (start != null) {

			final YearMonth key = YearMonth.from(start);
			// type!
			PriceExpressionUtils.constrainPriceExpression(ctx, slot, feature, price, minExpressionValue, maxExpressionValue, key, failures, type);

			if (price != null && !price.trim().isEmpty()) {
				PriceExpressionUtils.checkExpressionAgainstPricingDate(ctx, price, slot, start.toLocalDate(), feature, failures, type);
			}
		}
	}

}
