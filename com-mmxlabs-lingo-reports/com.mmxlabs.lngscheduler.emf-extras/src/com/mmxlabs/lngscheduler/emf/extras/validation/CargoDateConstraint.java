/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import java.util.HashMap;
import java.util.Map;

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
import scenario.fleet.VesselClassCost;
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelConstraint {
	private static final String DATE_ORDER_ID = "com.mmxlabs.lngscheduler.emf-extras.constraints.cargo_order";
	private static final String TRAVEL_TIME_ID = "com.mmxlabs.lngscheduler.emf-extras.constraints.cargo_travel_time";
	private static final String AVAILABLE_TIME_ID = "com.mmxlabs.lngscheduler.emf-extras.constraints.cargo_available_time";

	/**
	 * This is the maximum sensible amount of travel time in a cargo, in days
	 */
	private static final int SENSIBLE_TRAVEL_TIME = 80;

	/**
	 * Validate that the available time is not negative.
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private IStatus validateSlotOrder(final IValidationContext ctx, final Cargo cargo, final int availableTime) {
		if (availableTime < 0) {

			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId()));
			status.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart());
			return status;
		}
		return ctx.createSuccessStatus();
	}

	/**
	 * Validate that the available time is enough to get from A to B, if it's not negative
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private IStatus validateSlotTravelTime(final IValidationContext ctx, final Cargo cargo, final int availableTime) {
		if (availableTime >= 0) {
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();

			final ValidationSupport validationSupport = ValidationSupport.getInstance();

			EObject container = validationSupport.getContainer(cargo).getFirst();
			while (container != null && !(container instanceof Scenario)) {
				container = validationSupport.getContainer(container).getFirst();
			}
			if (container instanceof Scenario) {
				final Scenario scenario = (Scenario) container;

				float maxSpeedKnots = 0;
				for (final VesselClass vc : scenario.getFleetModel().getVesselClasses()) {
					maxSpeedKnots = Math.max(vc.getMaxSpeed(), maxSpeedKnots);
				}

				Map<Pair<Port, Port>, Integer> minTimes = (Map<Pair<Port, Port>, Integer>) ctx.getCurrentConstraintData();
				final Pair<Port, Port> key = new Pair<Port, Port>(cargo.getLoadSlot().getPort(), cargo.getDischargeSlot().getPort());
				if (minTimes == null) {
					minTimes = new HashMap<Pair<Port, Port>, Integer>();

					collectMinTimes(minTimes, scenario.getDistanceModel(), 0, maxSpeedKnots);
					
					for (final VesselClass vc : scenario.getFleetModel().getVesselClasses()) {
						for (final VesselClassCost vcc : vc.getCanalCosts()) {
							collectMinTimes(minTimes, vcc.getCanal().getDistanceModel(), vcc.getTransitTime(), vc.getMaxSpeed());
						}
					}

					ctx.putCurrentConstraintData(minTimes);
				}

				final Integer time = minTimes.get(key);

				if (time == null) {
					// distance line is missing
					// TODO customize message for this case.
					// seems like a waste to run the same code twice
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId(), "infinity", availableTime));
					dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port());
					return dsd;
				} else {
					if (time > availableTime) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId(), time, availableTime));
						dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart());
						return dsd;
					}

				}
			}
		}
		return ctx.createSuccessStatus();
	}

	private void collectMinTimes(Map<Pair<Port, Port>, Integer> minTimes, final DistanceModel d, int extraTime, float maxSpeed) {
		for (final DistanceLine dl : d.getDistances()) {
			final Pair<Port, Port> p = new Pair<Port, Port>(dl.getFromPort(), dl.getToPort());
			final int time = Calculator.getTimeFromSpeedDistance(Calculator.scaleToInt(maxSpeed), dl.getDistance()) + extraTime;
			if (!minTimes.containsKey(p) || minTimes.get(p) > time) {
				minTimes.put(p, time);
			}
		}
	}

	/**
	 * Validate that a ridiculous amount of time has not been allocated
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private IStatus validateSlotAvailableTime(final IValidationContext ctx, final Cargo cargo, final int availableTime) {
		if (availableTime / 24 > SENSIBLE_TRAVEL_TIME) {

			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId(), availableTime / 24, SENSIBLE_TRAVEL_TIME));
			status.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart());
			return status;
		}

		return ctx.createSuccessStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			if (cargo.getCargoType().equals(CargoType.FLEET) && loadSlot != null && dischargeSlot != null && cargo.getLoadSlot().getWindowStart() != null
					&& cargo.getDischargeSlot().getWindowStart() != null) {
				final String constraintID = ctx.getCurrentConstraintId();

				final int availableTime = (int) ((dischargeSlot.getWindowEnd().getTime() - loadSlot.getWindowStart().getDateWithDefaults(loadSlot.getPort()).getTime()) / Timer.ONE_HOUR)
						- (loadSlot.getSlotOrPortDuration());

				if (constraintID.equals(DATE_ORDER_ID)) {
					return validateSlotOrder(ctx, cargo, availableTime);
				} else if (constraintID.equals(TRAVEL_TIME_ID)) {
					return validateSlotTravelTime(ctx, cargo, availableTime);
				} else if (constraintID.equals(AVAILABLE_TIME_ID)) {
					return validateSlotAvailableTime(ctx, cargo, availableTime);
				}
			}
		}

		return ctx.createSuccessStatus();
	}
}
