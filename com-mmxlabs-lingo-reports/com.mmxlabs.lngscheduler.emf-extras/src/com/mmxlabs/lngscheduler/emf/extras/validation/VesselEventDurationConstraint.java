/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselEvent;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Checks that vessel events have a duration geater than zero.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselEventDurationConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;

			final int duration = vesselEvent.getDuration();

			if (duration < 1) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselEvent.getId()));
				dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_Duration());

				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
