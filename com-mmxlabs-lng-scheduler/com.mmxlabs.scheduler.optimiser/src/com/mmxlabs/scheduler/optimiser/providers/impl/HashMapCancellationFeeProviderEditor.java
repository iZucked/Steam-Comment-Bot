/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProviderEditor;

/**
 * @author berkan
 *
 */
public class HashMapCancellationFeeProviderEditor implements ICancellationFeeProviderEditor {

	private static final @NonNull ILongCurve zero = new ConstantValueLongCurve(0);

	private final Map<@NonNull IPortSlot, @NonNull ILongCurve> map = new HashMap<>();

	@Override
	public @NonNull ILongCurve getCancellationExpression(final @NonNull IPortSlot portSlot) {
		if (map.containsKey(portSlot)) {
			return map.get(portSlot);
		}

		return zero;
	}

	@Override
	public void setCancellationExpression(final @NonNull IPortSlot portSlot, final @NonNull ILongCurve cancellationFeeCurves) {
		assert cancellationFeeCurves != null;
		map.put(portSlot, cancellationFeeCurves);
	}
}
