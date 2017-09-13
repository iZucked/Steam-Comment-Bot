/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 */
public interface ICharterMarketProviderEditor extends ICharterMarketProvider {

	void addCharterInOption(@NonNull IVessel vessel, @NonNull ILongCurve charterInCurve);

	void addCharterOutOption(@NonNull IVessel vessel, @NonNull ILongCurve charterOutCurve, int minDuration, @NonNull Set<@NonNull IPort> allowedPorts);

	void setCharterOutStartTime(int startTime);
}
