package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseVolumeAllocator.AllocationRecord;

public interface IRedirectionVolumeCalculator {

	void calculateVolumes(AllocationRecord constraint);

}
