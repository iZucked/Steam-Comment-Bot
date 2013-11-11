/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class ShippingDaysRestrictionConstraint extends AbstractModelMultiConstraint {

	private int getMinRouteTimeInHours(final Slot from, final Slot to, final LNGScenarioModel lngScenarioModel, final Vessel vessel, final double maxSpeedKnots) {

		int minDuration = Integer.MAX_VALUE;
		for (final Route route : lngScenarioModel.getPortModel().getRoutes()) {
			assert route != null;
			final Port fromPort = from.getPort();
			final Port toPort = to.getPort();
			if (fromPort != null && toPort != null) {

				final int distance = getDistance(route, fromPort, toPort);

				int extraTime = 0;
				for (final VesselClassRouteParameters vcrp : vessel.getVesselClass().getRouteParameters()) {
					if (vcrp.getRoute().equals(route)) {
						extraTime = vcrp.getExtraTransitTime();
					}
				}
				final double travelTime = distance / maxSpeedKnots;
				final int totalTime = (int) (Math.round(travelTime) + extraTime);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	public int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to) {
		for (final RouteLine dl : route.getLines()) {
			if (dl.getFrom().equals(from) && dl.getTo().equals(to)) {
				return dl.getDistance();
			}
		}
		return Integer.MAX_VALUE;
	}

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		// Valid slot data checks
		if (object instanceof Slot) {
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (loadSlot.isDESPurchase()) {
					if (loadSlot.getPort().getCapabilities().contains(PortCapability.LOAD)) {
						if (loadSlot.getShippingDaysRestriction() > 90) {
							final String message = String.format("DES Purchase|%s shipping days restriction is too big.", loadSlot.getName());
							final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
							dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
							failures.add(dsd);
						}
					}
				}
			} else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				if (dischargeSlot.isFOBSale()) {
					if (dischargeSlot.getPort().getCapabilities().contains(PortCapability.DISCHARGE)) {
						if (dischargeSlot.getShippingDaysRestriction() > 90) {
							final String message = String.format("FOB Sale|%s shipping days restriction is too big.", dischargeSlot.getName());
							final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
							dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
							failures.add(dsd);
						}
					}
				}
			}
		}

		// Shipping checks
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			if (cargo.getCargoType() != CargoType.FLEET) {
				final MMXRootObject scenario = Activator.getDefault().getExtraValidationContext().getRootObject();
				if (scenario instanceof LNGScenarioModel) {

					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
					final AssignmentModel assignmentModel = lngScenarioModel.getPortfolioModel().getAssignmentModel();

					if (cargo.getCargoType() == CargoType.DES) {
						LoadSlot desPurchase = null;
						DischargeSlot dischargeSlot = null;
						for (final Slot s : cargo.getSlots()) {
							if (s instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) s;
								if (loadSlot.isDESPurchase() && loadSlot.getPort().getCapabilities().contains(PortCapability.LOAD)) {
									desPurchase = loadSlot;
								}
							} else if (s instanceof DischargeSlot) {
								dischargeSlot = (DischargeSlot) s;
							}
						}
						// Found a slot to validate
						if (desPurchase != null && dischargeSlot != null) {

							final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, desPurchase);
							if (elementAssignment != null && elementAssignment.getAssignment() instanceof Vessel) {
								final Vessel vessel = (Vessel) elementAssignment.getAssignment();
								final double maxSpeedKnots = vessel.getVesselClass().getMaxSpeed();

								final int loadDurationInHours = desPurchase.getSlotOrPortDuration();
								final int dischargeDurationInHours = dischargeSlot.getSlotOrPortDuration();

								final int ladenTravelTimeInHours = getMinRouteTimeInHours(desPurchase, dischargeSlot, lngScenarioModel, vessel, maxSpeedKnots);
								final int ballastTravelTimeInHours = getMinRouteTimeInHours(dischargeSlot, desPurchase, lngScenarioModel, vessel, maxSpeedKnots);

								// Calculate minumum time due to slot windows
								final int ladenWindowInHours;
								{
									// TODO: check overlaps
									final Date windowEndWithSlotOrPortTime = desPurchase.getWindowEndWithSlotOrPortTime();
									final Date windowStartWithSlotOrPortTime = dischargeSlot.getWindowStartWithSlotOrPortTime();

									if (windowEndWithSlotOrPortTime != null && windowStartWithSlotOrPortTime != null) {
										ladenWindowInHours = Math.max(0, (int) ((windowStartWithSlotOrPortTime.getTime() - windowEndWithSlotOrPortTime.getTime()) / Timer.ONE_HOUR)
												- (loadDurationInHours));
									} else {
										return Activator.PLUGIN_ID;
									}
								}
								// Smallest amount of time permitted between slots
								final int ladenTimeInHours = Math.max(ladenTravelTimeInHours, ladenWindowInHours);

								// Total min travel time.
								final int totalRoundTripTimeInHours = loadDurationInHours + ladenTimeInHours + dischargeDurationInHours + ballastTravelTimeInHours;

								if (totalRoundTripTimeInHours > desPurchase.getShippingDaysRestriction() * 24) {
									final String message = String.format(
											"DES Purchase|%s is paired with a sale at %s. However the round trip time (%s) is greater than the permitted restriction (%s).", desPurchase.getName(),
											dischargeSlot.getPort().getName(), formatHours(totalRoundTripTimeInHours), formatHours(desPurchase.getShippingDaysRestriction()));
									final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
									dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
									failures.add(dsd);
								}

							}
						}

					} else {

						// FOB?

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
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
}
