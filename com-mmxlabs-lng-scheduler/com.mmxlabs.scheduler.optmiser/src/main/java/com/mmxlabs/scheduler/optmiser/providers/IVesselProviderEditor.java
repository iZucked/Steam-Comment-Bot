package com.mmxlabs.scheduler.optmiser.providers;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optmiser.components.IVessel;

public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselResource(IResource resource, IVessel vessel);
	
}
