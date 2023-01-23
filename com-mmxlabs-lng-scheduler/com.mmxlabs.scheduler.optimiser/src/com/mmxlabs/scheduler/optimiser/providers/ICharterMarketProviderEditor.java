/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterOutMarket;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 */
public interface ICharterMarketProviderEditor extends ICharterMarketProvider {

	void addCharterInOption(@NonNull IVessel vessel, @NonNull ILongCurve charterInCurve, @NonNull ISpotCharterOutMarket market);

	void addCharterOutOption(@NonNull IVessel vessel, @NonNull ILongCurve charterOutCurve, int minDuration, int maxDuration, @NonNull Set<@NonNull IPort> allowedPorts, @NonNull ISpotCharterOutMarket market);

	void setCharterOutStartTime(int startTime);
	
	void setCharterOutEndTime(int endTime);

}
