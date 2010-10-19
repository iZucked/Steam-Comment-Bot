/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public interface IRouteCostProvider extends IDataComponentProvider {
	public int getRouteCost(final String route, final IVesselClass vesselClass, final VesselState vesselState);
}
