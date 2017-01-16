/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDateTime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that vessel events have a start time before their end time.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselEventDateConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;

			final LocalDateTime start = vesselEvent.getStartAfter();
			final LocalDateTime end = vesselEvent.getStartBy();

			if (start == null || end == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselEvent.getName()));
				if (start == null) {
					dcsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
				}
				if (end == null) {
					dcsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
				}
				return dcsd;
			} else {
				if (start.isAfter(end)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselEvent.getName()));
					dcsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
					dcsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
