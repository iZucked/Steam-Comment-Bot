/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that the safety heel is not zero.
 * 
 * @author FM
 * 
 */
public class VesselClassSafetyHeelConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Vessel) {
			final Vessel vessel = (Vessel) target;
			vessel.getVesselOrDelegateSafetyHeel();
			
			if (vessel.getVesselOrDelegateSafetyHeel() != 0 || !vessel.isSetSafetyHeel()) {
			
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("'" + vessel.getName() + "'"));

				dcsd.addEObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_SafetyHeel());
				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
