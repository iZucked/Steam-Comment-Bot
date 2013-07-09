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

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class EventWindowConstraint extends AbstractModelConstraint {
	private int maxWindowSizeInDays = 6 * 30;
	
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) target;
			
			final long windowSizeInMs = event.getStartBy().getTime() - event.getStartAfter().getTime();
			final long windowSizeInDays = windowSizeInMs / (1000 * 24 * 3600);
					
			if (windowSizeInDays > maxWindowSizeInDays) {
				String message = String.format("Event duration %d days (> %d days maximum)", windowSizeInDays, maxWindowSizeInDays);
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(event, FleetPackage.eINSTANCE.getVesselEvent_StartAfter());
				dcsd.addEObjectAndFeature(event, FleetPackage.eINSTANCE.getVesselEvent_StartBy());
				return dcsd;
			}

		}

		return ctx.createSuccessStatus();
	}
}
