/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * A 2-opt-2 move; swaps the tails of two sequences in a solution given two breakpoints in those sequences. For example, given a solution which contains two sequences thus:
 * 
 * <pre>
 *    A = [a1, a2, ... an]
 *    B = [b1, b2, ... bm]
 * </pre>
 * 
 * and breakpoints q < n and w < m, the resulting sequences look like
 * 
 * <pre>
 *    A' = [a1, a2, .. aq-1, bw, bw+1, ... bm-1, an]
 *    B' = [b1, b2, .. bw-1, aq, aq+1, ... an-1, bm]
 * </pre>
 * 
 * Note that the last elements of A and B are unchanged by the operation; this is in contrast to a traditional 2-opt-2 move because we usually want to preserve the start and end locations in a
 * sequence.
 * 
 * If you have some unusual situation in which this is not the case, the method {@link setPreserveLastElements} lets you turn this fix off.
 * 
 * @author hinton
 * 
 */
public class Move2over2 implements IMove {
	/**
	 * If true, the last elements of each sequence are not included in the exchange, making this a limited class of 4opt2.
	 */
	private boolean preserveStartAndEnd = true;
	/**
	 * The breakpoint in the sequence for resource 1; elements before this element will remain assigned to resource 1 and elements from this element onward will be moved to the sequence for resource 2
	 */
	private int resource1Position;
	/**
	 * The breakpoint in the sequence for resource 2; elements before this element remain in sequence, and subsequent elements are moved
	 */
	private int resource2Position;
	private IResource resource1, resource2;

	@Override
	@NonNull
	public final Collection<@NonNull IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(resource1, resource2);
	}

	@Override
	public final void apply(@NonNull final IModifiableSequences sequences) {
		final IModifiableSequence A = sequences.getModifiableSequence(resource1);
		final IModifiableSequence B = sequences.getModifiableSequence(resource2);

		final int end1 = A.size() - (preserveStartAndEnd ? 1 : 0);
		final int end2 = B.size() - (preserveStartAndEnd ? 1 : 0);

		final ISegment tailOfA = A.getSegment(resource1Position, end1);
		final ISegment tailOfB = B.getSegment(resource2Position, end2);

		A.insert(resource1Position, tailOfB);
		B.insert(resource2Position, tailOfA);
		A.remove(tailOfA);
		B.remove(tailOfB);
	}

	@Override
	public final boolean validate(@NonNull final ISequences sequences) {
		try {
			final int offset = preserveStartAndEnd ? 1 : 0;
			if (resource1 == null) {
				return false;
			}
			if (resource2 == null) {
				return false;
			}
			if (resource1Position < offset) {
				return false;
			}
			if (resource2Position < offset) {
				return false;
			}
			
			final Map<IResource, ISequence> sequenceMap = sequences.getSequences();
			if (sequenceMap.containsKey(resource1) == false) {
				return false;
			}
			if (sequenceMap.containsKey(resource2) == false) {
				return false;
			}
			final ISequence A = sequenceMap.get(resource1);
			final ISequence B = sequenceMap.get(resource2);
			
			// ensure sequences are not of length 0
			if (resource1Position >= A.size() - 1 && resource2Position >= B.size() - 1) {
				return false;
			}
				
			if (resource1Position >= (A.size() /*-  offset*/)) {
				return false;
			}
			if (resource2Position >= (B.size() /*-  offset*/)) {
				return false;
			}
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public final String toString() {
		return String.format("r1 (%s) %d, r2 (%s) %d %s", resource1.getName(), resource1Position, resource2.getName(), resource2Position, preserveStartAndEnd ? "(preserve start and end)" : "");
	}

	public final boolean isPreserveStartAndEnd() {
		return preserveStartAndEnd;
	}

	public final void setPreserveStartAndEnd(final boolean preserveStartAndEnd) {
		this.preserveStartAndEnd = preserveStartAndEnd;
	}

	public final int getResource1Position() {
		return resource1Position;
	}

	public final void setResource1Position(final int resource1Position) {
		this.resource1Position = resource1Position;
	}

	public final int getResource2Position() {
		return resource2Position;
	}

	public final void setResource2Position(final int resource2Position) {
		this.resource2Position = resource2Position;
	}

	public final IResource getResource1() {
		return resource1;
	}

	public final void setResource1(final IResource resource1) {
		this.resource1 = resource1;
	}

	public final IResource getResource2() {
		return resource2;
	}

	public final void setResource2(final IResource resource2) {
		this.resource2 = resource2;
	}
}
