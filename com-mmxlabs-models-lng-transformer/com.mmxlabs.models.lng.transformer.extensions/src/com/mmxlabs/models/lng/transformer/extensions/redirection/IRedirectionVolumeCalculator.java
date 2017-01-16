/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;

public interface IRedirectionVolumeCalculator {

	void modifyAllocationRecord(AllocationRecord constraint);

}
