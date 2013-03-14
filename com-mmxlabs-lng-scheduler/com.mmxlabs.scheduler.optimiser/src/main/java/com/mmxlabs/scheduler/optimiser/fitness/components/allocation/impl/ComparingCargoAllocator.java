/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class ComparingCargoAllocator implements ICargoAllocator {
	private final FastCargoAllocator fastAllocator = new FastCargoAllocator();

	// private final SimplexCargoAllocator simplexAllocator = new SimplexCargoAllocator();

	@Override
	public void setTotalVolumeLimitProvider(final ITotalVolumeLimitProvider tvlp) {
		fastAllocator.setTotalVolumeLimitProvider(tvlp);
		// simplexAllocator.setTotalVolumeLimitProvider(tvlp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #init()
	 */
	@Override
	public void init() {
		fastAllocator.init();
		// simplexAllocator.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #reset()
	 */
	@Override
	public void reset() {
		fastAllocator.reset();
		// simplexAllocator.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#allocate(com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences)
	 */
	@Override
	public Map<VoyagePlan, IAllocationAnnotation> allocate(final ScheduledSequences sequences) {
		// TODO Auto-generated method stub
		return fastAllocator.allocate(sequences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#setVesselProvider(com.mmxlabs.scheduler.optimiser.providers.IVesselProvider)
	 */
	@Override
	public void setVesselProvider(final IVesselProvider dataComponentProvider) {
		// TODO Auto-generated method stub

	}

	/**
	 * @since 2.0
	 */
	@Override
	public IAllocationAnnotation allocate(IVessel vessel, VoyagePlan plan, int[] arrivalTimes) {
		// TODO Auto-generated method stub
		return fastAllocator.allocate(vessel, plan, arrivalTimes);
	}

}
