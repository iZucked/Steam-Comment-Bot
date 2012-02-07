/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public interface IPortExclusionProvider extends IDataComponentProvider {
	/**
	 * Get the set of ports which vessels of this class cannot visit.
	 * 
	 * @param vesselClass
	 * @return
	 */
	public Set<IPort> getExcludedPorts(IVesselClass vesselClass);

	/**
	 * If there are no exclusions set at all, this returns true. Useful for quickly avoiding execution if this is empty.
	 * 
	 * @return
	 */
	public boolean hasNoExclusions();
}
