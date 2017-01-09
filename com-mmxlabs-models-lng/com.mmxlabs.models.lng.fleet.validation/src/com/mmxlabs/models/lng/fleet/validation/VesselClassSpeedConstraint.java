/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that the min/max speeds lie within fuel consumption curve range for all vessel classes.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselClassSpeedConstraint extends AbstractModelConstraint {

	private static final String MIN_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.min_speed";
	private static final String MAX_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.max_speed";
	private static final String ORDER_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.speed_order";

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final VesselStateAttributes laden = vesselClass.getLadenAttributes();
			final VesselStateAttributes ballast = vesselClass.getBallastAttributes();

			if (laden == null || ballast == null) {
				return ctx.createSuccessStatus();
			}

			if (ctx.getCurrentConstraintId().equals(ORDER_ID)) {
				// min speed cannot be larger than max speed, but can be equal.
				if (vesselClass.getMinSpeed() > vesselClass.getMaxSpeed()) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'"+vesselClass.getName()+"'", vesselClass.getMinSpeed(),
							vesselClass.getMaxSpeed()));

					dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_MinSpeed());
					dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_MaxSpeed());
					return dcsd;
				} else {
					return ctx.createSuccessStatus();
				}
			}

			final double maxMinSpeed = Math.max(getMinimumSpeed(laden), getMinimumSpeed(ballast));

			final double minMaxSpeed = Math.min(getMaximumSpeed(laden), getMaximumSpeed(ballast));

			final double theSpeed;
			final EAttribute theAttribute;
			if (ctx.getCurrentConstraintId().equals(MIN_ID)) {
				theSpeed = vesselClass.getMinSpeed();
				theAttribute = FleetPackage.eINSTANCE.getVesselClass_MinSpeed();
			} else if (ctx.getCurrentConstraintId().equals(MAX_ID)) {
				theSpeed = vesselClass.getMaxSpeed();
				theAttribute = FleetPackage.eINSTANCE.getVesselClass_MaxSpeed();
			} else {
				return ctx.createSuccessStatus();
			}

			if (theSpeed < maxMinSpeed || theSpeed > minMaxSpeed) {
				final IStatus fail = ctx.createFailureStatus("'"+vesselClass.getName()+"'", theSpeed, maxMinSpeed, minMaxSpeed);
				final DetailConstraintStatusDecorator detail = new DetailConstraintStatusDecorator((IConstraintStatus) fail);
				detail.addEObjectAndFeature(vesselClass, theAttribute);
				return detail;
			}
		}

		return ctx.createSuccessStatus();
	}

	private final double getMinimumSpeed(final VesselStateAttributes attributes) {
		double value = Float.MAX_VALUE;
		for (final FuelConsumption line : attributes.getFuelConsumption()) {
			value = Math.min(value, line.getSpeed());
		}
		return value;
	}

	private final double getMaximumSpeed(final VesselStateAttributes attributes) {
		double value = Float.MIN_VALUE;
		for (final FuelConsumption line : attributes.getFuelConsumption()) {
			value = Math.max(value, line.getSpeed());
		}
		return value;
	}
}
