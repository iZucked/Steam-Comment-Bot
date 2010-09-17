package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
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
	
//	final int cacheSize;
//	final int [] hits;
//	final int [] misses;
//	final List[] cache;
//	
//	int queries = 0;
//	int totalhits = 0;
//	int totalmisses = 0;
//	int evictions = 0;
//	
//	final List[]  sequences;
//	final int[][] arrivalTimess;
//	final IResource[] resources;
	
	private final ConcurrentMap<CacheKey, List<VoyagePlan>> cache = 
		new MapMaker()
			.concurrencyLevel(1)
			.weakValues()
			.expiration(10, TimeUnit.MINUTES)
			.makeComputingMap(new Function<CacheKey, List<VoyagePlan>>() {
				@Override
				public List<VoyagePlan> apply(CacheKey arg) {
					return reallySchedule(arg.getResource(), arg.getSequence(), arg.getArrivalTimes());
				}
			});
	
	public CachingAbstractSequenceScheduler(int cacheSize) {
		super();
//		this.cacheSize = cacheSize;
//		hits = new int[cacheSize];
//		misses = new int[cacheSize];
//		cache = new List[cacheSize];
//		
//		sequences = new List[cacheSize];
//		for (int i = 0; i<sequences.length ;i++) {
//			sequences[i] = new ArrayList();
//		}
//		arrivalTimess = new int[cacheSize][];
//		resources = new IResource[cacheSize];
		
	}

	private final List<VoyagePlan> reallySchedule(final IResource resource, final ISequence<T> sequence, final int [] arrivalTimes) {
		return super.schedule(resource, sequence, arrivalTimes, false);
	}
	
	@Override
	public List<VoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence, final int[] arrivalTimes, 
			final boolean adjustArrivals) {

//		queries++;
//		
//		if (queries == 10000) {
//			System.err.println("Sequence cache status");
//			System.err.println(totalhits + " hits, " + totalmisses + " misses");
//			int empties = 0;
//			for (int x : hits) {
//				if (x == 0) 
//					empties++;
//			}
//			System.err.println(empties + " slots empty out of " + cacheSize);
//			System.err.println(evictions + " evictions");
//			
//			System.err.println(Arrays.toString(hits));
//			System.err.println(Arrays.toString(misses));
//			
//			queries = 0;
//		}
		
		if (adjustArrivals) {
			return super.schedule(resource, sequence, arrivalTimes, adjustArrivals);
		} else {
//			final int hash = hash(resource, sequence, arrivalTimes);
//			// check the hash is pointing to something equal
//			if (checkEquality(hash, resource, sequence, arrivalTimes)) {
//				//cache hit
//				hits[hash]++;
//				misses[hash] = 0;
////				totalhits++;
//				return cache[hash];
//			} else {
//				final List<IVoyagePlan> result =
//					super.schedule(resource, sequence, arrivalTimes, false);
//				//do I need to clone this?
//				misses[hash]++;
////				totalmisses++;
//				if (misses[hash] > 25 /*hits[hash]*/) {
//					//evict
//					evictions++;
//					stash(hash, resource, sequence, arrivalTimes, result);
//				}
//				
//				return result;
//			}
			return cache.get(new CacheKey(resource, sequence, arrivalTimes));
		}
	}

	/**
	 * Stash the items with the given hash in the cache.
	 * @param hash
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @param result
	 */
//	private final void stash(int hash, IResource resource, ISequence<T> sequence,
//			int[] arrivalTimes, List<IVoyagePlan> result) {
//		//reset counters;
//		misses[hash] = 0;
//		hits[hash] = 1;
//		//stash the input . Presumably we have to clone everything to be sure.
//		//hopefully cloning will not cause too many problems.
//		resources[hash] = resource;
//		arrivalTimess[hash] = arrivalTimes.clone(); 
//		//elements are not cloneable
//		final List seq = sequences[hash];
//		seq.clear();
//		for (T elt : sequence) {
//			seq.add(elt);
//		}
//		
//		//stash the result in the table (cloned??)
//		cache[hash] = result;
//	}

	/**
	 * Test whether the item currently cached with the given hash equals the
	 * various bits and bobs we've just been given.
	 * 
	 * @param hash
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
//	private final boolean checkEquality(final int hash, final IResource resource,
//			final ISequence<T> sequence, final int[] arrivalTimes) {
//		if (resources[hash] != resource) return false;
//		final List cachedSequence = sequences[hash];
//		final int sz = cachedSequence.size();
//		if (sz != sequence.size()) return false;
//		
//		//element by element check
//		
//		final Iterator<Object> it1 = cachedSequence.iterator();
//		final Iterator<T> it2 = sequence.iterator();
//
//		while (it1.hasNext()) {
//			if (it1.next() != it2.next()) return false;
//		}
//		
//		//this is the only thing which can't make do with a reference check.
//		if (Arrays.equals(arrivalTimess[hash], arrivalTimes) == false) return false;
//		return true;
//	}

	/**
	 * Mangle the given stuff into a fairly unique hashcode of some kind.
	 * Presumably we don't want a cache with more than 2^31 elements?
	 * Although who knows what the collision rate will look like.
	 * 
	 * Wraps the hash code to the cache size for you.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
//	private final int hash(final IResource resource, final ISequence<T> sequence,
//			final int[] arrivalTimes) {
//		int hash = resource.hashCode();
//		final int mul = 17;
//		for (final T element : sequence) {
//			hash = hash * mul + element.hashCode();
//		}
//		
//		for (final int x : arrivalTimes) {
//			hash = hash * mul + x;
//		}
//		
//		return ((hash ^ (hash>>31)) - (hash>>31)) % cacheSize;
//	}
	
}
