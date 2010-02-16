package com.acme.optimiser.lso.impl;

import java.util.Collection;
import java.util.Map;

import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.IModifiableSequences;
import com.acme.optimiser.IResource;
import com.acme.optimiser.ISegment;
import com.acme.optimiser.ISequence;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.lso.IMove;
import com.acme.optimiser.lso.impl.internal.MoveUtil;

/**
 * The {@link Move4opt2} class swaps segments between two sequences.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class Move4opt2<T> implements IMove<T> {

	private IResource resource1;

	private IResource resource2;

	private int resource1Start, resource1End;

	private int resource2Start, resource2End;

	@Override
	public void apply(final IModifiableSequences<T> sequences) {

		// Get sequences
		final IModifiableSequence<T> sequence1 = sequences
				.getModifiableSequence(resource1);
		final IModifiableSequence<T> sequence2 = sequences
				.getModifiableSequence(resource2);

		// Get the moving segments
		final ISegment<T> segment1 = sequence1.getSegment(resource1Start,
				resource1End);
		final ISegment<T> segment2 = sequence2.getSegment(resource2Start,
				resource2End);

		// Insert the segments in the other sequence
		sequence1.insert(resource2Start, segment2);
		sequence2.insert(resource1Start, segment1);

		// Remove segments from original sequences
		sequence1.remove(segment1);
		sequence2.remove(segment2);
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return MoveUtil.makeArrayList(resource1, resource2);
	}

	@Override
	public boolean validate(final ISequences<T> sequences) {

		// Validate basic parameters
		if (resource1 == null) {
			return false;
		}

		if (resource2 == null) {
			return false;
		}

		if (resource1Start < 0) {
			return false;
		}
		if (resource1End < resource1Start) {
			return false;
		}

		if (resource2Start < 0) {
			return false;
		}
		if (resource2End < resource2Start) {
			return false;
		}

		// Validate parameters against sequences object
		final Map<IResource, ISequence<T>> sequenceMap = sequences
				.getSequences();

		// Make sure resources exist
		if (sequenceMap.containsKey(resource1) == false) {
			return false;
		}
		if (sequenceMap.containsKey(resource2) == false) {
			return false;
		}

		// Make sure end is within range. This implies start would also be
		// within range due to previous checks.
		final ISequence<T> sequence1 = sequenceMap.get(resource1);
		if (resource1End >= sequence1.size()) {
			return false;
		}

		final ISequence<T> sequence2 = sequenceMap.get(resource2);
		if (resource2End >= sequence2.size()) {
			return false;
		}

		return true;
	}

	public IResource getResource1() {
		return resource1;
	}

	public void setResource1(final IResource resource1) {
		this.resource1 = resource1;
	}

	public IResource getResource2() {
		return resource2;
	}

	public void setResource2(final IResource resource2) {
		this.resource2 = resource2;
	}

	public int getResource1Start() {
		return resource1Start;
	}

	public void setResource1Start(final int resource1Start) {
		this.resource1Start = resource1Start;
	}

	public int getResource1End() {
		return resource1End;
	}

	public void setResource1End(final int resource1End) {
		this.resource1End = resource1End;
	}

	public int getResource2Start() {
		return resource2Start;
	}

	public void setResource2Start(final int resource2Start) {
		this.resource2Start = resource2Start;
	}

	public int getResource2End() {
		return resource2End;
	}

	public void setResource2End(final int resource2End) {
		this.resource2End = resource2End;
	}
}
