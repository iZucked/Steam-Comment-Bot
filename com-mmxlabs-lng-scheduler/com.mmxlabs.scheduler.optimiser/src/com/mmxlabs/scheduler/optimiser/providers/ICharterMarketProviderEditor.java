/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 */
public interface ICharterMarketProviderEditor extends ICharterMarketProvider {

	void addCharterInOption(IVesselClass vesselClass, ICurve charterInCurve);

	void addCharterOutOption(IVesselClass vesselClass, ICurve charterOutCurve, int minDuration, Set<IPort> allowedPorts);

	void setCharterOutStartTime(int startTime);
}
