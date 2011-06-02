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
import scenario.cargo.CargoType;
import scenario.cargo.Slot;

/**
 * Check that the end of any cargo's discharge window is not before the start of
 * its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse
	 * .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			if (cargo.getCargoType().equals(CargoType.FLEET)
					&& loadSlot != null && dischargeSlot != null
					&& cargo.getLoadSlot().getWindowStart() != null
					&& cargo.getDischargeSlot().getWindowStart() != null
			) {
				if (dischargeSlot.getWindowEnd().before(
						loadSlot.getWindowStart())) {
					return ctx.createFailureStatus(cargo.getId());
				}
				// TODO check travel time feasibility here
				
			}
		}
		return ctx.createSuccessStatus();
	}

}
