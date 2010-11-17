/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit;

/**
 * @author hinton
 *
 */
public interface ITotalVolumeLimitEditor<T> extends ITotalVolumeLimitProvider<T> {
	public void addTotalVolumeLimit(final ITotalVolumeLimit limit);
}
