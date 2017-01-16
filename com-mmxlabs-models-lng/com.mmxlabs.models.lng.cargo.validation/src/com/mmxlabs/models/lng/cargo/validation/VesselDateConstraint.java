/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
  * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDateTime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Check that the end requirement of the vessel is after the start requirement.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselDateConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselAvailability) {
//			final Vessel vessel = (Vessel) object;
			// Gather start/end requirements
			
//			VesselAvailability availability = vessel.getAvailability();
			
//			if (availability != null) {
				// Gather dates
				VesselAvailability availability = (VesselAvailability) object;
				final LocalDateTime startStart = availability.getStartAfter();
				final LocalDateTime startEnd = availability.getStartBy();

				final LocalDateTime endStart = availability.getEndAfter();
				final LocalDateTime endEnd = availability.getEndBy();

				final LocalDateTime s = startStart == null ? startEnd : startStart;
				final LocalDateTime e = endEnd == null ? endStart : endEnd;

				if ((s != null) && (e != null)) {
					if (e.isBefore(s)) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(availability.getVessel().getName()));
						if (startStart != null) {
							status.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartAfter());
						}
						if (startEnd != null) {
							status.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartBy());
						}
						if (endStart != null) {
							status.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
						}
						if (endEnd != null) {
							status.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
						}
						return status;
					}
				}
			}
//		}

		return ctx.createSuccessStatus();
	}
}
