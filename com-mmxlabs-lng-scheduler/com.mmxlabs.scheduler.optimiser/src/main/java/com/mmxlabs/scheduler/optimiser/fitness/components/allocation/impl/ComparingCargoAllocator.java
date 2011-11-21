/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * @author hinton
 * 
 */
public class ComparingCargoAllocator<T> implements ICargoAllocator<T> {
	private final FastCargoAllocator<T> fastAllocator = new FastCargoAllocator<T>();
	private final SimplexCargoAllocator<T> simplexAllocator = new SimplexCargoAllocator<T>();

	@Override
	public void setTotalVolumeLimitProvider(
			final ITotalVolumeLimitProvider<T> tvlp) {
		fastAllocator.setTotalVolumeLimitProvider(tvlp);
//		simplexAllocator.setTotalVolumeLimitProvider(tvlp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #init()
	 */
	@Override
	public void init() {
		fastAllocator.init();
//		simplexAllocator.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #reset()
	 */
	@Override
	public void reset() {
		fastAllocator.reset();
//		simplexAllocator.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#allocate(com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences)
	 */
	@Override
	public Collection<IAllocationAnnotation> allocate(ScheduledSequences sequences) {
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
	public void setVesselProvider(IVesselProvider dataComponentProvider) {
		// TODO Auto-generated method stub

	}

}
