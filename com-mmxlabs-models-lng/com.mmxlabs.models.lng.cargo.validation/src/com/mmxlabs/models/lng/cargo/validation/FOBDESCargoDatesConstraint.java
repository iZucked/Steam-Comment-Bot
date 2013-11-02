/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class FOBDESCargoDatesConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		
		if (true) {
			return ctx.createSuccessStatus();
		}
		
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			// START WIHT SIZE CHECK

			// ADD IN UTILS TO GET SORTED SLOT ORDER
			// SORT BY DATE< THEN BY TYPE>
			//
			if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {

				if (cargo.getSlots().size() > 2) {
					// Error really,but that is for another validation constraint....
					return ctx.createSuccessStatus();
				}

				LoadSlot loadSlot = null;
				DischargeSlot dischargeSlot = null;

				for (Slot slot : cargo.getSlots()) {

					if (slot instanceof LoadSlot) {
						if (loadSlot != null) {
							// Error really, can only be one load slot..
							return ctx.createSuccessStatus();
						}
						loadSlot = (LoadSlot) slot;
					} else if (slot instanceof DischargeSlot) {
						if (dischargeSlot != null) {
							// Error really, can only be one load slot..
							return ctx.createSuccessStatus();
						}
						dischargeSlot = (DischargeSlot) slot;
					}
				}

				if (loadSlot != null && dischargeSlot != null) {
					if (!(checkDates(loadSlot.getWindowStartWithSlotOrPortTime(), loadSlot.getWindowEndWithSlotOrPortTime(), dischargeSlot.getWindowEndWithSlotOrPortTime())
							|| checkDates(loadSlot.getWindowStartWithSlotOrPortTime(), loadSlot.getWindowEndWithSlotOrPortTime(), dischargeSlot.getWindowEndWithSlotOrPortTime())
							|| checkDates(dischargeSlot.getWindowStartWithSlotOrPortTime(), dischargeSlot.getWindowEndWithSlotOrPortTime(), loadSlot.getWindowStartWithSlotOrPortTime()) || checkDates(
								dischargeSlot.getWindowStartWithSlotOrPortTime(), dischargeSlot.getWindowEndWithSlotOrPortTime(), loadSlot.getWindowEndWithSlotOrPortTime()))) {

						final String message = String.format("[Cargo|%s] Incompatible slot windows.", cargo.getName());

						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						status.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						status.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						return status;
					}
				}
			}
		}

		return ctx.createSuccessStatus();
	}

	private boolean checkDates(final Date windowStart, final Date windowEnd, final Date target) {

		if (target.before(windowStart)) {
			return false;
		}
		if (target.after(windowEnd)) {
			return false;
		}
		return true;
	}
}
