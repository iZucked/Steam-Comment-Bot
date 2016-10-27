/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.ZonedDateTime;
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
public class VesselAvailabilityOptionalFleetConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			final Vessel vessel = vesselAvailability.getVessel();

			if (vesselAvailability.isFleet() && vesselAvailability.isOptional()) {
					final String message = String.format("[Vessel Availability | %s] A fleet availability cannot be optional", vessel.getName());
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
					failure.addEObjectAndFeature(vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_Optional());
					failure.addEObjectAndFeature(vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_Fleet());
					failure.addEObjectAndFeature(vesselAvailability, CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities());
					failures.add(failure);
			}

		}
		return Activator.PLUGIN_ID;
	}
}
