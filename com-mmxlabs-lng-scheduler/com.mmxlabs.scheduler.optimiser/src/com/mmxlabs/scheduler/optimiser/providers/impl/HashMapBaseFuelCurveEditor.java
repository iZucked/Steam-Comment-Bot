/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProviderEditor;

/**
 * Implementation of {@link IBaseFuelCurveProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author achurchill
 * 
 */
public final class HashMapBaseFuelCurveEditor implements IBaseFuelCurveProviderEditor {

	private final Map<IBaseFuel, ICurve> map = new HashMap<IBaseFuel, ICurve>();
	private final Map<ICurve, Integer> firstValueMap = new HashMap<ICurve, Integer>();

	@Override
	public ICurve getBaseFuelCurve(final IBaseFuel baseFuel) {
		if (map.containsKey(baseFuel)) {
			return map.get(baseFuel);
		}
		throw new IllegalArgumentException("Unknown basefuel");
	}

	@Override
	public ICurve getVesselBaseFuelCurve(final IVessel vessel) {
		if (map.containsKey(vessel)) {
			return map.get(vessel);
		}
		throw new IllegalArgumentException("Unknown vessel");
	}

	@Override
	public void setBaseFuelCurve(final IBaseFuel baseFuel, final ICurve curve) {
		map.put(baseFuel, curve);
	}

	@Override
	public int getBaseFuelCurveFirstValueDate(@NonNull ICurve curve) {
		return firstValueMap.get(curve);
	}

	@Override
	public void setBaseFuelCurveFirstValueDate(@NonNull ICurve curve, int firstValueDate) {
		firstValueMap.put(curve, firstValueDate);
	}
}