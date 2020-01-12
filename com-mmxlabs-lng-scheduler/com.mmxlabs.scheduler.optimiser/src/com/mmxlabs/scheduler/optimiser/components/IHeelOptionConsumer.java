/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IHeelOptionConsumer {


	@NonNull
	IHeelPriceCalculator getHeelPriceCalculator();

	VesselTankState getExpectedTankState();

	long getMinimumHeelAcceptedInM3();

	long getMaximumHeelAcceptedInM3();

	boolean isUseLastPrice();
}
