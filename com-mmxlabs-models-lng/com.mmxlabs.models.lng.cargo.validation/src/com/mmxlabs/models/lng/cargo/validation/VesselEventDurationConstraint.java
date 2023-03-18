/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterLengthEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that vessel events have a duration greater than its minimum.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselEventDurationConstraint extends AbstractModelConstraint {
	private static final int minDurationCharterLength = 14;
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof final VesselEvent vesselEvent) {
			final int duration = vesselEvent.getDurationInDays();
			int min = 1;
			if(vesselEvent instanceof CharterLengthEvent) {
				min = minDurationCharterLength;
			}
			if (duration < min) {				
				final String msg = String.format("Vessel event '%s': Duration of %d days is below the minimum of %d days", vesselEvent.getName(), duration, min);
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
				dcsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_DurationInDays());
				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
