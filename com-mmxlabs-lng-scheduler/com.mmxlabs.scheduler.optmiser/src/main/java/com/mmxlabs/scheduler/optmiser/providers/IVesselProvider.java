package com.mmxlabs.scheduler.optmiser.providers;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optmiser.components.IVessel;

public interface IVesselProvider extends IDataComponentProvider {

	IVessel getVessel(IResource resource);

	IResource getResource(IVessel vessel);
}
