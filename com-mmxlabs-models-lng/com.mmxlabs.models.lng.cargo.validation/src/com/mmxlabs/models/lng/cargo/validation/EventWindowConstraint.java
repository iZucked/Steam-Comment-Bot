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

import com.mmxlabs.common.time.Days;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.BaseFuel;
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

			if (event.getStartAfter() != null && event.getStartBy() != null) {

				final int windowSizeInDays = Days.between(event.getStartAfter(), event.getStartBy());

				if (windowSizeInDays > maxWindowSizeInDays) {
					String message = String.format("Event duration %d days (> %d days maximum)", windowSizeInDays, maxWindowSizeInDays);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(event, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
					dcsd.addEObjectAndFeature(event, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
					return dcsd;
				}
			}

		}

		return ctx.createSuccessStatus();
	}
}
