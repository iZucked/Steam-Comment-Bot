/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelMultiConstraint {

	private static final String DATE_ORDER_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_order";
	private static final String TRAVEL_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_travel_time";
	private static final String AVAILABLE_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_available_time";

	/**
	 * This is the maximum sensible amount of travel time in a cargo, in days
	 */
	private static final int SENSIBLE_TRAVEL_TIME = 160;

	/**
	 * Validate that the available time is not negative.
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotOrder(final IValidationContext ctx, final Cargo cargo, Slot slot, final int availableTime, final boolean inDialog, List<IStatus> failures) {
		if (availableTime < 0) {
			final int severity = inDialog ? IStatus.WARNING : IStatus.ERROR;
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName() + "'"), severity);
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			failures.add(status);
		}
	}

	private String formatHours(final int hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final int remainderHours = hours % 24;
			final int days = hours / 24;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}

	/**
	 * Validate that the available time is enough to get from A to B, if it's not negative
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotTravelTime(final IValidationContext ctx, final Cargo cargo, Slot from, Slot to, final int availableTime, final boolean inDialog, List<IStatus> failures) {
		
		// Skip for FOB/DES cargoes. 
		// TODO: Roll in common des redirection travel time
		if (cargo.getCargoType() != CargoType.FLEET) {
			return;
		}
		
		if (availableTime >= 0) {

			final MMXRootObject scenario = Activator.getDefault().getExtraValidationContext().getRootObject();
			if (scenario instanceof LNGScenarioModel) {

				double maxSpeedKnots = 0.0;
				final FleetModel fleetModel = ((LNGScenarioModel) scenario).getFleetModel();

				if (fleetModel.getVesselClasses().isEmpty()) {
					// Cannot perform our validation, so return
					return;
				}

				for (final VesselClass vc : fleetModel.getVesselClasses()) {
					maxSpeedKnots = Math.max(vc.getMaxSpeed(), maxSpeedKnots);
				}

				@SuppressWarnings("unchecked")
				Map<Pair<Port, Port>, Integer> minTimes = (Map<Pair<Port, Port>, Integer>) ctx.getCurrentConstraintData();
				final Pair<Port, Port> key = new Pair<Port, Port>(from.getPort(), to.getPort());
				if (minTimes == null) {
					minTimes = new HashMap<Pair<Port, Port>, Integer>();

					for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
						for (final VesselClassRouteParameters parameters : vesselClass.getRouteParameters()) {
							collectMinTimes(minTimes, parameters.getRoute(), parameters.getExtraTransitTime(), vesselClass.getMaxSpeed());
						}
					}

					for (final Route route : ((LNGScenarioModel) scenario).getPortModel().getRoutes()) {
						if (route.isCanal() == false) {
							collectMinTimes(minTimes, route, 0, maxSpeedKnots);
						}
					}

					ctx.putCurrentConstraintData(minTimes);
				}

				final Integer minTime = minTimes.get(key);

				final int severity = inDialog ? IStatus.WARNING : IStatus.ERROR;
				if (minTime == null) {
					// distance line is missing
					// TODO customise message for this case.
					// seems like a waste to run the same code twice
					final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName() + "'", "infinity", formatHours(availableTime));
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, severity);
					dsd.addEObjectAndFeature(from, CargoPackage.eINSTANCE.getSlot_Port());
					failures.add(dsd);
				} else {
					if (minTime > availableTime) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName() + "'", formatHours(minTime
								- availableTime)), (cargo.isAllowRewiring()) ? IStatus.WARNING : severity);
						dsd.addEObjectAndFeature(from, CargoPackage.eINSTANCE.getSlot_WindowStart());
						dsd.addEObjectAndFeature(to, CargoPackage.eINSTANCE.getSlot_WindowStart());
						failures.add(dsd);
					}

				}
			}
		}
	}

	private void collectMinTimes(final Map<Pair<Port, Port>, Integer> minTimes, final Route d, final int extraTime, final double maxSpeed) {
		if (d == null) {
			return;
		}

		for (final RouteLine dl : d.getLines()) {
			final Pair<Port, Port> p = new Pair<Port, Port>(dl.getFrom(), dl.getTo());
			final int time = ((dl.getFullDistance() == 0) ? 0 : Calculator.getTimeFromSpeedDistance(OptimiserUnitConvertor.convertToInternalSpeed(maxSpeed), dl.getFullDistance())) + extraTime;
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
	private void validateSlotAvailableTime(final IValidationContext ctx, final Cargo cargo, Slot slot, final int availableTime, final boolean inDialog, List<IStatus> failures) {
		if ((availableTime / 24) > SENSIBLE_TRAVEL_TIME) {
			final int severity = inDialog ? IStatus.WARNING : IStatus.ERROR;
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName() + "'", availableTime / 24,
					SENSIBLE_TRAVEL_TIME), severity);
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			failures.add(status);
		}

	}

	@Override
	protected String validate(IValidationContext ctx, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		boolean inDialog = false;

		// Simple check that may indicate that we are in a dialog rather the full validation.
		if (object.eContainer() == null) {
			inDialog = true;
		}

		if (object instanceof Cargo) {
			final String constraintID = ctx.getCurrentConstraintId();
			final Cargo cargo = (Cargo) object;
			// final Slot loadSlot = cargo.getLoadSlot();
			// final Slot dischargeSlot = cargo.getDischargeSlot();
//			if (cargo.getCargoType().equals(CargoType.FLEET)) {

				// && (loadSlot != null) && (dischargeSlot != null) && (loadSlot.getWindowStart() != null) && (dischargeSlot.getWindowStart() != null)) {
				// }
				Slot prevSlot = null;
				for (Slot slot : cargo.getSortedSlots()) {
					if (prevSlot != null) {
						final Port loadPort = prevSlot.getPort();
						final Port dischargePort = slot.getPort();
						if ((loadPort != null) && (dischargePort != null)) {

							Date windowEndWithSlotOrPortTime = slot.getWindowEndWithSlotOrPortTime();
							Date windowStartWithSlotOrPortTime = prevSlot.getWindowStartWithSlotOrPortTime();

							if (windowEndWithSlotOrPortTime != null && windowStartWithSlotOrPortTime != null) {
								final int availableTime = (int) ((windowEndWithSlotOrPortTime.getTime() - windowStartWithSlotOrPortTime.getTime()) / Timer.ONE_HOUR)
										- (prevSlot.getSlotOrPortDuration());

								if (constraintID.equals(DATE_ORDER_ID)) {
									validateSlotOrder(ctx, cargo, prevSlot, availableTime, inDialog, failures);
								} else if (constraintID.equals(TRAVEL_TIME_ID)) {
									validateSlotTravelTime(ctx, cargo, prevSlot, slot, availableTime, inDialog, failures);
								} else if (constraintID.equals(AVAILABLE_TIME_ID)) {
									validateSlotAvailableTime(ctx, cargo, prevSlot, availableTime, inDialog, failures);
								}
							}
						}
					}
					prevSlot = slot;
				}
//			}
		}

		return Activator.PLUGIN_ID;
	}
}
