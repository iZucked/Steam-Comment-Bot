package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public interface IPortExclusionProviderEditor extends
		IPortExclusionProvider {
	public void setExcludedPorts(IVesselClass vesselClass, Set<IPort> excludedPorts);
}
