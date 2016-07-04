/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that the dates for start and end requirements are sane.
 * 
 * @author hinton
 * 
 */
public class VesselAvailabilityDateConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) target;
			if (va.isSetStartAfter() && va.isSetStartBy()) {
				if (va.getStartAfter().isAfter(va.getStartBy())) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(va.getVessel().getName(), "start"));
					dcsd.addEObjectAndFeature(va, CargoPackage.eINSTANCE.getVesselAvailability_StartAfter());
					dcsd.addEObjectAndFeature(va, CargoPackage.eINSTANCE.getVesselAvailability_StartBy());
					return dcsd;
				}
			}

			if (va.isSetEndAfter() && va.isSetEndBy()) {
				if (va.getEndAfter().isAfter(va.getEndBy())) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(va.getVessel().getName(), "end"));
					dcsd.addEObjectAndFeature(va, CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
					dcsd.addEObjectAndFeature(va, CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
