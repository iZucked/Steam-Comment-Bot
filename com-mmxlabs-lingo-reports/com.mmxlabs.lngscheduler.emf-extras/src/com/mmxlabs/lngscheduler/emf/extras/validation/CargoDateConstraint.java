/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

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

				Map<DistanceModel, Map<Pair<Port, Port>, Integer>> distanceMatrices = (Map<DistanceModel, Map<Pair<Port, Port>, Integer>>) ctx.getCurrentConstraintData();

				if (distanceMatrices == null) {
					distanceMatrices = new HashMap<DistanceModel, Map<Pair<Port, Port>, Integer>>();
					ctx.putCurrentConstraintData(distanceMatrices);
				}

				Map<Pair<Port, Port>, Integer> distanceMatrix = distanceMatrices.get(scenario.getDistanceModel());
				if (distanceMatrix == null) {
					distanceMatrix = new HashMap<Pair<Port, Port>, Integer>();
					for (final DistanceLine dl : scenario.getDistanceModel().getDistances()) {
						distanceMatrix.put(new Pair<Port, Port>(dl.getFromPort(), dl.getToPort()), dl.getDistance());
					}
					distanceMatrices.put(scenario.getDistanceModel(), distanceMatrix);
				}
				final Pair<Port, Port> key = new Pair<Port, Port>(loadSlot.getPort(), dischargeSlot.getPort());
				Integer distance = distanceMatrix.get(key);
				for (final Canal c : scenario.getCanalModel().getCanals()) {
					Map<Pair<Port, Port>, Integer> canalMatrix = distanceMatrices.get(c.getDistanceModel());
					if (canalMatrix == null) {
						canalMatrix = new HashMap<Pair<Port, Port>, Integer>();
						for (final DistanceLine dl : c.getDistanceModel().getDistances()) {
							canalMatrix.put(new Pair<Port, Port>(dl.getFromPort(), dl.getToPort()), dl.getDistance());
						}
						distanceMatrices.put(scenario.getDistanceModel(), canalMatrix);
					}
					final Integer distance2 = canalMatrix.get(key);
					if (distance2 != null) {
						if (distance == null || distance2 < distance) {
							distance = distance2;
						}
					}
				}

				if (distance == null) {
					// distance line is missing
					// TODO customize message for this case.
					// seems like a waste to run the same code twice
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId(), "infinity", availableTime));
					dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port());
					return dsd;
				} else {
					if (distance / maxSpeedKnots > availableTime) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getId(), (int) (distance / maxSpeedKnots),
								availableTime));
						dsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart());
						return dsd;
					}

				}
			}
		}
		return ctx.createSuccessStatus();
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

				final int availableTime = (int) ((dischargeSlot.getWindowEnd().getTime() - loadSlot.getWindowStart().getTime()) / Timer.ONE_HOUR);

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
