/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A voyage plan optimiser which acts as a caching proxy for a real VPO, passed in through the constructor
 * 
 * @author hinton
 * 
 * @param
 */
public final class CachingVoyagePlanOptimiser implements IVoyagePlanOptimiser {

	/**
	 * The class which keys into the cache ( {@link CachingVoyagePlanOptimiser#cache}). NOTE: the cache key may not be completely stable, for speed reasons; specifically, the sequence and arrivalTimes
	 * are <em>not cloned</em> from input, and so are likely to be invalid once the cache entry for the key has been computed.
	 * 
	 * @author hinton
	 * 
	 */
	private final class CacheKey {

		// Hashcode / Equals fields
		private final IPortSlot[] slots;
		private final int[] voyageTimes;
		private final int dischargePrice;
		private final int vesselCharterInRatePerDay;
		private final IVessel vessel;
		protected final long startHeel;

		// Non hashcode fields
		private final int baseFuelPricePerMT;
		private final List<IOptionsSequenceElement> sequence;
		private final List<IVoyagePlanChoice> choices;
		protected IPortTimesRecord portTimesRecord;
		protected IResource resource;

		public CacheKey(final IVessel vessel, final IResource resource, final int vesselCharterInRatePerDay, final int baseFuelPricePerMT, final List<IOptionsSequenceElement> sequence,
				final IPortTimesRecord portTimesRecord, final List<IVoyagePlanChoice> choices, final long startHeel) {
			super();
			this.vessel = vessel;
			this.resource = resource;
			this.vesselCharterInRatePerDay = vesselCharterInRatePerDay;
			this.baseFuelPricePerMT = baseFuelPricePerMT;
			final int sz = sequence.size();
			this.voyageTimes = new int[sz / 2];
			this.slots = new IPortSlot[(sz / 2) + 1];
			int slotix = 0;
			int timeix = 0;

			int loadix = -1, dischargeix = -1;
			for (final Object o : sequence) {
				if (o instanceof PortOptions) {
					slots[slotix] = ((PortOptions) o).getPortSlot();
					if ((loadix == -1) && (slots[slotix] instanceof ILoadSlot)) {
						loadix = slotix;
					} else if ((dischargeix == -1) && (slots[slotix] instanceof IDischargeSlot)) {
						dischargeix = slotix;
					}
					slotix++;
				} else {
					voyageTimes[timeix++] = ((VoyageOptions) o).getAvailableTime();
				}
			}

			this.sequence = sequence;
			this.portTimesRecord = portTimesRecord;
			this.choices = choices;
			this.startHeel = startHeel;

			if ((loadix != -1) && (dischargeix != -1)) {
				// loadPrice =
				// ((ILoadSlot)
				// slots[loadix]).getPurchasePriceAtTime(arrivalTimes.get(loadix));
				dischargePrice = ((IDischargeSlot) slots[dischargeix]).getDischargePriceCalculator().estimateSalesUnitPrice((IDischargeSlot) slots[dischargeix], portTimesRecord, null);
			} else {
				// loadPrice =
				dischargePrice = -1;
			}
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			// result = prime * result + getOuterType().hashCode();
			result = (prime * result) + Arrays.hashCode(slots);
			result = (prime * result) + Arrays.hashCode(voyageTimes);

			// result = prime * result + loadPrice;
			result = (prime * result) + dischargePrice;
			result = (prime * result) + vesselCharterInRatePerDay;

			result = (prime * result) + ((vessel == null) ? 0 : vessel.hashCode());

			result = (prime * result) + (int) startHeel;

			return result;
		}

