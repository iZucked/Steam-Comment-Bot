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

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselEvent;

/**
 * Checks that vessel events have a start time before their end time.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselEventDateConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;

			final Date start = vesselEvent.getStartDate();
			final Date end = vesselEvent.getEndDate();

			if (start == null || end == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(vesselEvent.getId()));
				if (start == null) {
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_StartDate());
				}
				if (end == null) {
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_EndDate());
				}
				return dcsd;
			} else {
				if (start.after(end)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(vesselEvent.getId()));
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_StartDate());
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_EndDate());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
