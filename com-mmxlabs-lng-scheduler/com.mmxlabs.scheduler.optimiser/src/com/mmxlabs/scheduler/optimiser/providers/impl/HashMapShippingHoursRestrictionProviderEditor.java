/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;

/**
 */
public class HashMapShippingHoursRestrictionProviderEditor implements IShippingHoursRestrictionProviderEditor {

	private static final List<ERouteOption> defaultAllowedRoutes = Lists.newArrayList(ERouteOption.DIRECT);

	private final Map<ISequenceElement, Integer> hoursMap = new HashMap<>();
	private final Map<ISequenceElement, ITimeWindow> baseTimeMap = new HashMap<>();
	private final Map<IPortSlot, Map<IVessel, Integer>> ballastReferenceSpeeds = new HashMap<>();
	private final Map<IPortSlot, Map<IVessel, Integer>> ladenReferenceSpeeds = new HashMap<>();
	private final Map<ILoadOption, List<ERouteOption>> allowedDESRoutes = new HashMap<>();
	private final Map<IDischargeOption, List<ERouteOption>> allowedFOBRoutes = new HashMap<>();

	@Override
	public int getShippingHoursRestriction(@NonNull final ISequenceElement element) {
		if (hoursMap.containsKey(element)) {
			return hoursMap.get(element);
		}
		return RESTRICTION_UNDEFINED;
	}

	@Override
	public ITimeWindow getBaseTime(@NonNull final ISequenceElement element) {
		if (baseTimeMap.containsKey(element)) {
			return baseTimeMap.get(element);
		}
		return null;
	}

	@Override
	public void setShippingHoursRestriction(@NonNull final ISequenceElement element, final @NonNull ITimeWindow baseTime, final int hours) {
		hoursMap.put(element, hours);
		baseTimeMap.put(element, baseTime);
	}

	@Override
	public boolean isDivertible(@NonNull final ISequenceElement element) {
		return hoursMap.containsKey(element);
	}

	@Override
	public int getReferenceSpeed(@NonNull final IPortSlot slot, @NonNull final IVessel vessel, final VesselState vesselState) {
		if (vesselState == VesselState.Ballast) {
			return ballastReferenceSpeeds.get(slot).get(vessel);
		} else {
			return ladenReferenceSpeeds.get(slot).get(vessel);

		}
	}

	@Override
	public void setReferenceSpeed(@NonNull final IPortSlot slot, @NonNull final IVessel vessel, final VesselState vesselState, final int referenceSpeed) {
		if (vesselState == VesselState.Ballast) {
			ballastReferenceSpeeds.computeIfAbsent(slot, k -> new HashMap<>()).put(vessel, referenceSpeed);
		} else {
			ladenReferenceSpeeds.computeIfAbsent(slot, k -> new HashMap<>()).put(vessel, referenceSpeed);
		}
	}

	@Override
	public Collection<ERouteOption> getDivertibleDESAllowedRoutes(@NonNull final ILoadOption loadOption) {
		return allowedDESRoutes.getOrDefault(loadOption, defaultAllowedRoutes);
	}

	@Override
	public Collection<ERouteOption> getDivertibleFOBAllowedRoutes(@NonNull final IDischargeOption fobSale) {
		return allowedFOBRoutes.getOrDefault(fobSale, defaultAllowedRoutes);
	}

	@Override
	public void setDivertibleDESAllowedRoute(@NonNull final ILoadOption loadOption, @NonNull final ERouteOption route) {
		if (!allowedDESRoutes.containsKey(loadOption)) {
			allowedDESRoutes.put(loadOption, new LinkedList<>());
		}
		allowedDESRoutes.get(loadOption).add(route);
	}

	@Override
	public void setDivertibleFOBAllowedRoute(@NonNull IDischargeOption fobSale, @NonNull final ERouteOption route) {
		if (!allowedFOBRoutes.containsKey(fobSale)) {
			allowedFOBRoutes.put(fobSale, new LinkedList<>());
		}
		allowedFOBRoutes.get(fobSale).add(route);
	}
}
