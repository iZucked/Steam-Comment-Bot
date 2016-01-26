/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;

/**
 */
public class HashMapShippingHoursRestrictionProviderEditor implements IShippingHoursRestrictionProviderEditor {

	private final Map<ISequenceElement, Integer> hoursMap = new HashMap<>();
	private final Map<ISequenceElement, ITimeWindow> baseTimeMap = new HashMap<>();
	private final Map<IVessel, Integer> ballastReferenceSpeeds = new HashMap<>();
	private final Map<IVessel, Integer> ladenReferenceSpeeds = new HashMap<>();
	private final Map<IVesselClass, List<String>> allowedRoutes = new HashMap<>();

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
	public boolean isDivertable(@NonNull final ISequenceElement element) {
		return hoursMap.containsKey(element);
	}

	@Override
	public int getReferenceSpeed(@NonNull final IVessel vessel, final VesselState vesselState) {
		if (vesselState == VesselState.Ballast) {
			return ballastReferenceSpeeds.get(vessel);
		} else {
			return ladenReferenceSpeeds.get(vessel);

		}
	}

	@Override
	public void setReferenceSpeed(@NonNull final IVessel vessel, final VesselState vesselState, final int referenceSpeed) {
		if (vesselState == VesselState.Ballast) {
			ballastReferenceSpeeds.put(vessel, referenceSpeed);
		} else {
			ladenReferenceSpeeds.put(vessel, referenceSpeed);
		}
	}

	@Override
	public Collection<String> getDivertableDESAllowedRoutes(@NonNull IVesselClass vc) {
		return allowedRoutes.get(vc);
	}

	@Override
	public void setDivertableDESAllowedRoute(@NonNull IVesselClass vc, @NonNull String route) {
		if (!allowedRoutes.containsKey(vc)) {
			allowedRoutes.put(vc, new LinkedList<String>());
		}
		allowedRoutes.get(vc).add(route);
	}
}
