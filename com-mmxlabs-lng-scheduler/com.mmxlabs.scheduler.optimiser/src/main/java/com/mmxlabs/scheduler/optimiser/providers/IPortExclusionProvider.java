package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public interface IPortExclusionProvider extends IDataComponentProvider {
	/**
	 * Get the set of ports which vessels of this class cannot visit.
	 * @param vesselClass
	 * @return
	 */
	public Set<IPort> getExcludedPorts(IVesselClass vesselClass);
}
