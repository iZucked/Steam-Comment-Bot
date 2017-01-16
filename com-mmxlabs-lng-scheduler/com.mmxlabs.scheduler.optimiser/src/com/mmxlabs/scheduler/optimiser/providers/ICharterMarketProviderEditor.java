/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 */
public interface ICharterMarketProviderEditor extends ICharterMarketProvider {

	void addCharterInOption(@NonNull IVesselClass vesselClass, @NonNull ILongCurve charterInCurve);

	void addCharterOutOption(@NonNull IVesselClass vesselClass, @NonNull ILongCurve charterOutCurve, int minDuration, @NonNull Set<@NonNull IPort> allowedPorts);

	void setCharterOutStartTime(int startTime);
}
