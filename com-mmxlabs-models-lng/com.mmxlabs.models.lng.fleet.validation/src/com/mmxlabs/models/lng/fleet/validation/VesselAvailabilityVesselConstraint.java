package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselAvailabilityVesselConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			VesselAvailability availability = (VesselAvailability) target;
			if (availability.getVessel() == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Vessel must be specified."));
				dcsd.addEObjectAndFeature(availability, FleetPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}

}
