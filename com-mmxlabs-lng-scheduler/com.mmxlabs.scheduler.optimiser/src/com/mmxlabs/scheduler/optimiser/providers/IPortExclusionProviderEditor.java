/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public interface IPortExclusionProviderEditor extends IPortExclusionProvider {

	/**
	 */
	public void setExcludedPorts(IVessel vessel, Set<IPort> excludedPorts);

	public void setExcludedPorts(IVesselClass vesselClass, Set<IPort> excludedPorts);
}
