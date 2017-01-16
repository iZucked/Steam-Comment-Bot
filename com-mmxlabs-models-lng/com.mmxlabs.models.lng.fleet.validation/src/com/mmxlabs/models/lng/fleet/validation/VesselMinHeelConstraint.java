/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class VesselMinHeelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Vessel) {
			final Vessel vessel = (Vessel) target;

			if (vessel.isSetCapacity() || vessel.isSetFillCapacity()) {

				final VesselClass vesselClass = vessel.getVesselClass();

				double effectiveCapacity = vessel.getVesselOrVesselClassCapacity() * vessel.getVesselOrVesselClassFillCapacity();
				if (vesselClass.getMinHeel() > effectiveCapacity) {
					final String message = String.format("Minimum heel should be less than fill capacity");
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					if (vessel.isSetCapacity()) {
						dcsd.addEObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_Capacity());
					}
					if (vessel.isSetFillCapacity()) {
						dcsd.addEObjectAndFeature(vessel, FleetPackage.eINSTANCE.getVessel_FillCapacity());
					}
					statuses.add(dcsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
