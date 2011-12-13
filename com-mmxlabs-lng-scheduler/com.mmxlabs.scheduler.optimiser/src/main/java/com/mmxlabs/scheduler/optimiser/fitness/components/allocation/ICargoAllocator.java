/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Interface for cargo allocation logic.
 * 
 * @author hinton
 * 
 */
public interface ICargoAllocator {
	/**
	 * Set the {@link ITotalVolumeLimitProvider} which informs the allocator of any global constraints on load/discharge volumes
	 * 
	 * @param tvlp
	 */
	public void setTotalVolumeLimitProvider(ITotalVolumeLimitProvider tvlp);

	public void init();

	/**
	 * Return a bunch of {@link IAllocationAnnotation}s for the given {@link ScheduledSequences}.
	 * 
	 * These should include the correct load prices/volumes for each allocated cargo.
	 * 
	 * @param sequences
	 * @return
	 */
	public Collection<IAllocationAnnotation> allocate(ScheduledSequences sequences);

	public void dispose();

	/**
	 * Forget anything that's happened in the last allocation step.
	 */
	void reset();

	/**
	 * @param dataComponentProvider
	 */
	public void setVesselProvider(IVesselProvider dataComponentProvider);
}