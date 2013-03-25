/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

/**
 * A constraint which checks that the load and discharge quantities for a cargo are compatible, so min discharge volume < max load volume
 * 
 * @author Tom Hinton
 * 
 */
public class CargoVolumeConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {

			final Cargo cargo = (Cargo) object;

			int loadMinVolume = 0;
			int loadMaxVolume = 0;
			int dischargeMinVolume = 0;
			int dischargeMaxVolume = 0;
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					loadMinVolume += slot.getSlotOrContractMinQuantity();
					loadMaxVolume += slot.getSlotOrContractMaxQuantity();
				} else if (slot instanceof DischargeSlot) {
					dischargeMinVolume += slot.getSlotOrContractMinQuantity();
					dischargeMaxVolume += slot.getSlotOrContractMaxQuantity();
				}
			}
			if (loadMaxVolume > dischargeMinVolume) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo " + cargo.getName()
						+ " max load quantity is less than the minimum discharge quantity."));

				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					} else if (slot instanceof DischargeSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

					}
				}

				return status;
			}
			if (loadMinVolume > dischargeMaxVolume) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo " + cargo.getName()
						+ " min load quantity is greater than the maximum discharge quantity."));
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					} else if (slot instanceof DischargeSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

					}
				}

				return status;
			}

		}
		return ctx.createSuccessStatus();
	}
}
