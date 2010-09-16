package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * A voyage plan optimiser which acts as a caching proxy for a real VPO,
 * passed in through the constructor
 * @author hinton
 *
 * @param <T>
 */
public class CachingVoyagePlanOptimiser<T> implements IVoyagePlanOptimiser<T> {
	
	private final class CacheKey {
		private final IVessel vessel;
		private final int[] times;
		private final IPortSlot[] slots;
		
		public CacheKey(IVessel vessel, List<Object> sequence) {
			super();
			this.vessel = vessel;
			final int sz = sequence.size();
			this.times = new int[sz / 2];
			this.slots = new IPortSlot[sz / 2 + 1];
			int slotix = 0;
			int timeix = 0;
			for (final Object o : sequence) {
				if (o instanceof IPortDetails) {
					slots[slotix++] = ((IPortDetails) o).getPortSlot();
				} else {
					times[timeix++] = ((IVoyageOptions) o).getAvailableTime();
				}
			}
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(slots);
			result = prime * result + Arrays.hashCode(times);
			result = prime * result
					+ ((vessel == null) ? 0 : vessel.hashCode());
			return result;
		}

		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.equals(slots, other.slots))
				return false;
			if (!Arrays.equals(times, other.times))
				return false;
			if (vessel == null) {
				if (other.vessel != null)
					return false;
			} else if (!vessel.equals(other.vessel))
				return false;
			return true;
		}

		private final CachingVoyagePlanOptimiser getOuterType() {
			return CachingVoyagePlanOptimiser.this;
		}	
	}
	
	private final IVoyagePlanOptimiser<T> delegate;
	
//	private int cacheSize;
//	
//	final int[] hits;
//	final int[] misses;
//	final long[] cachedBestCosts;
//	final IVoyagePlan [] cachedPlans;
//
//	private final IVessel[] storedVessels;
//	private final List<Integer>[] storedDurationLists;
//	private final List<IPortSlot>[] storedSlotLists;
	
	
	private final ConcurrentMap<CacheKey, Pair<IVoyagePlan, Long>> cache = 
		new MapMaker()
			.concurrencyLevel(1)
			.weakValues()
			.expiration(1, TimeUnit.MINUTES)
			.makeComputingMap(new Function<CacheKey, Pair<IVoyagePlan, Long>>() {
				@Override
				public Pair<IVoyagePlan, Long> apply(CacheKey arg) {
					final Pair<IVoyagePlan, Long> answer = new Pair<IVoyagePlan, Long>();
					
					//this is horribly dodgy, and depends on assumptions about
					//the inner workings of the mapmaker.
					delegate.optimise();
					answer.setBoth(delegate.getBestPlan(), delegate.getBestCost());
					
					return answer;
				}
			});
	
	IVoyagePlan bestPlan;
	long bestCost;
	public CachingVoyagePlanOptimiser(final IVoyagePlanOptimiser<T> delegate,
			final int cacheSize) {
		super();
		this.delegate = delegate;
//		this.cacheSize = cacheSize;
//		hits = new int[cacheSize];
//		misses = new int[cacheSize];
//		cachedBestCosts = new long[cacheSize];
//		cachedPlans = new IVoyagePlan[cacheSize];
//		
//		storedVessels = new IVessel[cacheSize];
//		storedDurationLists = new List[cacheSize];
//		storedSlotLists = new List[cacheSize];
//		for (int i = 0; i<cacheSize; i++) {
//			storedDurationLists[i] = new ArrayList();
//			storedSlotLists[i] = new ArrayList();
//		}
	}

