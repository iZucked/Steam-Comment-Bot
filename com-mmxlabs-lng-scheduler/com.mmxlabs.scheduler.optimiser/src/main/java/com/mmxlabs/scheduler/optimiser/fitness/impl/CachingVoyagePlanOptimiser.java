/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A voyage plan optimiser which acts as a caching proxy for a real VPO, passed
 * in through the constructor
 * 
 * @author hinton
 * 
 * @param <T>
 */
public final class CachingVoyagePlanOptimiser<T> implements IVoyagePlanOptimiser<T> {
	private final class CacheKey {
		private final IVessel vessel;
		private final int[] times;
		private final IPortSlot[] slots;
		private final List<Object> sequence;
		private final List<IVoyagePlanChoice> choices;

		public CacheKey(final IVessel vessel, final List<Object> sequence,
				final List<IVoyagePlanChoice> choices) {
			super();
			this.vessel = vessel;
			final int sz = sequence.size();
			this.times = new int[sz / 2];
			this.slots = new IPortSlot[sz / 2 + 1];
			int slotix = 0;
			int timeix = 0;
			for (final Object o : sequence) {
				if (o instanceof PortDetails) {
					slots[slotix++] = ((PortDetails) o).getPortSlot();
				} else {
					times[timeix++] = ((VoyageOptions) o).getAvailableTime();
				}
			}
			this.sequence = sequence;
			this.choices = choices;
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
//			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(slots);
			result = prime * result + Arrays.hashCode(times);
			result = prime * result
					+ ((vessel == null) ? 0 : vessel.hashCode());
			return result;
		}

		/**
		 * This equals method almost certainly doesn't fulfil the normal
		 * equality contract; however it should be fast, and because this class
		 * is final and private it ought not end up getting used wrongly.
		 */
		@Override
		public final boolean equals(final Object obj) {
			final CacheKey other = (CacheKey) obj;
			// if (getClass() != obj.getClass())
			// return false;
			// CacheKey other = (CacheKey) obj;
			// if (!getOuterType().equals(other.getOuterType()))
			// return false;
			
			return  
				Equality.shallowEquals(slots, other.slots) 
				&&(vessel == other.vessel)&&
				Arrays.equals(times, other.times);
		}

		private final CachingVoyagePlanOptimiser<T> getOuterType() {
			return CachingVoyagePlanOptimiser.this;
		}
	}

	private final IVoyagePlanOptimiser<T> delegate;

	// private final ConcurrentMap<CacheKey, Pair<VoyagePlan, Long>> cache;// =
	// null;
	private final AbstractCache<CacheKey,Pair<VoyagePlan,Long>> cache;

	VoyagePlan bestPlan;
	long bestCost;

	private List<Object> basicSequence;
	private IVessel vessel;
	private final List<IVoyagePlanChoice> choices = new ArrayList<IVoyagePlanChoice>();

	public CachingVoyagePlanOptimiser(final IVoyagePlanOptimiser<T> delegate,
			final int cacheSize) {
		super();
		this.delegate = delegate;
		final IKeyEvaluator<CacheKey, Pair<VoyagePlan, Long>>
		evaluator = new IKeyEvaluator<CacheKey, Pair<VoyagePlan, Long>>() {

			@Override
			final public Pair<CacheKey, Pair<VoyagePlan, Long>> evaluate(final CacheKey arg) {
				final Pair<VoyagePlan, Long> answer = new Pair<VoyagePlan, Long>();

				delegate.reset();
				for (final IVoyagePlanChoice c:arg.choices) {
					delegate.addChoice(c);
				}
				delegate.setVessel(arg.vessel);

				delegate.setBasicSequence(arg.sequence);
				delegate.init();
				delegate.optimise();

				answer.setBoth(delegate.getBestPlan(), delegate.getBestCost());

				return new Pair<CacheKey, Pair<VoyagePlan, Long>>(arg, answer); //don't clone key
			}
		};
		this.cache = new LHMCache<CacheKey, Pair<VoyagePlan, Long>>("VPO", evaluator, cacheSize);
	}

	@Override
	public VoyagePlan optimise() {

		final Pair<VoyagePlan, Long> best = cache.get(new CacheKey(vessel,
				basicSequence, choices));

//		bestPlan = (VoyagePlan) best.getFirst().clone();
		bestPlan = best.getFirst();
		bestCost = best.getSecond();

		return bestPlan;
	}

	@Override
	public void init() {

	}

	@Override
	public void reset() {
		choices.clear();
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}

	@Override
	public List<Object> getBasicSequence() {
		return basicSequence;
	}

	@Override
	public void setBasicSequence(final List<Object> basicSequence) {
		this.basicSequence = basicSequence;
	}

	@Override
	public IVessel getVessel() {
		return this.vessel;
	}

	@Override
	public void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	@Override
	public long getBestCost() {
		return bestCost;
	}

	@Override
	public VoyagePlan getBestPlan() {
		return bestPlan;
	}

	@Override
	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return delegate.getVoyageCalculator();
	}

	@Override
	public void addChoice(final IVoyagePlanChoice choice) {
		choices.add(choice);
	}

}
