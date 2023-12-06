package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.impl.EuEtsSeasonalityCurve;

public interface IEuEtsProvider {

	Set<IPort> getEuPorts();
	EuEtsSeasonalityCurve getSeasonalityCurve();
	IParameterisedCurve getPriceCurve();
}
