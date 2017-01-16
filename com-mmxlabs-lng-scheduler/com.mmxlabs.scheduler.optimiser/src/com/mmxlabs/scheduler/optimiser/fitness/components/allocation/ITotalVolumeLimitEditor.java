/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit;

/**
 * @author hinton
 * 
 */
public interface ITotalVolumeLimitEditor extends ITotalVolumeLimitProvider {
	public void addTotalVolumeLimit(final ITotalVolumeLimit limit);
}
