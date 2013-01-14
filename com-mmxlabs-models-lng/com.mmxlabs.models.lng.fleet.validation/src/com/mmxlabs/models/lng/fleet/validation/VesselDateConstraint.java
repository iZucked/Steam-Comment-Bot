/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
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
		if (object instanceof Vessel) {
			final Vessel vessel = (Vessel) object;
			// Gather start/end requirements
			
			VesselAvailability availability = vessel.getAvailability();
			
			if (availability != null) {
				// Gather dates
				final Date startStart = availability.getStartAfter();
				final Date startEnd = availability.getStartBy();

				final Date endStart = availability.getEndAfter();
				final Date endEnd = availability.getEndBy();

				final Date s = startStart == null ? startEnd : startStart;
				final Date e = endEnd == null ? endStart : endEnd;

				if ((s != null) && (e != null)) {
					if (e.before(s)) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName()));
						if (startStart != null) {
							status.addEObjectAndFeature(availability, FleetPackage.eINSTANCE.getVesselAvailability_StartAfter());
						}
						if (startEnd != null) {
							status.addEObjectAndFeature(availability, FleetPackage.eINSTANCE.getVesselAvailability_StartBy());
						}
						if (endStart != null) {
							status.addEObjectAndFeature(availability, FleetPackage.eINSTANCE.getVesselAvailability_EndAfter());
						}
						if (endEnd != null) {
							status.addEObjectAndFeature(availability, FleetPackage.eINSTANCE.getVesselAvailability_EndBy());
						}
						return status;
					}
				}
			}
		}

		return ctx.createSuccessStatus();
	}
}
