/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.CargoType;
import scenario.cargo.Slot;
import scenario.fleet.VesselClass;
import scenario.port.DistanceLine;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Check that the end of any cargo's discharge window is not before the start of
 * its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse
	 * .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			if (cargo.getCargoType().equals(CargoType.FLEET)
					&& loadSlot != null && dischargeSlot != null
					&& cargo.getLoadSlot().getWindowStart() != null
					&& cargo.getDischargeSlot().getWindowStart() != null) {

				Object allottedTime = 0;
				Object requiredTime = "some";

				if (dischargeSlot.getWindowEnd().before(
						loadSlot.getWindowStart())) {
					allottedTime = "a negative number (discharge is before load)";
				} else {
					// TODO check travel time feasibility here
					final long maxTravelTime = (dischargeSlot.getWindowEnd()
							.getTime() - loadSlot.getWindowStart().getTime())
							/ Timer.ONE_HOUR;

					// now check the time it would take on the fastest vessel.
					// to do this we have to get the distance matrix, which
					// cargo does not have efficient access to.
					EObject container = cargo.eContainer();
					while (container != null
							&& !(container instanceof Scenario)) {
						container = container.eContainer();
					}
					if (container instanceof Scenario) {
						final Scenario scenario = (Scenario) container;

						float maxSpeedKnots = 0;
						for (final VesselClass vc : scenario.getFleetModel()
								.getVesselClasses()) {
							maxSpeedKnots = Math.max(vc.getMaxSpeed(),
									maxSpeedKnots);
						}

						for (final DistanceLine dl : scenario
								.getDistanceModel().getDistances()) {
							if (dl.getFromPort() == loadSlot.getPort()
									&& dl.getToPort() == dischargeSlot
											.getPort()) {
								final int distance = dl.getDistance();
								if (distance / maxSpeedKnots < maxTravelTime) {
									return ctx.createSuccessStatus();
								} else {
									allottedTime = maxTravelTime;
									requiredTime = (int) (distance / maxSpeedKnots);
									break;
								}
							}
						}
					} else {
						return ctx.createSuccessStatus();
					}
				}
				
				return new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(cargo
								.getId(), allottedTime, requiredTime), dischargeSlot,
						CargoPackage.eINSTANCE.getSlot_WindowStart()
								.getName());
			}
		}
		return ctx.createSuccessStatus();
	}
}
