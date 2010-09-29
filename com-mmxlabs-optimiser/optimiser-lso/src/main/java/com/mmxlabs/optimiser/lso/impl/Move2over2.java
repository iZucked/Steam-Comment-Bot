package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * A 2-opt-2 move; swaps the tails of two sequences in a solution given two breakpoints in those sequences.
 * For example, given a solution which contains two sequences thus:
 * <pre>
 *    A = [a1, a2, ... an]
 *    B = [b1, b2, ... bm]
 * </pre>
 * 
 * and breakpoints q < n and w < m, the resulting sequences look like
 * <pre>
 *    A' = [a1, a2, .. aq-1, bw, bw+1, ... bm-1, an]
 *    B' = [b1, b2, .. bw-1, aq, aq+1, ... an-1, bm]
 * </pre>
 * 
 * Note that the last elements of A and B are unchanged by the operation; this is in contrast to a traditional
 * 2-opt-2 move because we usually want to preserve the start and end locations in a sequence.
 * 
 * If you have some unusual situation in which this is not the case, the method {@link setPreserveLastElements} lets
 * you turn this fix off.
 * 
 * @author hinton
 *
 * @param <T>
 *            Sequence element type
 */
public class Move2over2<T> implements IMove<T> {
	/**
	 * If true, the last elements of each sequence are not included in the exchange,
	 * making this a limited class of 4opt2.
	 */
	private boolean preserveStartAndEnd = true;
	/**
	 * The breakpoint in the sequence for resource 1; elements before this element will remain assigned to resource 1
	 * and elements from this element onward will be moved to the sequence for resource 2
	 */
	private int resource1Position;
	/**
	 * The breakpoint in the sequence for resource 2; elements before this element remain in sequence, and subsequent
	 * elements are moved
	 */
	private int resource2Position;
	private IResource resource1, resource2;
	
	
	
	@Override
	public Collection<IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(resource1, resource2);
	}

	@Override
	public void apply(IModifiableSequences<T> sequences) {
		IModifiableSequence<T> A = sequences.getModifiableSequence(resource1);
		IModifiableSequence<T> B = sequences.getModifiableSequence(resource2);
		
		final int end1 = A.size() - (preserveStartAndEnd ? 1 : 0);
		final int end2 = B.size() - (preserveStartAndEnd ? 1 : 0);
		
		final ISegment<T> tailOfA = A.getSegment(resource1Position, end1);
		final ISegment<T> tailOfB = B.getSegment(resource2Position, end2);
		
		A.insert(resource1Position, tailOfB);
		B.insert(resource2Position, tailOfA);
		A.remove(tailOfA);
		B.remove(tailOfB);
	}

	@Override
	public boolean validate(ISequences<T> sequences) {
		try {
			final int offset = preserveStartAndEnd ? 1 : 0;
			if (resource1 == null) return false;
			if (resource2 == null) return false;
			if (resource1Position < offset) return false;
			if (resource2Position < offset) return false;
			final Map<IResource, ISequence<T>> sequenceMap = sequences.getSequences();
			if (sequenceMap.containsKey(resource1) == false) return false;
			if (sequenceMap.containsKey(resource2) == false) return false;
			final ISequence<T> A = sequenceMap.get(resource1);
			final ISequence<T> B = sequenceMap.get(resource2);
			if (resource1Position >= (A.size() /*-  offset*/)) return false;
			if (resource2Position >= (B.size() /*-  offset*/)) return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("r1 (%s) %d, r2 (%s) %d %s",
				resource1.getName(), resource1Position, resource2.getName(), resource2Position, 
				preserveStartAndEnd ? "(preserve start and end)" : "");
	}

	public boolean isPreserveStartAndEnd() {
		return preserveStartAndEnd;
	}

	public void setPreserveStartAndEnd(boolean preserveStartAndEnd) {
		this.preserveStartAndEnd = preserveStartAndEnd;
	}

	public int getResource1Position() {
		return resource1Position;
	}

	public void setResource1Position(int resource1Position) {
		this.resource1Position = resource1Position;
	}

	public int getResource2Position() {
		return resource2Position;
	}

	public void setResource2Position(int resource2Position) {
		this.resource2Position = resource2Position;
	}

	public IResource getResource1() {
		return resource1;
	}

	public void setResource1(IResource resource1) {
		this.resource1 = resource1;
	}

	public IResource getResource2() {
		return resource2;
	}

	public void setResource2(IResource resource2) {
		this.resource2 = resource2;
	}
}
