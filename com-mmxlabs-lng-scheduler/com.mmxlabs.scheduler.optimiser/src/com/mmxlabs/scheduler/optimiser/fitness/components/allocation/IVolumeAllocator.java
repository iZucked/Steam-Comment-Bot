/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Interface for volume allocation logic.
 * 
 * @author hinton
 * 
 */
public interface IVolumeAllocator {
	/**
	 * Set the {@link ITotalVolumeLimitProvider} which informs the allocator of any global constraints on load/discharge volumes
	 * 
	 * @param tvlp
	 */
	public void setTotalVolumeLimitProvider(ITotalVolumeLimitProvider tvlp);

	public void init();

	/**
	 * Return a bunch of {@link IAllocationAnnotation}s for the given {@link ScheduledSequences} keyed off {@link VoyagePlan}s.
	 * 
	 * These should include the correct load prices/volumes for each allocated cargo.
	 * 
	 * @param sequences
	 * @return
	 */
	public Map<VoyagePlan, IAllocationAnnotation> allocate(ScheduledSequences sequences);

	/**
	 * Given a single {@link VoyagePlan} calculate "best as" the cargo allocation. By "best as" this means allocation without the other cargo volumes. Typically this will mean maxing out the load.
	 * 
	 * @param voyagePlan
	 * @return
	 * @since 5.0
	 */
	public IAllocationAnnotation allocate(IVessel vessel, VoyagePlan plan, List<Integer> arrivalTimes);

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