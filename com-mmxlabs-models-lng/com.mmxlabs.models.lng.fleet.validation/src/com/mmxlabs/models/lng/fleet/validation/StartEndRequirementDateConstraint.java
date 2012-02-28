/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.mmxcore.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.mmxcore.validation.context.ValidationSupport;

/**
 * Checks that the dates for start and end requirements are sane.
 * 
 * @author hinton
 * 
 */
public class StartEndRequirementDateConstraint extends AbstractModelConstraint {
	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailablility) {
			final VesselAvailablility pat = (VesselAvailablility) target;
			if (pat.isSetStartTime() && pat.isSetEndTime()) {
				if (pat.getStartTime().after(pat.getEndTime())) {
					final Pair<EObject, EReference> container = ValidationSupport.getInstance().getContainer(pat);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
							((Vessel)container.getFirst()).getName(),
							(container.getSecond() == FleetPackage.eINSTANCE.getVessel_StartRequirement() ? "start" : "end")));
					dcsd.addEObjectAndFeature(pat, FleetPackage.eINSTANCE.getPortAndTime_StartTime());
					dcsd.addEObjectAndFeature(pat, FleetPackage.eINSTANCE.getPortAndTime_EndTime());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
