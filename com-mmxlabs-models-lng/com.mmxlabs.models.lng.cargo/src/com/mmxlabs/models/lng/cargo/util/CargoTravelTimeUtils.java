/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
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
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

@NonNullByDefault
public class CargoTravelTimeUtils {
	private CargoTravelTimeUtils() {

	}

	public static final List<RouteOption> EMPTY_ROUTES = Collections.emptyList();

	public static int getDivertibleDESMinRouteTimeInHours(final LoadSlot desPurchase, final Slot<?> from, final Slot<?> to,
			final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final PortModel portModel, final Vessel vessel, final double referenceSpeed,
			final ModelDistanceProvider modelDistanceProvider) {

		Collection<Route> allowedRoutes = null;
		if (shippingDaysSpeedProvider != null) {
			allowedRoutes = shippingDaysSpeedProvider.getValidRoutes(portModel, desPurchase);
		}
		if (allowedRoutes == null || allowedRoutes.isEmpty()) {
			allowedRoutes = portModel.getRoutes();
		}
		return getMinTimeFromAllowedRoutes(from, to, vessel, referenceSpeed, allowedRoutes, modelDistanceProvider);
	}

	public static int getDivertibleFOBMinRouteTimeInHours(final DischargeSlot fobSale, final Slot<?> from, final Slot<?> to,
			final @Nullable IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider, final PortModel portModel, final Vessel vessel, final double referenceSpeed,
			final ModelDistanceProvider modelDistanceProvider) {

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
			final double referenceSpeed, final ModelDistanceProvider modelDistanceProvider) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, portModel);
		final Pair<Vessel, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final Vessel vessel = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vessel != null ? vessel.getVesselOrDelegateMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes, modelDistanceProvider);
	}

	public static int getFobMinTimeInHours(final Slot<?> from, final Slot<?> to, final LocalDate date, final VesselAssignmentType vesselAssignmentType, final PortModel portModel,
			final double referenceSpeed, final Pair<Integer, Integer> panamaWaitingDays, final ModelDistanceProvider modelDistanceProvider) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, portModel);
		final Pair<Vessel, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final Vessel vessel = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vessel != null ? vessel.getVesselOrDelegateMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes, panamaWaitingDays, modelDistanceProvider);
	}

	public static int getFobMinTimeInHours(final Port from, final Port to, final VesselAssignmentType vesselAssignmentType, final PortModel portModel, final double referenceSpeed,
			final ModelDistanceProvider modelDistanceProvider, final int bufferTime) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, portModel);
		final Pair<Vessel, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final Vessel vessel = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vessel != null ? vessel.getVesselOrDelegateMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes, modelDistanceProvider, bufferTime);
	}

	public static int getFobMinTimeInHours(final Port from, final Port to, final VesselAssignmentType vesselAssignmentType, final PortModel portModel, final double referenceSpeed,
			final Pair<Integer, Integer> panamaWaitingDays, final ModelDistanceProvider modelDistanceProvider, final int bufferTime) {
		final List<Route> allowedRoutes = getAllowedRoutes(vesselAssignmentType, portModel);
		final Pair<Vessel, List<RouteOption>> vesselAssignmentTypeData = getVesselAssignmentTypeData(vesselAssignmentType);
		final Vessel vessel = vesselAssignmentTypeData.getFirst();
		final double maxSpeed = vessel != null ? vessel.getVesselOrDelegateMaxSpeed() : referenceSpeed;
		return getMinTimeFromAllowedRoutes(from, to, vesselAssignmentTypeData.getFirst(), maxSpeed, allowedRoutes, panamaWaitingDays, modelDistanceProvider, bufferTime);
	}

	public static int getMinTimeFromAllowedRoutes(final Slot<?> from, final Slot<?> to, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			final ModelDistanceProvider modelDistanceProvider) {
		int minDuration = Integer.MAX_VALUE;
		if (from != null && from.getPort() != null && to != null && to.getPort() != null) {
			for (final Route route : allowedRoutes) {
				assert route != null;
				final int totalTime = getTimeForRoute(vessel, referenceSpeed, route.getRouteOption(), from, to, modelDistanceProvider);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	public static int getMinTimeFromAllowedRoutes(final Slot<?> from, final Slot<?> to, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			final Pair<Integer, Integer> panamaWaitingDays, final ModelDistanceProvider modelDistanceProvider) {
		int minDuration = Integer.MAX_VALUE;
		if (from != null && from.getPort() != null && to != null && to.getPort() != null) {
			for (final Route route : allowedRoutes) {
				assert route != null;
				int totalTime = getTimeForRoute(vessel, referenceSpeed, route.getRouteOption(), from, to, modelDistanceProvider);
				if (totalTime != Integer.MAX_VALUE && route.getRouteOption() == RouteOption.PANAMA) {
					final CanalEntry closestEntry = modelDistanceProvider.getClosestCanalEntry(RouteOption.PANAMA, from.getPort());
					if (closestEntry != null) {
						totalTime += closestEntry == CanalEntry.NORTHSIDE ? panamaWaitingDays.getSecond() * 24 : panamaWaitingDays.getFirst() * 24;
					}
				}
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	public static int getMinTimeFromAllowedRoutes(final Port from, final Port to, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			final ModelDistanceProvider modelDistanceProvider, final int bufferTime) {
		int minDuration = Integer.MAX_VALUE;
		for (final Route route : allowedRoutes) {
			assert route != null;
			int totalTime = getTimeForRoute(vessel, referenceSpeed, route.getRouteOption(), from, to, modelDistanceProvider);
			if (totalTime != Integer.MAX_VALUE) {
				totalTime += bufferTime;
			}
			if (totalTime < minDuration) {
				minDuration = totalTime;
			}
		}

		return minDuration;
	}

	public static int getMinTimeFromAllowedRoutes(final Port from, final Port to, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			final Pair<Integer, Integer> panamaWaitingDays, final ModelDistanceProvider modelDistanceProvider, final int bufferTime) {
		int minDuration = Integer.MAX_VALUE;
		for (final Route route : allowedRoutes) {
			assert route != null;
			int totalTime = getTimeForRoute(vessel, referenceSpeed, route.getRouteOption(), from, to, modelDistanceProvider);
			if (totalTime != Integer.MAX_VALUE) {
				totalTime += bufferTime;
				if (route.getRouteOption() == RouteOption.PANAMA) {
					final CanalEntry closestEntry = modelDistanceProvider.getClosestCanalEntry(RouteOption.PANAMA, from);
					if (closestEntry != null) {
						totalTime += closestEntry == CanalEntry.NORTHSIDE ? panamaWaitingDays.getSecond() * 24 : panamaWaitingDays.getFirst() * 24;
					}
				}
			}
			if (totalTime < minDuration) {
				minDuration = totalTime;
			}
		}

		return minDuration;
	}

	public static int getTimeForRoute(final @Nullable Vessel vessel, final double referenceSpeed, final @NonNull RouteOption routeOptions, final @NonNull Slot<?> fromSlot,
			final @NonNull Slot<?> toSlot, ModelDistanceProvider modelDistanceProvider) {
		final Port fromPort = fromSlot.getPort();
		final Port toPort = toSlot.getPort();
		int bufferTime = toSlot.getSlotOrDelegateDaysBuffer() * 24;
		if (fromPort != null && toPort != null) {
			int timeForRoute = getTimeForRoute(vessel, referenceSpeed, routeOptions, fromPort, toPort, modelDistanceProvider);
			if (timeForRoute != Integer.MAX_VALUE) {
				return timeForRoute + bufferTime;
			} else {
				return Integer.MAX_VALUE; // do not add on bufferTime, as would cause extreme -ve time for route.
			}
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public static int getTimeForRoute(final @Nullable Vessel vessel, final double referenceSpeed, final @NonNull RouteOption routeOptions, final @NonNull Port fromPort, final @NonNull Port toPort,
			ModelDistanceProvider modelDistanceProvider) {

		final int distance = getDistance(routeOptions, fromPort, toPort, modelDistanceProvider);
		if (distance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		final int extraIdleTime = getContingencyIdleTimeInHours(fromPort, toPort, modelDistanceProvider);

		int extraTime = 0;
		if (vessel != null) {
			for (final VesselClassRouteParameters vcrp : vessel.getVesselOrDelegateRouteParameters()) {
				if (vcrp.getRouteOption() == routeOptions) {
					extraTime = vcrp.getExtraTransitTime();
				}
			}
		}

		final double travelTime = distance / referenceSpeed;
		final int totalTime = (int) (Math.floor(travelTime) + extraTime) + extraIdleTime;

		return totalTime;
	}

	private static int getDistance(final @NonNull RouteOption routeOptions, @NonNull final Port from, @NonNull final Port to, @NonNull ModelDistanceProvider modelDistanceProvider) {
		return modelDistanceProvider.getDistance(from, to, routeOptions);
	}

	private static int getContingencyIdleTimeInHours(@NonNull final Port from, @NonNull final Port to, @NonNull ModelDistanceProvider modelDistanceProvider) {
		return modelDistanceProvider.getPortToPortContingencyIdleTimeInHours(from, to);
	}

	private static List<Route> getAllowedRoutes(final VesselAssignmentType vesselAssignmentType, final PortModel portModel) {
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
			for (final Slot<?> s : cargo.getSlots()) {
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
