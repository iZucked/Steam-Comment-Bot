/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import scenario.cargo.Cargo;
import scenario.cargo.CargoType;
import scenario.cargo.Slot;

/**
 * Checks that the user hasn't allowed a really long time between a load and a discharge
 * 
 * TODO this and CargoDateConstraint should both consider the travel time
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateSanityConstraint extends AbstractModelConstraint {
	/**
	 * This is the maximum sensible amount of travel time in a cargo, in days
	 */
	private static final long SENSIBLE_TRAVEL_TIME = 60 ;

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
					&& loadSlot != null && dischargeSlot != null) {
				final long availableTime = (dischargeSlot.getWindowStart().getTime() - loadSlot.getWindowStart().getTime()) / Timer.ONE_DAY;
				if (availableTime >
					SENSIBLE_TRAVEL_TIME) {
					return ctx.createFailureStatus(cargo.getId(), availableTime, SENSIBLE_TRAVEL_TIME);
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
