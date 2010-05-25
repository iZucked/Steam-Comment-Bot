package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselResource(IResource resource, IVessel vessel);
	
}
