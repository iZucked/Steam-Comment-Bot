/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 */
public interface ICharterMarketProviderEditor extends ICharterMarketProvider {

	void addCharterInOption(IVesselClass vesselClass, ICurve charterInCurve);

	void addCharterOutOption(IVesselClass vesselClass, ICurve charterOutCurve, int minDuration);

	void setCharterOutStartTime(int startTime);
}
