package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IVesselCharterInRateProvider extends IDataComponentProvider {

	@Nullable
	ICurve getCharterInRatePerDay(@NonNull IVessel vessel);
}
