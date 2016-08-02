/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.utils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.RouteDistanceLineCache;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class TravelTimeUtils {
	
	public static final List<RouteOption> EMPTY_ROUTES = Collections.EMPTY_LIST;
	
	public static int getDivertableDESMinRouteTimeInHours(final LoadSlot desPurchase, Slot from,  final Slot to, final IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final LNGScenarioModel lngScenarioModel,
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
		int minDuration = getMinTimeFromAllowedRoutes(from, to, vessel.getVesselClass(), referenceSpeed, allowedRoutes);
		return minDuration;
	}

	public static int getFobMinTimeInHours(final Slot from, final Slot to, final LocalDate date, final VesselAssignmentType vesselAssignmentType, PortModel portModel, CostModel costModel, double referenceSpeed) {
		List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, date, portModel, costModel);
		Pair<VesselClass,List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		VesselClass vc = vesselAssignmentTypeData.getFirst();
		double maxSpeed = vc != null ? vc.getMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes);
	}
	
	private static int getMinTimeFromAllowedRoutes(Slot from, final Slot to, final VesselClass vesselClass, final double referenceSpeed, Collection<Route> allowedRoutes) {
		int minDuration = Integer.MAX_VALUE;
		for (final Route route : allowedRoutes) {
			assert route != null;
			final Port fromPort = from.getPort();
			final Port toPort = to.getPort();
			if (fromPort != null && toPort != null) {
				final int totalTime = getTimeForRoute(vesselClass, referenceSpeed, route, fromPort, toPort);
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
		List<Route> routes = new LinkedList<Route>();
		Pair<VesselClass, List<RouteOption>> vatData = getVesselAssignmentTypeData(vesselAssignmentType);
		for (Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == RouteOption.PANAMA && (costModel.getPanamaCanalTariff() != null && costModel.getPanamaCanalTariff().getAvailableFrom() != null && costModel.getPanamaCanalTariff().getAvailableFrom().isAfter(date))) {
				// Panama is closed
				continue;
			}
			if (!vatData.getSecond().contains(route.getRouteOption())&&!vatData.getFirst().getInaccessibleRoutes().contains(route.getRouteOption())) {
				routes.add(route);
			}
		}
		return routes;
	}
	
	public static Pair<VesselClass, List<RouteOption>> getVesselAssignmentTypeData(VesselAssignmentType vesselAssignmentType) {
		VesselClass vc;
		List<RouteOption> vesselDisabledRoutes;
		if (vesselAssignmentType == null) {
			return new Pair<VesselClass, List<RouteOption>>(null, null);
		}
		if (vesselAssignmentType instanceof CharterInMarket) {
			CharterInMarket cim = ((CharterInMarket) vesselAssignmentType);
			vc = cim.getVesselClass();
			vesselDisabledRoutes = cim.isOverrideInaccessibleRoutes() ? cim.getInaccessibleRoutes() : EMPTY_ROUTES;
		} else {
			VesselAvailability v = (VesselAvailability) vesselAssignmentType;
			vc = v.getVessel().getVesselClass();
			vesselDisabledRoutes = v.getVessel().isOverrideInaccessibleRoutes() ? v.getVessel().getInaccessibleRoutes() : EMPTY_ROUTES;
		}
		return new Pair(vc, vesselDisabledRoutes);
	}
		
	private static List<Route> processRouteOptions(EList<Route> routes, EList<RouteOption> inaccessibleRoutes) {
		List<Route> newRoutes = new LinkedList<>();
		for (Route route : routes) {
			for (RouteOption routeOption : inaccessibleRoutes) {
				if (route.getRouteOption().equals(routeOption)) {
					routes.add(route);
					break;
				}
			}
		}
		return newRoutes;
	}

	private static int getTimeForRoute(final VesselClass vesselClass, final double referenceSpeed, final Route route, final Port fromPort, final Port toPort) {
		final int distance = getDistance(route, fromPort, toPort);

		int extraTime = 0;
		if (vesselClass != null) {
			for (final VesselClassRouteParameters vcrp : vesselClass.getRouteParameters()) {
				if (vcrp.getRoute().equals(route)) {
					extraTime = vcrp.getExtraTransitTime();
				}
			}
		}
		final double travelTime = distance / referenceSpeed;
		final int totalTime = (int) (Math.floor(travelTime) + extraTime);
		return totalTime;
	}

	private static int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to) {
		final RouteDistanceLineCache cache = (RouteDistanceLineCache) Platform.getAdapterManager().loadAdapter(route, RouteDistanceLineCache.class.getName());
		if (cache != null) {
			return cache.getDistance(from, to);
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