		/**
		 * This equals method almost certainly doesn't fulfil the normal equality contract; however it should be fast, and because this class is final and private it ought not end up getting used
		 * wrongly.
		 */
		@Override
		public final boolean equals(final Object obj) {
			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				// if (getClass() != obj.getClass())
				// return false;
				// CacheKey other = (CacheKey) obj;
				// if (!getOuterType().equals(other.getOuterType()))
				// return false;

				return Equality.shallowEquals(slots, other.slots) && (vessel == other.vessel) && Arrays.equals(voyageTimes, other.voyageTimes) && (// loadPrice == other.loadPrice &&
						dischargePrice == other.dischargePrice && startHeel == other.startHeel);
			}
			return false;
		}
	}

	private final IVoyagePlanOptimiser delegate;

	// private final ConcurrentMap<CacheKey, Pair<VoyagePlan, Long>> cache;// =
	// null;
	private final AbstractCache<CacheKey, Pair<VoyagePlan, Long>> cache;

	VoyagePlan bestPlan;
	long bestCost;

	private List<IOptionsSequenceElement> basicSequence;
	private IVessel vessel;
	private int vesselCharterInRatePerDay;
	private int baseFuelPricePerMT;
	private final List<IVoyagePlanChoice> choices = new ArrayList<IVoyagePlanChoice>();

	private IPortTimesRecord portTimesRecord;

	private long startHeel;

	private IResource resource;

	public CachingVoyagePlanOptimiser(final IVoyagePlanOptimiser delegate, final int cacheSize) {
		super();
		this.delegate = delegate;
		final IKeyEvaluator<CacheKey, Pair<VoyagePlan, Long>> evaluator = new IKeyEvaluator<CacheKey, Pair<VoyagePlan, Long>>() {

			@Override
			final public Pair<CacheKey, Pair<VoyagePlan, Long>> evaluate(final CacheKey arg) {
				final Pair<VoyagePlan, Long> answer = new Pair<VoyagePlan, Long>();

				delegate.reset();
				for (final IVoyagePlanChoice c : arg.choices) {
					delegate.addChoice(c);
				}
				delegate.setVessel(arg.vessel, arg.resource, arg.baseFuelPricePerMT);
				delegate.setVesselCharterInRatePerDay(arg.vesselCharterInRatePerDay);
				delegate.setBasicSequence(arg.sequence);
				delegate.setPortTimesRecord(arg.portTimesRecord);
				delegate.setStartHeel(arg.startHeel);
				delegate.init();
				delegate.optimise();

				answer.setBoth(delegate.getBestPlan(), delegate.getBestCost());

				// don't clone key
				return new Pair<CacheKey, Pair<VoyagePlan, Long>>(arg, answer);
			}
		};
		this.cache = new LHMCache<CacheKey, Pair<VoyagePlan, Long>>("VPO", evaluator, cacheSize);
	}

	@Override
	public VoyagePlan optimise() {

		final Pair<VoyagePlan, Long> best = cache.get(new CacheKey(vessel, resource, vesselCharterInRatePerDay, baseFuelPricePerMT, basicSequence, portTimesRecord, choices, startHeel));

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
	public List<IOptionsSequenceElement> getBasicSequence() {
		return basicSequence;
	}

	@Override
	public void setBasicSequence(final List<IOptionsSequenceElement> basicSequence) {
		this.basicSequence = basicSequence;
	}

	@Override
	public IVessel getVessel() {
		return this.vessel;
	}

	/**
	 */
	@Override
	public void setVessel(final IVessel vessel, final IResource resource, final int baseFuelPricePerMT) {
		this.vessel = vessel;
		this.resource = resource;
		this.baseFuelPricePerMT = baseFuelPricePerMT;
	}

	@Override
	public void setBaseFuelPricePerMT(int baseFuelPricePerMT) {
		this.baseFuelPricePerMT = baseFuelPricePerMT;
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
	public ILNGVoyageCalculator getVoyageCalculator() {
		return delegate.getVoyageCalculator();
	}

	@Override
	public void addChoice(final IVoyagePlanChoice choice) {
		choices.add(choice);
	}

	@Override
	public void setPortTimesRecord(final IPortTimesRecord portTimesRecord) {
		this.portTimesRecord = portTimesRecord;
	}

	@Override
	public void setStartHeel(long heelVolumeInM3) {
		startHeel = heelVolumeInM3;
	}

	@Override
	public long getStartHeel() {
		return startHeel;
	}

	@Override
	public void setVesselCharterInRatePerDay(int charterInRatePerDay) {
		this.vesselCharterInRatePerDay = charterInRatePerDay;
	}

}
