/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A model constraint for checking that a slot's minimum and maximum volumes are sensible (0 <= min <= max)
 * 
 * @author Tom Hinton
 * 
 */
public class SlotVolumeConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final EMFEventType eventType = ctx.getEventType();
			
			// This is being triggered by a batch mode validation.
			if (eventType == EMFEventType.NULL) {

				final Slot slot = (Slot) object;
				String name = slot.getName();
				// TODO return some placeholders for the error message
				if (slot.getSlotOrContractMinQuantity() < 0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Negative min volume"));

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					return dsd;
				}
				if (slot.getSlotOrContractMaxQuantity() < 0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Negative max volume"));

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					return dsd;
				}
				if (slot.getSlotOrContractMinQuantity() > slot.getSlotOrContractMaxQuantity()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Min volume greater than max volume."));

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

					return dsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
