/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class EventDurationConstraint extends AbstractModelConstraint {
	private int maxDryDockDurationInDays = 30 * 6;
	private int maxMaintenanceDurationInDays = 30;
	private int maxCharterOutDurationInDays = 365;
	
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) target;
			
			int max;
			String type;
			if (target instanceof MaintenanceEvent) {
				max = maxMaintenanceDurationInDays;
				type = "Maintenance";
			}
			else if (target instanceof DryDockEvent) {
				max = maxDryDockDurationInDays;
				type = "Dry dock";
			}
			else if (target instanceof CharterOutEvent) {
				max = maxCharterOutDurationInDays;
				type = "Charter out";
			}
			else {
				return (IConstraintStatus) ctx.createFailureStatus("No known maximum duration for events of type '" + event.getClass().getName() + "'.");
			}
			
			final int duration = event.getDurationInDays();
			if (duration > max) {
				String message = String.format("%s event duration %d days (> %d days maximum)", type, duration, max);
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(event, CargoPackage.eINSTANCE.getVesselEvent_DurationInDays());
				return dcsd;
			}

		}

		return ctx.createSuccessStatus();
	}
}
