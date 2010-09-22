package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public interface IRouteCostProviderEditor extends IRouteCostProvider {
	public void setRouteCost(final String route, final IVesselClass vesselClass, final VesselState vesselState, final int price);
	public void setDefaultRouteCost(final String route, final int price);
}
