/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import scenario.cargo.Cargo;
import scenario.cargo.Slot;

/**
 * A constraint which checks that the load and discharge quantities for a cargo are compatible, so
 * min discharge volume < max load volume
 * @author Tom Hinton
 *
 */
public class CargoVolumeConstraint extends AbstractModelConstraint {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		System.err.println(this.getClass().getSimpleName() + " " + object);
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			if (loadSlot != null && dischargeSlot != null) {
				if (loadSlot.getMaxQuantity() < dischargeSlot.getMinQuantity()) {
					return ctx.createFailureStatus(cargo.getId());
				}
				if (loadSlot.getMinQuantity() > dischargeSlot.getMaxQuantity()) {
					return ctx.createFailureStatus(cargo.getId());
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
