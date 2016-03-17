/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.utils;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class TravelTimeUtils {
	public static int getMinRouteTimeInHours(final LoadSlot desPurchase, Slot from,  final Slot to, final IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final LNGScenarioModel lngScenarioModel,
			final Vessel vessel, final double referenceSpeed) {
		Collection<Route> allowedRoutes = null;
		final PortModel portModel = lngScenarioModel.getReferenceModel().getPortModel();
		try {
			allowedRoutes = shippingDaysSpeedProvider.getValidRoutes(portModel, desPurchase);
		} catch (final org.ops4j.peaberry.ServiceUnavailableException e) {
		}

		if (allowedRoutes == null || allowedRoutes.isEmpty()) {
			allowedRoutes = portModel.getRoutes();
		}

		int minDuration = Integer.MAX_VALUE;
		for (final Route route : allowedRoutes) {
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
				final double travelTime = distance / referenceSpeed;
				final int totalTime = (int) (Math.floor(travelTime) + extraTime);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	private static int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to) {
		for (final RouteLine dl : route.getLines()) {
			if (dl.getFrom().equals(from) && dl.getTo().equals(to)) {
				return dl.getDistance();
			}
		}
		return Integer.MAX_VALUE;
	}
	
	public static String formatHours(final long hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final long remainderHours = hours % 24L;
			final long days = hours / 24L;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}

	public static Pair<LoadSlot, DischargeSlot> getDESSlots(final Cargo cargo) {
		if (cargo.getCargoType() == CargoType.DES) {
			LoadSlot desPurchase = null;
			DischargeSlot dischargeSlot = null;
			for (final Slot s : cargo.getSlots()) {
				if (s instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) s;
					if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
						desPurchase = loadSlot;
					}
				} else if (s instanceof DischargeSlot) {
					dischargeSlot = (DischargeSlot) s;
				}
			}
			return new Pair<LoadSlot, DischargeSlot>(desPurchase, dischargeSlot);
		}
		return new Pair<LoadSlot, DischargeSlot>();
	}

	public static LNGScenarioModel getScenarioModel(final IExtraValidationContext extraContext) {
		final MMXRootObject scenario = extraContext.getRootObject();
		if (scenario instanceof LNGScenarioModel) {
			return (LNGScenarioModel) scenario;
		}
		return null;
	}

	public static double getReferenceSpeed(final IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final VesselClass vesselClass, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		try {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(vesselClass, isLaden);
		} catch (final org.ops4j.peaberry.ServiceUnavailableException e) {
			referenceSpeed = vesselClass.getMaxSpeed();
		}
		return referenceSpeed;
	}

	public static double getReferenceSpeed(final IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final LoadSlot loadSlot, final VesselClass vesselClass, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		try {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(loadSlot, vesselClass, isLaden);
		} catch (final org.ops4j.peaberry.ServiceUnavailableException e) {
			referenceSpeed = vesselClass.getMaxSpeed();
		}
		return referenceSpeed;
	}

}
