/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * @author hinton
 * 
 * @param <T>
 */
public interface ICargoAllocator<T> {	
	public void setTotalVolumeLimitProvider(ITotalVolumeLimitProvider<T> tvlp);

	public abstract void init();

	public abstract void reset();

	/**
	 * Notify the allocator of a cargo to be included in the allocation. This
	 * method will probably be called in a loop iterating over a sequence. It
	 * allows for quite a lot of flexibility in the outer optimisation, so load
	 * and discharge slots can be freely connected up without causing problems.
	 * 
	 * This might loose a little performance in the solver, as no constraints
	 * can be kept between runs.
	 * 
	 * @param loadDetails
	 *            The details of the load slot
	 * @param dischargeDetails
	 *            details of the paired discharge slot
	 * @param loadTime
	 *            time at which loading happens
	 * @param dischargeTime
	 *            time at which discharge happens
	 * @param requiredLoadVolume
	 *            the volume of LNG which must be loaded to meet fuel
	 *            requirements for the voyage
	 * @param vesselCapacity
	 *            the capacity of the vessel to which this cargo has been
	 *            allocated
	 */
	public abstract void addCargo(final PortDetails loadDetails,
			final PortDetails dischargeDetails, final int loadTime,
			final int dischargeTime, final long requiredLoadVolume,
			final long vesselCapacity);

	public abstract void solve();

	public abstract long getProfit();

	public abstract long getAllocation(IPortSlot slot);

	/**
	 * @return
	 */
	public Iterable<IAllocationAnnotation> getAllocations();
}