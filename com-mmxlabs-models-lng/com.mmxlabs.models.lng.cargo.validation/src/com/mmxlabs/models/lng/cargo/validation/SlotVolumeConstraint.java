/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.mmxcore.validation.context.ValidationSupport;

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
			if (eventType == EMFEventType.NULL) {
				final MMXRootObject scenario = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, object);

				if (scenario == null) {
					return ctx.createSuccessStatus();
				}

				// This is being triggered by a batch mode validation.
				final Slot slot = (Slot) object;
				// TODO return some placeholders for the error message
				if (slot.getSlotOrContractMinQuantity(scenario) < 0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					return dsd;
				}
				if (slot.getSlotOrContractMaxQuantity(scenario) < 0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					return dsd;
				}
				if (slot.getSlotOrContractMinQuantity(scenario) > slot.getSlotOrContractMaxQuantity(scenario)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

					return dsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
