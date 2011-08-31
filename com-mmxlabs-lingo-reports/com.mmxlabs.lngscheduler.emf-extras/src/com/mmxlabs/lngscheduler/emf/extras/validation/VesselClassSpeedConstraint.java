/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

/**
 * Checks that the min/max speeds lie within fuel consumption curve range for
 * all vessel classes.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselClassSpeedConstraint extends AbstractModelConstraint {
	private static final String MIN_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_min_speed";
	private static final String MAX_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_max_speed";
	private static final String ORDER_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_speed_order";

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final VesselStateAttributes laden = vesselClass
					.getLadenAttributes();
			final VesselStateAttributes ballast = vesselClass
					.getBallastAttributes();

			if (laden == null || ballast == null)
				return ctx.createSuccessStatus();

			if (ctx.getCurrentConstraintId().equals(ORDER_ID)) {
				// min speed cannot be larger than max speed, but can be equal.
				if (vesselClass.getMinSpeed() > vesselClass.getMaxSpeed()) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(vesselClass.getName(),
									vesselClass.getMinSpeed(),
									vesselClass.getMaxSpeed()));
					
					dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_MinSpeed());
					dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_MaxSpeed());
					return dcsd;
				} else {
					return ctx.createSuccessStatus();
				}
			}

			final float maxMinSpeed = Math.max(getMinimumSpeed(laden),
					getMinimumSpeed(ballast));

			final float minMaxSpeed = Math.min(getMaximumSpeed(laden),
					getMaximumSpeed(ballast));

			final float theSpeed;
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
				final IStatus fail = ctx.createFailureStatus(theSpeed,
						maxMinSpeed, minMaxSpeed);
				final DetailConstraintStatusDecorator detail = new DetailConstraintStatusDecorator(
						(IConstraintStatus) fail);
				detail.addEObjectAndFeature(vesselClass, theAttribute);
				return detail;
			}
		}

		return ctx.createSuccessStatus();
	}

	private final float getMinimumSpeed(final VesselStateAttributes attributes) {
		float value = Float.MAX_VALUE;
		for (final FuelConsumptionLine line : attributes
				.getFuelConsumptionCurve()) {
			value = Math.min(value, line.getSpeed());
		}
		return value;
	}

	private final float getMaximumSpeed(final VesselStateAttributes attributes) {
		float value = Float.MIN_VALUE;
		for (final FuelConsumptionLine line : attributes
				.getFuelConsumptionCurve()) {
			value = Math.max(value, line.getSpeed());
		}
		return value;
	}
}
