package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.common.caches.SimpleCache;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * An extension of the abstract sequence scheduler which caches subclass calls
 * to schedule when adjustArrivalTimes is false. Currently is not really very
 * memory-friendly at all. Should be made to use weak references to let the GC
 * clear out the cache when necessary (note to self: can we subclass weak refs to
 * get notice of when the ref is cleared and reset the associated table data?)
 * 
 * @author hinton
 *
 * @param <T>
 */
public abstract class CachingAbstractSequenceScheduler<T> 
 	extends AbstractSequenceScheduler<T> {

	public CachingAbstractSequenceScheduler() {
		this(8000);
	}
	
	private final class CacheKey {
		private final IModifiableSequence<T> sequence;
		private final int[] arrivalTimes;
		private final IResource resource;
		private final int hashCode;
		
		public CacheKey(IResource resource, ISequence<T> sequence, int[] arrivalTimes
				) {
			super();
			this.sequence = new ListModifiableSequence<T> (new ArrayList<T>( sequence.size() ));
			for (T elt : sequence) {
				this.sequence.add(elt);
			}
			this.arrivalTimes = arrivalTimes.clone(); //must this be cloned?
			this.resource = resource;
			
			{
				final int prime = 31;
				int hash = Arrays.hashCode(arrivalTimes);
				hash = prime * hash + resource.hashCode();
				hash = prime * hash + sequence.hashCode();
				hashCode = hash;
			}
		}

		@Override
		public final int hashCode() {
			return hashCode;
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
			if (!Arrays.equals(arrivalTimes, other.arrivalTimes))
				return false;
			if (hashCode != other.hashCode)
				return false;
			if (resource == null) {
				if (other.resource != null)
					return false;
			} else if (!resource.equals(other.resource))
				return false;
			if (sequence == null) {
				if (other.sequence != null)
					return false;
			} else if (!sequence.equals(other.sequence))
				return false;
			return true;
		}

		private final CachingAbstractSequenceScheduler getOuterType() {
			return CachingAbstractSequenceScheduler.this;
		}

		public ISequence<T> getSequence() {
			return sequence;
		}

		public int[] getArrivalTimes() {
			return arrivalTimes;
		}

		public IResource getResource() {
			return resource;
		}
	}
	
//	private final ConcurrentMap<CacheKey, List<VoyagePlan>> cache ;
	private final SimpleCache<CacheKey, List<VoyagePlan>> cache;
	public CachingAbstractSequenceScheduler(int cacheSize) {
		super();
		
		cache = new SimpleCache<CacheKey, List<VoyagePlan>>("Sequence Scheduler",
				new IKeyEvaluator<CacheKey, List<VoyagePlan>>() {

					@Override
					public List<VoyagePlan> evaluate(CacheKey arg) {
						return  reallySchedule(arg.getResource(), arg.getSequence(), arg.getArrivalTimes());
					}
			
				}, cacheSize, 3);
//		cache = 
//			new MapMaker()
//				.concurrencyLevel(1)
//				.expiration(10, TimeUnit.MINUTES)
//				.weakValues()
//				.initialCapacity(cacheSize)
//				.makeComputingMap(new Function<CacheKey, List<VoyagePlan>>() {
//					@Override
//					public List<VoyagePlan> apply(CacheKey arg) {
//						return reallySchedule(arg.getResource(), arg.getSequence(), arg.getArrivalTimes());
//					}
//				});
	}

	private final List<VoyagePlan> reallySchedule(final IResource resource, final ISequence<T> sequence, final int [] arrivalTimes) {
		return super.schedule(resource, sequence, arrivalTimes, false);
	}
	
	@Override
	public List<VoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence, final int[] arrivalTimes, 
			final boolean adjustArrivals) {
		if (adjustArrivals) {
			return super.schedule(resource, sequence, arrivalTimes, adjustArrivals);
		} else {
			final List<VoyagePlan> cacheValue = cache.get(new CacheKey(resource, sequence, arrivalTimes));
			
//			final List<VoyagePlan> realValue = super.schedule(resource, sequence, arrivalTimes, adjustArrivals);
			
//			if (!realValue.toString().equals(cacheValue.toString())) {
//				Iterator<VoyagePlan> realIterator = realValue.iterator();
//				Iterator<VoyagePlan> cacheIterator = cacheValue.iterator();
//				while (realIterator.hasNext() && cacheIterator.hasNext()) {
//					String s1 = cacheIterator.next().toString();
//					String s2 = realIterator.next().toString();
//					
//					if (!s1.equals(s2)) {
//						System.err.println("ERROR:");
//						System.err.println("CACHE:" + s1);
//						System.err.println(" REAL:" +s2);
//						
//					}
//				}
//			}
			
			return cacheValue;
		}
	}

	
}
