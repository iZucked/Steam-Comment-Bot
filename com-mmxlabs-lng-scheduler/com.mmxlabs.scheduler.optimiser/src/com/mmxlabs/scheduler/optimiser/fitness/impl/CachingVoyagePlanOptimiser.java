/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
		private final int[] durations;
		private final int[] voyageTimes;
		private final int dischargePrice;
		private final long vesselCharterInRatePerDay;
		private final @NonNull IVessel vessel;
		private final long @NonNull [] startHeelRangeInM3;
		private final int baseFuelPricePerMT;
		private int startCV;

		// Non hashcode fields
		private final @NonNull List<@NonNull IOptionsSequenceElement> sequence;
		private final @NonNull List<@NonNull IVoyagePlanChoice> choices;
		private @NonNull IPortTimesRecord portTimesRecord;
		private @Nullable IResource resource;

		private int originalHashCode;

		public CacheKey(final @NonNull IVessel vessel, final @Nullable IResource resource, final long vesselCharterInRatePerDay, final int baseFuelPricePerMT,
				final @NonNull List<@NonNull IOptionsSequenceElement> sequence, final @NonNull IPortTimesRecord portTimesRecord, final @NonNull List<@NonNull IVoyagePlanChoice> choices,
				final long @NonNull [] startHeelRangeInM3) {
			super();
			this.vessel = vessel;
			this.resource = resource;
			this.vesselCharterInRatePerDay = vesselCharterInRatePerDay;
			this.baseFuelPricePerMT = baseFuelPricePerMT;
			final int sz = sequence.size();
			this.voyageTimes = new int[sz / 2];
			this.slots = new IPortSlot[(sz / 2) + 1];
			this.durations = new int[(sz / 2) + 1];
			int slotix = 0;
			int timeix = 0;

			int loadix = -1, dischargeix = -1;
			for (final Object o : sequence) {
				if (o instanceof PortOptions) {
					PortOptions portOptions = (PortOptions) o;
					slots[slotix] = portOptions.getPortSlot();
					durations[slotix] = portOptions.getVisitDuration();
					if ((loadix == -1) && (slots[slotix] instanceof ILoadSlot)) {
						loadix = slotix;
					} else if ((dischargeix == -1) && (slots[slotix] instanceof IDischargeSlot)) {
						dischargeix = slotix;
					}
					if (slotix == 0) {
						startCV = portOptions.getCargoCVValue();
					}
					slotix++;
				} else {
					voyageTimes[timeix++] = ((VoyageOptions) o).getAvailableTime();
				}
			}

			this.sequence = sequence;
			this.portTimesRecord = portTimesRecord;
			this.choices = choices;
			this.startHeelRangeInM3 = startHeelRangeInM3;

			if ((loadix != -1) && (dischargeix != -1)) {
				dischargePrice = ((IDischargeSlot) slots[dischargeix]).getDischargePriceCalculator().estimateSalesUnitPrice((IDischargeSlot) slots[dischargeix], portTimesRecord, null);
			} else {
				// loadPrice =
				dischargePrice = -1;
			}

			originalHashCode = doHashCode();
		}

		@Override
		public final int hashCode() {
			return originalHashCode;
		}

		private int doHashCode() {
			final int prime = 31;
			int result = 1;
			// result = prime * result + getOuterType().hashCode();
			result = (prime * result) + Arrays.hashCode(slots);
			result = (prime * result) + Arrays.hashCode(durations);
			result = (prime * result) + Arrays.hashCode(voyageTimes);

			// result = prime * result + loadPrice;
			result = (prime * result) + dischargePrice;
			result = (prime * result) + startCV;
			result = (prime * result) + baseFuelPricePerMT;
			result = (prime * result) + (int) vesselCharterInRatePerDay;

			result = (prime * result) + vessel.hashCode();

			result = (prime * result) + Arrays.hashCode(startHeelRangeInM3);

			return result;
		}

		/**
		 * This equals method almost certainly doesn't fulfil the normal equality contract; however it should be fast, and because this class is final and private it ought not end up getting used
		 * wrongly.
		 */
		@Override
		public final boolean equals(final Object obj) {

			if (obj == this) {
				return true;
			}

			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;

				return dischargePrice == other.dischargePrice//
						&& baseFuelPricePerMT == other.baseFuelPricePerMT//
						&& startCV == other.startCV //
						&& (vessel == other.vessel) //
						&& Arrays.equals(startHeelRangeInM3, other.startHeelRangeInM3) //
						&& Arrays.equals(voyageTimes, other.voyageTimes) //
						&& Arrays.equals(durations, other.durations)//
						&& Equality.shallowEquals(slots, other.slots);
			}
			return false;
		}
	}

	private final AbstractCache<@NonNull CacheKey, VoyagePlan> cache;

	public CachingVoyagePlanOptimiser(final @NonNull IVoyagePlanOptimiser delegate, final int cacheSize) {
		super();
		final IKeyEvaluator<@NonNull CacheKey, VoyagePlan> evaluator = new IKeyEvaluator<@NonNull CacheKey, VoyagePlan>() {

			@Override
			final public @NonNull Pair<@NonNull CacheKey, VoyagePlan> evaluate(final CacheKey arg) {

				final VoyagePlan plan = delegate.optimise(arg.resource, arg.vessel, arg.startHeelRangeInM3, arg.baseFuelPricePerMT, arg.vesselCharterInRatePerDay, arg.portTimesRecord, arg.sequence,
						arg.choices);

				plan.setCacheLocked(true);
				// don't clone key
				return new Pair<@NonNull CacheKey, VoyagePlan>(arg, plan);
			}
		};
		this.cache = new LHMCache<@NonNull CacheKey, VoyagePlan>("VPO", evaluator, cacheSize);
	}

	@Override
	@Nullable
	public VoyagePlan optimise(@Nullable final IResource resource, final IVessel vessel, final long[] startHeelRangeInM3, final int baseFuelPricePerMT, final long vesselCharterInRatePerDay,
			final IPortTimesRecord portTimesRecord, final List<@NonNull IOptionsSequenceElement> basicSequence, final List<@NonNull IVoyagePlanChoice> choices) {

		final VoyagePlan best = cache.get(new CacheKey(vessel, resource, vesselCharterInRatePerDay, baseFuelPricePerMT, basicSequence, portTimesRecord, choices, startHeelRangeInM3));

		return best;
	}
}
