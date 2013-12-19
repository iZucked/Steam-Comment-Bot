/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseVolumeAllocator.AllocationRecord;

public interface IRedirectionVolumeCalculator {

	void calculateVolumes(AllocationRecord constraint);

}
