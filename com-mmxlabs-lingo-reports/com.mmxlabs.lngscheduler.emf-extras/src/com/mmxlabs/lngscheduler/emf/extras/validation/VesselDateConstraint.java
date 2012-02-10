/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

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
			final PortAndTime start = vessel.getStartRequirement();
			final PortAndTime end = vessel.getEndRequirement();

			if ((start != null) && (end != null)) {
				// Gather dates
				final Date startStart = start.getStartTime();
				final Date startEnd = start.getEndTime();

				final Date endStart = end.getStartTime();
				final Date endEnd = end.getEndTime();

				final Date s = startStart == null ? startEnd : startStart;
				final Date e = endEnd == null ? endStart : endEnd;

				if ((s != null) && (e != null)) {
					if (e.before(s)) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName()));
						if (startStart != null) {
							status.addEObjectAndFeature(start, FleetPackage.eINSTANCE.getPortAndTime_StartTime());
						}
						if (startEnd != null) {
							status.addEObjectAndFeature(start, FleetPackage.eINSTANCE.getPortAndTime_EndTime());
						}
						if (endStart != null) {
							status.addEObjectAndFeature(end, FleetPackage.eINSTANCE.getPortAndTime_StartTime());
						}
						if (endEnd != null) {
							status.addEObjectAndFeature(end, FleetPackage.eINSTANCE.getPortAndTime_EndTime());
						}
						return status;
					}
				}
			}
		}

		return ctx.createSuccessStatus();
	}
}
