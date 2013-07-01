package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class BaseFuelPriceConstraint  extends AbstractModelConstraint {
	final double min = 2000;
	final double max = 10000;

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof BaseFuelCost) {
			final BaseFuelCost cost = (BaseFuelCost) target;
			final double price = cost.getPrice();
			
			if (price < min || price > max) {
				String message = String.format("'%s' has price %.2f (should be between %f and %f)", cost.getFuel().getName(), cost.getPrice(), min, max);
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				return dcsd;
			}

		}

		return ctx.createSuccessStatus();
	}

}
