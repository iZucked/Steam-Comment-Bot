/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof BaseFuel) {
			final BaseFuel baseFuel = (BaseFuel) target;

			if (baseFuel.getEquivalenceFactor() < 0.000001) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'"+baseFuel.getName()+"'"));
				dcsd.addEObjectAndFeature(baseFuel, FleetPackage.eINSTANCE.getBaseFuel_EquivalenceFactor());
				return dcsd;
			}

		}

		return ctx.createSuccessStatus();
	}
}
