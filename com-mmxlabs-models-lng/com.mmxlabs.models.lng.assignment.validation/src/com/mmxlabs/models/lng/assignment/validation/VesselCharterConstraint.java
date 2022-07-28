/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks to test if slot and vessel event dates are consistent with the start /
 * end dates of the assigned vessel.
 * 
 */
public class VesselCharterConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement assignment) {

			final VesselAssignmentType vesselAssignmentType = assignment.getVesselAssignmentType();
			if (!(vesselAssignmentType instanceof VesselCharter)) {
				return;
			}

			final VesselCharter vesselCharter = (VesselCharter) vesselAssignmentType;
			final Vessel vessel = vesselCharter.getVessel();

			final ZonedDateTime availabilityStartAfter = vesselCharter.getStartAfterAsDateTime();
			final ZonedDateTime availabilityEndBy = vesselCharter.getEndByAsDateTime();

			final EObject container = extraContext.getContainer(assignment);
			if (container instanceof CargoModel cargoModel) {

				if (assignment instanceof Cargo cargo) {
					for (final Slot<?> slot : cargo.getSlots()) {
						if (vesselCharter.isSetStartAfter()) {
							final ZonedDateTime windowEndWithSlotOrPortTime = slot.getSchedulingTimeWindow().getEnd();
							if (windowEndWithSlotOrPortTime != null && availabilityStartAfter != null && windowEndWithSlotOrPortTime.isBefore(availabilityStartAfter)) {
								final String message = String.format("Slot|%s is assigned to vessel %s but window date is before the vessel start date.", slot.getName(), vessel.getName());
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
								failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
								failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
								failures.add(failure);
							}
						}
						if (vesselCharter.isSetEndBy()) {
							final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStart();
							if (windowStartWithSlotOrPortTime != null && availabilityEndBy != null && windowStartWithSlotOrPortTime.isAfter(availabilityEndBy)) {
								final String message = String.format("Slot|%s is assigned to vessel %s but window date is after the vessel end date.", slot.getName(), vessel.getName());
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
								failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
								failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
								failures.add(failure);
							}
						}
					}

				} else if (assignment instanceof VesselEvent vesselEvent) {
					if (vesselCharter.isSetStartAfter()) {
						final ZonedDateTime eventStartBy = vesselEvent.getStartByAsDateTime();
						if (eventStartBy != null && availabilityStartAfter != null && eventStartBy.isBefore(availabilityStartAfter)) {
							final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is before the vessel start date.", vesselEvent.getName(), vessel.getName());
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
							failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
							failures.add(failure);
						}
					}
					if (vesselCharter.isSetEndBy()) {
						final ZonedDateTime eventStartAfter = vesselEvent.getStartAfterAsDateTime();
						if (eventStartAfter != null && availabilityEndBy != null && eventStartAfter.isAfter(availabilityEndBy)) {
							final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is after the vessel end date.", vesselEvent.getName(), vessel.getName());
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
							failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
							failures.add(failure);
						}
					}
				}
			}
		}
	}
}
