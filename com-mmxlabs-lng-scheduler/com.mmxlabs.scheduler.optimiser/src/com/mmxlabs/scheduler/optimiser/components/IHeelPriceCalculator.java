/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IHeelPriceCalculator {

	int getHeelPrice(long heelVolumeInM3, int utcTime);

	int getHeelPrice(long heelVolume, int localTime, @NonNull IPort port);
}
