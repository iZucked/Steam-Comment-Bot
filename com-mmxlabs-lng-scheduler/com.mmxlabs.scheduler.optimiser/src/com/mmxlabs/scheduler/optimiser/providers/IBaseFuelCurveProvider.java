/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

public interface IBaseFuelCurveProvider extends IDataComponentProvider {

	@NonNull
	ICurve getBaseFuelCurve(@NonNull IBaseFuel baseFuel);
}
