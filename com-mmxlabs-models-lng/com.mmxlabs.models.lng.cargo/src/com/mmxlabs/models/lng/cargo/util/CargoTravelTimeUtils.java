/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

@NonNullByDefault
public class CargoTravelTimeUtils {
	private CargoTravelTimeUtils() {

	}

	public static final List<RouteOption> EMPTY_ROUTES = Collections.emptyList();

	public static int getDivertibleDESMinRouteTimeInHours(final LoadSlot desPurchase, final Slot from, final Slot to, final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider,
			final PortModel portModel, final Vessel vessel, final double referenceSpeed, final ModelDistanceProvider modelDistanceProvider) {

		Collection<Route> allowedRoutes = null;
		if (shippingDaysSpeedProvider != null) {
			allowedRoutes = shippingDaysSpeedProvider.getValidRoutes(portModel, desPurchase);
		}
		if (allowedRoutes == null || allowedRoutes.isEmpty()) {
			allowedRoutes = portModel.getRoutes();
		}
		return getMinTimeFromAllowedRoutes(from, to, vessel, referenceSpeed, allowedRoutes, modelDistanceProvider);
	}

	public static int getDivertibleFOBMinRouteTimeInHours(final DischargeSlot fobSale, final Slot from, final Slot to, final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider,
			final PortModel portModel, final Vessel vessel, final double referenceSpeed, final ModelDistanceProvider modelDistanceProvider) {

		Collection<Route> allowedRoutes = null;
		if (shippingDaysSpeedProvider != null) {
			allowedRoutes = shippingDaysSpeedProvider.getValidRoutes(portModel, fobSale);
		}
		if (allowedRoutes == null || allowedRoutes.isEmpty()) {
			allowedRoutes = portModel.getRoutes();
		}
		return getMinTimeFromAllowedRoutes(from, to, vessel, referenceSpeed, allowedRoutes, modelDistanceProvider);
	}

	public static int getFobMinTimeInHours(final Slot<?> from, final Slot<?> to, final LocalDate date, final VesselAssignmentType vesselAssignmentType, final PortModel portModel,
			final CostModel costModel, final double referenceSpeed, final ModelDistanceProvider modelDistanceProvider) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, portModel, costModel);
		final Pair<Vessel, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final Vessel vessel = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vessel != null ? vessel.getVesselOrDelegateMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes, modelDistanceProvider);
	}

	private static int getMinTimeFromAllowedRoutes(final Slot from, final Slot to, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			final ModelDistanceProvider modelDistanceProvider) {

		return TravelTimeUtils.getMinTimeFromAllowedRoutes(from.getPort(), to.getPort(), vessel, referenceSpeed, allowedRoutes, modelDistanceProvider);
	}

	private static List<Route> getAllowedRoutes(final VesselAssignmentType vesselAssignmentType, final PortModel portModel, final CostModel costModel) {
		if (vesselAssignmentType == null) {
			// allow all routes if not on a vessel
			return portModel.getRoutes();
		}
		final List<Route> routes = new LinkedList<>();
		final Pair<Vessel, List<RouteOption>> vatData = getVesselAssignmentTypeData(vesselAssignmentType);
		if (vatData.getFirst() == null) {
			return routes;
		}
		for (final Route route : portModel.getRoutes()) {
			if (!vatData.getSecond().contains(route.getRouteOption()) && !vatData.getFirst().getVesselOrDelegateInaccessibleRoutes().contains(route.getRouteOption())) {
				routes.add(route);
			}
		}
		return routes;
	}

	public static Pair<Vessel, List<RouteOption>> getVesselAssignmentTypeData(final VesselAssignmentType vesselAssignmentType) {
		Vessel vessel;
		List<RouteOption> vesselDisabledRoutes;
		if (vesselAssignmentType == null) {
			return new Pair<>();
		}
		if (vesselAssignmentType instanceof CharterInMarket) {
			final CharterInMarket cim = (CharterInMarket) vesselAssignmentType;
			vessel = cim.getVessel();
		} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
			final CharterInMarketOverride cim = (CharterInMarketOverride) vesselAssignmentType;
			vessel = cim.getCharterInMarket().getVessel();
		} else {
			final VesselAvailability va = (VesselAvailability) vesselAssignmentType;
			vessel = va.getVessel();
		}
		if (vessel == null) {
			return new Pair<>();
		}

		vesselDisabledRoutes = vessel.getVesselOrDelegateInaccessibleRoutes();
		return new Pair(vessel, vesselDisabledRoutes);
	}

	public static Pair<@Nullable LoadSlot, @Nullable DischargeSlot> getDESSlots(final Cargo cargo) {
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
			return new Pair<>(desPurchase, dischargeSlot);
		}
		return new Pair<>();
	}

	public static double getReferenceSpeed(final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final Vessel vessel, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		if (shippingDaysSpeedProvider != null) {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(vessel, isLaden);
		} else {
			referenceSpeed = vessel.getVesselOrDelegateMaxSpeed();
		}
		return referenceSpeed;
	}

	public static double getReferenceSpeed(final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final LoadSlot desPurchase, final Vessel vessel, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		if (shippingDaysSpeedProvider != null) {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(desPurchase, vessel, isLaden);
		} else {
			referenceSpeed = vessel.getVesselOrDelegateMaxSpeed();
		}
		return referenceSpeed;
	}

	public static double getReferenceSpeed(final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final DischargeSlot fobSale, final Vessel vessel, final boolean isLaden) {
		double referenceSpeed;

		// catch error in case no service registered
		if (shippingDaysSpeedProvider != null) {
			referenceSpeed = shippingDaysSpeedProvider.getSpeed(fobSale, vessel, isLaden);
		} else {
			referenceSpeed = vessel.getVesselOrDelegateMaxSpeed();
		}
		return referenceSpeed;
	}

}
