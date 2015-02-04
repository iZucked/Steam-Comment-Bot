/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class HashMapPortCostEditor implements IPortCostProviderEditor {

	class PortCosts {
		private final Map<IVessel, LongFastEnumMap<PortType>> contents = new HashMap<>();

		public void setPortCost(final IVessel vessel, final PortType type, final long cost) {
			LongFastEnumMap<PortType> x = contents.get(vessel);
			if (x == null) {
				x = new LongFastEnumMap<PortType>(PortType.values().length);
				contents.put(vessel, x);
			}
			x.put(type, cost);
		}

		public long getPortCost(final IVessel vessel, final PortType type) {
			final LongFastEnumMap<PortType> maybeGet = contents.get(vessel);
			if (maybeGet == null)
				return 0;
			return maybeGet.get(type);
		}
	}

	final IIndexMap<IPort, PortCosts> content = new ArrayIndexMap<IPort, PortCosts>();

	@Override
	public long getPortCost(final IPort port, final IVessel vessel, final PortType portType) {
		final PortCosts pc = content.maybeGet(port);
		if (pc == null)
			return 0;
		return pc.getPortCost(vessel, portType);
	}

	@Override
	public void setPortCost(final IPort port, final IVessel vessel, final PortType type, final long cost) {
		PortCosts pc = content.maybeGet(port);
		if (pc == null) {
			pc = new PortCosts();
			content.set(port, pc);
		}
		pc.setPortCost(vessel, type, cost);
	}
}
