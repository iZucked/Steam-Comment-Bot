/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * @author hinton
 * 
 */
public class ComparingCargoAllocator<T> implements ICargoAllocator<T> {
	private final FastCargoAllocator<T> fastAllocator = new FastCargoAllocator<T>();
	private final SimplexCargoAllocator<T> simplexAllocator = new SimplexCargoAllocator<T>();

	@Override
	public void setTotalVolumeLimitProvider(ITotalVolumeLimitProvider<T> tvlp) {
		fastAllocator.setTotalVolumeLimitProvider(tvlp);
		simplexAllocator.setTotalVolumeLimitProvider(tvlp);
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
		simplexAllocator.init();
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
		simplexAllocator.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #addCargo(com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails,
	 * com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails, int, int, long,
	 * long)
	 */
	@Override
	public void addCargo(PortDetails loadDetails, PortDetails dischargeDetails,
			int loadTime, int dischargeTime, long requiredLoadVolume,
			long vesselCapacity) {
		fastAllocator.addCargo(loadDetails, dischargeDetails, loadTime,
				dischargeTime, requiredLoadVolume, vesselCapacity);
		simplexAllocator.addCargo(loadDetails, dischargeDetails, loadTime,
				dischargeTime, requiredLoadVolume, vesselCapacity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #solve()
	 */
	@Override
	public void solve() {
		simplexAllocator.solve();
		fastAllocator.solve();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #getProfit()
	 */
	@Override
	public long getProfit() {
		final long simplexResult = simplexAllocator.getProfit();
		final long fastResult = fastAllocator.getProfit();
		System.err.println("simplex = " + simplexResult + " fast = " + fastResult);
		System.err.println((fastResult > simplexResult ? "fast" : "simplex") + " wins");
		return fastResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #getAllocation(com.mmxlabs.scheduler.optimiser.components.IPortSlot)
	 */
	@Override
	public long getAllocation(IPortSlot slot) {
		// TODO Auto-generated method stub
		return fastAllocator.getAllocation(slot);
	}



	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#getAllocations()
	 */
	@Override
	public Iterable<CargoAllocation> getAllocations() {
		return fastAllocator.getAllocations();
	}

}
