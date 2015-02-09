/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

public interface IBaseFuelCurveProviderEditor extends IBaseFuelCurveProvider {

	void setBaseFuelCurve(@NonNull IBaseFuel baseFuel, @NonNull ICurve curve);

	void setBaseFuelCurveFirstValueDate(@NonNull ICurve curve, int firstValueDate);

}
