/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class CargoTravelTimeUtils {

	public static final List<RouteOption> EMPTY_ROUTES = Collections.emptyList();

	public static int getDivertableDESMinRouteTimeInHours(final @NonNull LoadSlot desPurchase, @NonNull final Slot from, final @NonNull Slot to,
			final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final @NonNull PortModel portModel, final Vessel vessel, final double referenceSpeed) {
		Collection<Route> allowedRoutes = null;
		if (shippingDaysSpeedProvider != null) {
			allowedRoutes = shippingDaysSpeedProvider.getValidRoutes(portModel, desPurchase);
		}
		if (allowedRoutes == null || allowedRoutes.isEmpty()) {
			allowedRoutes = portModel.getRoutes();
		}
		final int minDuration = getMinTimeFromAllowedRoutes(from, to, vessel.getVesselClass(), referenceSpeed, allowedRoutes);
		return minDuration;
	}

	public static int getFobMinTimeInHours(final Slot from, final Slot to, final LocalDate date, final VesselAssignmentType vesselAssignmentType, final PortModel portModel, final CostModel costModel,
			final double referenceSpeed) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, date, portModel, costModel);
		final Pair<VesselClass, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final VesselClass vc = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vc != null ? vc.getMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes);
	}

	private static int getMinTimeFromAllowedRoutes(final Slot from, final Slot to, final VesselClass vesselClass, final double referenceSpeed, final Collection<Route> allowedRoutes) {
		int minDuration = Integer.MAX_VALUE;
		for (final Route route : allowedRoutes) {
			assert route != null;
			final Port fromPort = from.getPort();
			final Port toPort = to.getPort();
			if (fromPort != null && toPort != null) {
				final int totalTime = TravelTimeUtils.getTimeForRoute(vesselClass, referenceSpeed, route, fromPort, toPort);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	

	private static List<Route> getAllowedRoutes(final VesselAssignmentType vesselAssignmentType, final LocalDate date, final PortModel portModel, final CostModel costModel) {
		if (vesselAssignmentType == null) {
			// allow all routes if not on a vessel
			return portModel.getRoutes();
		}
		final List<Route> routes = new LinkedList<Route>();
		final Pair<VesselClass, List<RouteOption>> vatData = getVesselAssignmentTypeData(vesselAssignmentType);
		for (final Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == RouteOption.PANAMA
					&& (costModel.getPanamaCanalTariff() != null && costModel.getPanamaCanalTariff().getAvailableFrom() != null && costModel.getPanamaCanalTariff().getAvailableFrom().isAfter(date))) {
				// Panama is closed
				continue;
			}
			if (!vatData.getSecond().contains(route.getRouteOption()) && !vatData.getFirst().getInaccessibleRoutes().contains(route.getRouteOption())) {
				routes.add(route);
			}
		}
		return routes;
	}

	public static Pair<VesselClass, List<RouteOption>> getVesselAssignmentTypeData(final VesselAssignmentType vesselAssignmentType) {
		VesselClass vc;
		List<RouteOption> vesselDisabledRoutes;
		if (vesselAssignmentType == null) {
			return new Pair<VesselClass, List<RouteOption>>(null, null);
		}
		if (vesselAssignmentType instanceof CharterInMarket) {
			final CharterInMarket cim = ((CharterInMarket) vesselAssignmentType);
			vc = cim.getVesselClass();
			vesselDisabledRoutes = cim.isOverrideInaccessibleRoutes() ? cim.getInaccessibleRoutes() : EMPTY_ROUTES;
		} else {
			final VesselAvailability v = (VesselAvailability) vesselAssignmentType;
			vc = v.getVessel().getVesselClass();
			vesselDisabledRoutes = v.getVessel().isOverrideInaccessibleRoutes() ? v.getVessel().getInaccessibleRoutes() : EMPTY_ROUTES;
		}
		return new Pair(vc, vesselDisabledRoutes);
	}

	private static List<Route> processRouteOptions(final EList<Route> routes, final EList<RouteOption> inaccessibleRoutes) {
		final List<Route> newRoutes = new LinkedList<>();
		for (final Route route : routes) {
			for (final RouteOption routeOption : inaccessibleRoutes) {
				if (route.getRouteOption().equals(routeOption)) {
					routes.add(route);
					break;
				}
			}
		}
		return newRoutes;
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

	public static double getReferenceSpeed(final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final VesselClass vesselClass, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		if (shippingDaysSpeedProvider != null) {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(vesselClass, isLaden);
		} else {
			referenceSpeed = vesselClass.getMaxSpeed();
		}
		return referenceSpeed;
	}

	public static double getReferenceSpeed(final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final LoadSlot loadSlot, final VesselClass vesselClass,
			final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		if (shippingDaysSpeedProvider != null) {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(loadSlot, vesselClass, isLaden);
		} else {
			referenceSpeed = vesselClass.getMaxSpeed();
		}
		return referenceSpeed;
	}

}
