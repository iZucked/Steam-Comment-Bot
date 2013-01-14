/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that vessel events have a duration greater than zero.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselEventDurationConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;

			final int duration = vesselEvent.getDurationInDays();

			if (duration < 1) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselEvent.getName()));
				dcsd.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_DurationInDays());

				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
