/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks for duplicate speed fuel curve entries
 * 
 * @author Simon Goodall
 * 
 */
public class VesselClassSpeedCurveConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			{
				final VesselStateAttributes laden = vesselClass.getLadenAttributes();
				final Set<Double> seenSpeeds = new HashSet<Double>();
				for (final FuelConsumption line : laden.getFuelConsumption()) {
					if (!seenSpeeds.add(line.getSpeed())) {
						final String message = String.format("Vessel Class %s has duplicate speed entry of %2.2f in Laden attributes", vesselClass.getName(), line.getSpeed());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_LadenAttributes());
						statuses.add(dcsd);
					}
				}
			}
			{
				final VesselStateAttributes ballast = vesselClass.getBallastAttributes();
				final Set<Double> seenSpeeds = new HashSet<Double>();
				for (final FuelConsumption line : ballast.getFuelConsumption()) {
					if (!seenSpeeds.add(line.getSpeed())) {
						final String message = String.format("Vessel Class %s has duplicate speed entry of %2.2f in ballast attributes", vesselClass.getName(), line.getSpeed());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_LadenAttributes());
						statuses.add(dcsd);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
