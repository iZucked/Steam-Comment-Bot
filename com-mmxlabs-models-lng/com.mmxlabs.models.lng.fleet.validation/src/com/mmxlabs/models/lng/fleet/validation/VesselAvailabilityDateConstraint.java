/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

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
		final IExtraValidationContext extraContext = Activator.getDefault().getExtraValidationContext();
		if (target instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) target;
			if (va.isSetStartAfter() && va.isSetStartBy()) {
				if (va.getStartAfter().after(va.getStartBy())) {
					
					final Pair<EObject, EReference> container = 
							new Pair<EObject, EReference>(extraContext.getContainer(va), extraContext.getContainment(va));
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(((Vessel) container.getFirst()).getName(), "start"));
					dcsd.addEObjectAndFeature(va, FleetPackage.eINSTANCE.getVesselAvailability_StartAfter());
					dcsd.addEObjectAndFeature(va, FleetPackage.eINSTANCE.getVesselAvailability_StartBy());
					return dcsd;
				}
			}

			if (va.isSetEndAfter() && va.isSetEndBy()) {
				if (va.getEndAfter().after(va.getEndBy())) {
					final Pair<EObject, EReference> container = new Pair<EObject, EReference>(extraContext.getContainer(va), extraContext.getContainment(va));
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(((Vessel) container.getFirst()).getName(), "end"));
					dcsd.addEObjectAndFeature(va, FleetPackage.eINSTANCE.getVesselAvailability_EndAfter());
					dcsd.addEObjectAndFeature(va, FleetPackage.eINSTANCE.getVesselAvailability_EndBy());
					return dcsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
