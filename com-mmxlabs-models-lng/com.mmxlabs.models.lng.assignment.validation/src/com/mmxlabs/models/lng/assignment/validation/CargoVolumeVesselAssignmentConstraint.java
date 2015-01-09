/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoVolumeVesselAssignmentConstraint extends AbstractModelMultiConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;
			VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();

			if (vesselAssignmentType == null) {
				return Activator.PLUGIN_ID;
			}

			int capacity = -1;
			if (vesselAssignmentType instanceof VesselAvailability) {
				VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;

				final Vessel vessel = vesselAvailability.getVessel();
				if (vessel != null) {
					capacity = vessel.getVesselOrVesselClassCapacity();
				}
			} else if (vesselAssignmentType instanceof CharterInMarket) {
				CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				final VesselClass vesselClass = charterInMarket.getVesselClass();
				if (vesselClass != null) {
					capacity = vesselClass.getCapacity();
				}
			} else {
				// Can't do much here, no capacity...
				return Activator.PLUGIN_ID;
			}

			Cargo cargo = null;
			Slot slot = null;
			if (assignableElement instanceof Slot) {
				slot = (Slot) assignableElement;
				cargo = slot.getCargo();
			} else if (assignableElement instanceof Cargo) {
				cargo = (Cargo) assignableElement;
			}

			if (cargo != null) {
				for (final Slot s : cargo.getSlots()) {

					if (s.getSlotOrContractMinQuantity() > capacity) {

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Slot|" + s.getName()
								+ "] has a minimum volume greater than the capacity of current vessel assignment"));
						failure.addEObjectAndFeature(s, CargoPackage.eINSTANCE.getSlot_MinQuantity());
						failures.add(failure);
					}
				}
			} else if (slot != null) {
				if (slot.getSlotOrContractMinQuantity() > capacity) {

					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Slot|" + slot.getName()
							+ "] has a minimum volume greater than the capacity of current vessel assignment"));
					failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					failures.add(failure);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
