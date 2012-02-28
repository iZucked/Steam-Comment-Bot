/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.validation.DetailConstraintStatusDecorator;


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

			final Date start = vesselEvent.getEarliestStartDate();
			final Date end = vesselEvent.getLatestStartDate();

			if (start == null || end == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(vesselEvent.getName()));
				if (start == null) {
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_EarliestStartDate());
				}
				if (end == null) {
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_LatestStartDate());
				}
				return dcsd;
			} else {
				if (start.after(end)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(vesselEvent.getName()));
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_EarliestStartDate());
					dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_LatestStartDate());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
