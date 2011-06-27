/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

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
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			if (loadSlot != null && dischargeSlot != null) {
				
				if (loadSlot.getMaxQuantity() < dischargeSlot.getMinQuantity()) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus)ctx.createFailureStatus(cargo.getId()));
					status.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					status.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					return status;
				}
				if (loadSlot.getMinQuantity() > dischargeSlot.getMaxQuantity()) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus)ctx.createFailureStatus(cargo.getId()));
					status.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					status.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					return status;
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