//	/**
//	 * Store the input data for collision checking in the cache
//	 * @param hash
//	 */
//	private final void storeInputs(final int hash) {
//		storedVessels[hash] = getVessel();
//		
//		final List<Object> sequence = delegate.getBasicSequence();
//		final List<IPortSlot> storedSlotList = storedSlotLists[hash];
//		final List<Integer> storedDurationList = storedDurationLists[hash];
//		
//		storedSlotList.clear();
//		storedDurationList.clear();
//		
//		for (final Object o : sequence) {
//			if (o instanceof IPortDetails) {
//				storedSlotList.add(((IPortDetails)o).getPortSlot());
//			} else {
//				storedDurationList.add(((IVoyageOptions)o).getAvailableTime());
//			}
//		}
//	}
//	/**
//	 * Determine whether a collision has happened
//	 * @param hash
//	 * @return
//	 */
//	private final boolean collision(final int hash) {
//		if (getVessel() != storedVessels[hash]) return true;
//		
//		final List<IPortSlot> storedSlotList = storedSlotLists[hash];
//		final List<Integer> storedDurationList = storedDurationLists[hash];
//		
//		final List<Object> sequence = delegate.getBasicSequence();
//		
//		if (storedSlotList.size() + storedDurationList.size() != sequence.size()) 
//			return true;
//		
//		final Iterator<IPortSlot> slotIterator = storedSlotList.iterator();
//		final Iterator<Integer> durationIterator = storedDurationList.iterator();
//		
//		for (final Object o : sequence) {
//			if (o instanceof IPortDetails) {
//				final IPortDetails slot = (IPortDetails) o;
//				if (slot.getPortSlot() != slotIterator.next())
//					return true;
//			} else {
//				final IVoyageOptions voy = (IVoyageOptions) o;
//				if (durationIterator.next().intValue() != voy.getAvailableTime()) 
//					return true;
//			}
//		}
//		
//		return false;
//	}
//	/**
//	 * Compute a hash of the current input to the delegate
//	 * @return
//	 */
//	private final int hash() {
//		int hash = 23;
//		final int multiplier = 17;
//		//what things do we hash together?
//		//the vessel
//		hash = hash * multiplier + getVessel().hashCode();
//		
//		//some features of the basic sequence
//		final List<Object> sequence = delegate.getBasicSequence();
////		boolean isPortDetails = true;
//		for (final Object o : sequence) {
//			if (o instanceof IPortDetails) {
//				IPortDetails portDetails = (IPortDetails) o;
//				//we care about: the port slot
//				//TODO should this just be the port
//				hash = hash * multiplier + portDetails.getPortSlot().hashCode();
//			} else {
//				final IVoyageOptions voy = (IVoyageOptions) o;
//				hash = hash * multiplier + 
//					voy.getAvailableTime();
//			}
////			isPortDetails = !isPortDetails;
//		}
//		
//		return ((hash ^ (hash>>31)) - (hash>>31)) % cacheSize;
//	}
//	
//	private final boolean evict(final int hash) {
//		return misses[hash] > 25;
////		return misses[hash] > hits[hash]; //TODO perhaps this should just be
//		//misses > 10 (or so) and reset every time there's a hit?
//	}
//	
//	int tests = 0;
//	int totalHits = 0;
//	int totalMisses = 0;
	
	@Override
	public IVoyagePlan optimise() {
//		// check whether the current state is already in the cache; if it is,
//		// we can sneakily just recover it
//		tests++;
//		
//		if (tests % 600000 == 0) {
//
//			System.err.println("CVPO Cache Data");
//			int emptySlots = 0;
//			for (IVessel v : storedVessels) {
//				if (v == null) {
//					emptySlots++;
//				}
//			}
//			
//			System.err.println(emptySlots + " of " + cacheSize + " empty");
//			System.err.println(totalHits + " hits, " + totalMisses + " misses");
//			System.err.println(100.0 * totalHits / tests + " % hit rate");
//		}
//		
//		final int hash = hash();
//		if (collision(hash)) {
//			bestPlan = delegate.optimise();
//			bestCost = delegate.getBestCost();
//			totalMisses++;
//			if (evict(hash)) {
//				misses[hash] = 0;
//				hits[hash] = 1;
//				
//				//store result (ought I clone it?)
//				cachedBestCosts[hash] = bestCost;
//				cachedPlans[hash] = bestPlan;
//				
//				//store inputs, for next collision check
//				storeInputs(hash);
//			} else {
//				misses[hash]++;
//			}
//		} else {
//			totalHits++;
//			hits[hash]++;
//			misses[hash] = 0;
//			bestCost = cachedBestCosts[hash];
//			bestPlan = cachedPlans[hash];
//		}
		
		final Pair<IVoyagePlan, Long> best = cache.get(new CacheKey(getVessel(), getBasicSequence()));
		
		bestPlan = best.getFirst();
		bestCost = best.getSecond();
		
		return bestPlan;
	}
	
	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public void reset() {
		delegate.reset();
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}

	@Override
	public List<Object> getBasicSequence() {
		return delegate.getBasicSequence();
	}

	@Override
	public void setBasicSequence(List<Object> basicSequence) {
		delegate.setBasicSequence(basicSequence);
	}

	@Override
	public IVessel getVessel() {
		return delegate.getVessel();
	}

	@Override
	public void setVessel(IVessel vessel) {
		delegate.setVessel(vessel);
	}

	@Override
	public long getBestCost() {
		return bestCost;
	}

	@Override
	public IVoyagePlan getBestPlan() {
		return bestPlan;
	}

	@Override
	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return delegate.getVoyageCalculator();
	}

	@Override
	public void setVoyageCalculator(ILNGVoyageCalculator<T> voyageCalculator) {
		delegate.setVoyageCalculator(voyageCalculator);
	}

	@Override
	public void addChoice(IVoyagePlanChoice choice) {
		delegate.addChoice(choice);
	}

}
