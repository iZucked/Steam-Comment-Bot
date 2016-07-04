/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IVesselCharterInRateProviderEditor extends IVesselCharterInRateProvider {

	void setCharterInRatePerDay(@NonNull IVessel vessel, @Nullable ICurve rateCurve);
}
