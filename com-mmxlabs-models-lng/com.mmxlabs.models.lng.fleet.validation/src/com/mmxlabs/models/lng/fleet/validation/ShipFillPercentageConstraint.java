/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;


/**
 * Checks that the ship fill percentage is not zero and warns whether it's too small
 * 
 * @author hinton
 * 
 */
public class ShipFillPercentageConstraint extends AbstractModelConstraint {
	
	// TODO: Fix up ID string
	
	public static final String VALIDITY_ID = "com.mmxlabs.models.lng.fleet.validation.ShipFillPercentageConstraint.validity";
	public static final String SANITY_ID = "com.mmxlabs.models.lng.fleet.validation.ShipFillPercentageConstraint.sanity";
	private static final double MIN_SENSIBLE_SFC = 0.8;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			
			boolean failed = false;
			if (ctx.getCurrentConstraintId().equals(VALIDITY_ID)) {
				if (vesselClass.getFillCapacity() <= 0) {
					failed = true;
				} else if (vesselClass.getFillCapacity() > 1) {
					failed = true;
				}
			} else if (ctx.getCurrentConstraintId().equals(SANITY_ID)) {
				if (vesselClass.getFillCapacity() < MIN_SENSIBLE_SFC) {
					failed = true;
				}
			}
			
			if (failed) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselClass.getName(),
						vesselClass.getFillCapacity() * 100, MIN_SENSIBLE_SFC * 100));
				dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_FillCapacity());
				return dcsd;
			}
		}
		
		return ctx.createSuccessStatus();
	}
}
