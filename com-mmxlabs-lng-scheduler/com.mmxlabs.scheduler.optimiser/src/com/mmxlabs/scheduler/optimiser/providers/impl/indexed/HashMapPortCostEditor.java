/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class HashMapPortCostEditor implements IPortCostProviderEditor {
	private final IIndexMap<@NonNull IPort, @Nullable PortCosts> content = new ArrayIndexMap<>();

	private static class PortCosts {

		private final Map<@NonNull IVessel, @Nullable LongFastEnumMap<PortType>> contents = new HashMap<>();

		public void setPortCost(final @NonNull IVessel vessel, final @NonNull PortType type, final long cost) {
			LongFastEnumMap<PortType> x = contents.get(vessel);
			if (x == null) {
				x = new LongFastEnumMap<PortType>(PortType.values().length);
				contents.put(vessel, x);
			}
			x.put(type, cost);
		}

		public long getPortCost(final @NonNull IVessel vessel, final @NonNull PortType type) {
			final LongFastEnumMap<PortType> maybeGet = contents.get(vessel);
			if (maybeGet == null) {
				return 0L;
			}
			return maybeGet.get(type);
		}
	}

	@Override
	public long getPortCost(final @NonNull IPort port, @NonNull final IVessel vessel, @NonNull final PortType portType) {
		final PortCosts pc = content.maybeGet(port);
		if (pc == null) {
			return 0L;
		}
		return pc.getPortCost(vessel, portType);
	}

	@Override
	public void setPortCost(final @NonNull IPort port, final @NonNull IVessel vessel, final @NonNull PortType type, final long cost) {
		PortCosts pc = content.maybeGet(port);
		if (pc == null) {
			pc = new PortCosts();
			content.set(port, pc);
		}
		pc.setPortCost(vessel, type, cost);
	}
}
