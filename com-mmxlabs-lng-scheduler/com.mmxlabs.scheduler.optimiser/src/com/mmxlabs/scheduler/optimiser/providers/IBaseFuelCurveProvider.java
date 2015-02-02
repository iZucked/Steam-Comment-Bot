/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IBaseFuelCurveProvider extends IDataComponentProvider {

	@NonNull
	ICurve getBaseFuelCurve(IBaseFuel baseFuel);
	
	@NonNull
	ICurve getVesselBaseFuelCurve(IVessel vessel);
	
	int getBaseFuelCurveFirstValueDate(ICurve curve);

}
