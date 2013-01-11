/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortCostEditor implements IPortCostProviderEditor {
	final String name;
	
	class PortCosts {
		private final IIndexMap<IVessel, LongFastEnumMap<PortType>> contents = 
				new ArrayIndexMap<IVessel, LongFastEnumMap<PortType>>();
		
		public void setPortCost(final IVessel vessel, final PortType type, final long cost) {
			LongFastEnumMap<PortType> x = contents.maybeGet(vessel);
			if (x == null) {
				x = new LongFastEnumMap<PortType>(PortType.values().length);
				contents.set(vessel, x);
			}
			x.put(type, cost);
		}
		
		public long getPortCost(final IVessel vessel, final PortType type) {
			LongFastEnumMap<PortType> maybeGet = contents.maybeGet(vessel);
			if (maybeGet == null) return 0;
			return maybeGet.get(type);
		}
	}
	
	final IIndexMap<IPort, PortCosts> content = new ArrayIndexMap<IPort, PortCosts>();
	
	public IndexedPortCostEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public long getPortCost(final IPort port, final IVessel vessel, final PortType portType) {
		final PortCosts pc = content.maybeGet(port);
		if (pc == null) return 0;
		return pc.getPortCost(vessel, portType);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		content.clear();
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
