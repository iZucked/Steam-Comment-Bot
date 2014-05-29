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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoVolumeVesselAssignmentConstraint extends AbstractModelMultiConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			if (assignableElement.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}

			int capacity = -1;
			if (assignableElement.getAssignment() instanceof Vessel) {
				final Vessel vessel = (Vessel) assignableElement.getAssignment();
				capacity = vessel.getVesselOrVesselClassCapacity();
			} else if (assignableElement.getAssignment() instanceof VesselClass) {
				final VesselClass vesselClass = (VesselClass) assignableElement.getAssignment();
				capacity = vesselClass.getCapacity();
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
