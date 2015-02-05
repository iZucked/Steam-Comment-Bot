/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks to test if slot and vessel event dates are consistent with the start / end dates of the assigned vessel.
 * 
 */
public class VesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignment = (AssignableElement) object;

			final VesselAssignmentType vesselAssignmentType = assignment.getVesselAssignmentType();
			if (!(vesselAssignmentType instanceof VesselAvailability)) {
				return Activator.PLUGIN_ID;
			}

			final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
			final Vessel vessel = vesselAvailability.getVessel();

			final Date availabilityStartAfter = vesselAvailability.getStartAfter();
			final Date availabilityEndBy = vesselAvailability.getEndBy();

			if (assignment instanceof Cargo) {
				final Cargo cargo = (Cargo) assignment;
				for (final Slot slot : cargo.getSlots()) {
					if (vesselAvailability.isSetStartAfter()) {
						final Date windowEndWithSlotOrPortTime = slot.getWindowEndWithSlotOrPortTime();
						if (windowEndWithSlotOrPortTime != null && availabilityStartAfter != null && windowEndWithSlotOrPortTime.before(availabilityStartAfter)) {
							final String message = String.format("Slot|%s is assigned to vessel %s but window date is before the vessel start date.", slot.getName(), vessel.getName());
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
							failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
							failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
							failures.add(failure);
						}
					}
					if (vesselAvailability.isSetEndBy()) {
						final Date windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
						if (windowStartWithSlotOrPortTime != null && availabilityEndBy != null && windowStartWithSlotOrPortTime.after(availabilityEndBy)) {
							final String message = String.format("Slot|%s is assigned to vessel %s but window date is after the vessel end date.", slot.getName(), vessel.getName());
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
							failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
							failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
							failures.add(failure);
						}
					}
				}

			} else if (assignment instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) assignment;
				if (vesselAvailability.isSetStartAfter()) {
					final Date eventStartBy = vesselEvent.getStartBy();
					if (eventStartBy != null && availabilityStartAfter != null && eventStartBy.before(availabilityStartAfter)) {
						final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is before the vessel start date.", vesselEvent.getName(), vessel.getName());
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
						failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
						failures.add(failure);
					}
				}
				if (vesselAvailability.isSetEndBy()) {
					final Date eventStartAfter = vesselEvent.getStartAfter();
					if (eventStartAfter != null && availabilityEndBy != null && eventStartAfter.after(availabilityEndBy)) {
						final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is after the vessel end date.", vesselEvent.getName(), vessel.getName());
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
						failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
						failures.add(failure);
					}
				}
			}

		}
		return Activator.PLUGIN_ID;
	}
}
