/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class BaseFuelEquivalenceConstraint extends AbstractModelConstraint {
	private final double min = 10.;
	private final double max = 70.;
	
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof BaseFuel) {
			final BaseFuel baseFuel = (BaseFuel) target;
			double factor = baseFuel.getEquivalenceFactor();

			if (factor < min || factor > max) {
				String message = String.format("'%s' has equivalence factor %.2f (should be between %.2f and %.2f)", baseFuel.getName(), factor, min, max);
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(baseFuel, FleetPackage.eINSTANCE.getBaseFuel_EquivalenceFactor());
				return dcsd;
			}

		}

		return ctx.createSuccessStatus();
	}
}
