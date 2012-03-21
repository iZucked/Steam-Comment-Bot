/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashMap;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.ValidationSupport;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelConstraint {

	private static final String DATE_ORDER_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_order";
	private static final String TRAVEL_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_travel_time";
	private static final String AVAILABLE_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_available_time";

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

			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName()));
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

			final MMXRootObject scenario = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, cargo);
			if (scenario != null) {

				double maxSpeedKnots = 0.0;
				final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

				for (final VesselClass vc : fleetModel.getVesselClasses()) {
					maxSpeedKnots = Math.max(vc.getMaxSpeed(), maxSpeedKnots);
				}

				@SuppressWarnings("unchecked")
				Map<Pair<Port, Port>, Integer> minTimes = (Map<Pair<Port, Port>, Integer>) ctx.getCurrentConstraintData();
				final Pair<Port, Port> key = new Pair<Port, Port>(cargo.getLoadSlot().getPort(), cargo.getDischargeSlot().getPort());
				if (minTimes == null) {
					minTimes = new HashMap<Pair<Port, Port>, Integer>();

					for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
						for (final VesselClassRouteParameters parameters : vesselClass.getRouteParameters()) {
							collectMinTimes(minTimes, parameters.getRoute(), parameters.getExtraTransitTime(), vesselClass.getMaxSpeed());
						}
					}
					
					for (final Route route : scenario.getSubModel(PortModel.class).getRoutes()) {
						if (route.isCanal() == false) {
							collectMinTimes(minTimes, route, 0, maxSpeedKnots);
						}
					}

					ctx.putCurrentConstraintData(minTimes);
				}

				final Integer time = minTimes.get(key);

				if (time == null) {
					// distance line is missing
					// TODO customise message for this case.
					// seems like a waste to run the same code twice
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName(), "infinity", availableTime));
					dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port());
					return dsd;
				} else {
					if (time > availableTime) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName(), time, availableTime));
						dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart());
						return dsd;
					}

				}
			}
		}
		return ctx.createSuccessStatus();
	}

	private void collectMinTimes(final Map<Pair<Port, Port>, Integer> minTimes, final Route d, final int extraTime, final double maxSpeed) {
		for (final RouteLine dl : d.getLines()) {
			final Pair<Port, Port> p = new Pair<Port, Port>(dl.getFrom(), dl.getTo());
			final int time = Calculator.getTimeFromSpeedDistance(Calculator.scaleToInt(maxSpeed), dl.getDistance()) + extraTime;
			if (!minTimes.containsKey(p) || (minTimes.get(p) > time)) {
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
		if ((availableTime / 24) > SENSIBLE_TRAVEL_TIME) {

			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName(), availableTime / 24, SENSIBLE_TRAVEL_TIME));
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
			if (cargo.getCargoType().equals(CargoType.FLEET) && (loadSlot != null) && (dischargeSlot != null) && (loadSlot.getWindowStart() != null) && (dischargeSlot.getWindowStart() != null)) {
				final String constraintID = ctx.getCurrentConstraintId();

				final Port loadPort = loadSlot.getPort();
				final Port dischargePort = dischargeSlot.getPort();
				if ((loadPort != null) && (dischargePort != null)) {

					final int availableTime = (int) ((dischargeSlot.getWindowEndWithSlotOrPortTime().getTime() - loadSlot.getWindowStartWithSlotOrPortTime().getTime()) / Timer.ONE_HOUR)
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
		}

		return ctx.createSuccessStatus();
	}
}
