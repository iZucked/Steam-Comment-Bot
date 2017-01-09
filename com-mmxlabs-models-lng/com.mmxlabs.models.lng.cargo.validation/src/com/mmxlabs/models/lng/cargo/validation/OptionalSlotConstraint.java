/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class OptionalSlotConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final Slot slot = (Slot) object;

			Cargo cargo = null;
			if (slot instanceof LoadSlot) {
				cargo = ((LoadSlot) slot).getCargo();
			} else if (slot instanceof DischargeSlot) {
				cargo = ((DischargeSlot) slot).getCargo();
			} else {
				return ctx.createSuccessStatus();

			}
			if (cargo == null && !slot.isOptional()) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'"+slot.getName()+"'"), IStatus.WARNING);
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Optional());
				return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}

}
