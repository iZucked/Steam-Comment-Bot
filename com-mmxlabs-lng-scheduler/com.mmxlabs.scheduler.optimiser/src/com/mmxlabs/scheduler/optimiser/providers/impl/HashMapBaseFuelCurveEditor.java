/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProviderEditor;

/**
 * Implementation of {@link IBaseFuelCurveProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author achurchill
 * 
 */
public final class HashMapBaseFuelCurveEditor implements IBaseFuelCurveProviderEditor {

	@NonNull
	private final Map<IBaseFuel, ICurve> map = new HashMap<IBaseFuel, ICurve>();

	@Override
	@NonNull
	public ICurve getBaseFuelCurve(@NonNull final IBaseFuel baseFuel) {
		if (map.containsKey(baseFuel)) {
			return map.get(baseFuel);
		}
		throw new IllegalArgumentException("Unknown basefuel");
	}

	@Override
	public void setBaseFuelCurve(@NonNull final IBaseFuel baseFuel, @NonNull final ICurve curve) {
		map.put(baseFuel, curve);
	}

}