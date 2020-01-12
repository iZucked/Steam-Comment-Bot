/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 */
public class SlotCancellationFeeConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			// final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("Slot", slot.getName());

			final String cancellationFee = slot.getSlotOrDelegateCancellationExpression();

			if (cancellationFee != null && !cancellationFee.isEmpty()) {
				PriceExpressionUtils.validatePriceExpression(ctx, failures, factory, slot, CargoPackage.Literals.SLOT__CANCELLATION_EXPRESSION, true);
			}
		}
		return Activator.PLUGIN_ID;
	}

}
