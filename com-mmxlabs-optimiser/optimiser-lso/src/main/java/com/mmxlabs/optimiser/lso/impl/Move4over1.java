package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link Move4over1} move type allows two non-overlapping segments within a
 * single {@link ISequence} to be swapped.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class Move4over1<T> implements IMove<T> {

	private IResource resource;

	private int segment1Start, segment1End;

	private int segment2Start, segment2End;

	@Override
	public void apply(final IModifiableSequences<T> sequences) {

		// Get sequences
		final IModifiableSequence<T> sequence = sequences
				.getModifiableSequence(resource);

		// Get the moving segments
		final ISegment<T> segment1 = sequence.getSegment(segment1Start,
				segment1End);
		final ISegment<T> segment2 = sequence.getSegment(segment2Start,
				segment2End);

		// Here we need to be careful, we assume each element can only exist in
		// the sequence once - assuming we are using the ListModifiableSequence,
		// the remove method will remove the first occurrence of the instance.

		// As segment 1 is before segment 2, we can use this to define the
		// correct order. We removing elements, we need to be careful to
		// preserve, or adjust accordingly the breakpoints.

		// Insert segment one into the route
		sequence.insert(segment2Start, segment1);
		// As remove might remove the first occurrence, we need to remove
		// segment2 before re-inserting it.
		sequence.remove(segment2);

		// Insert segment 2
		sequence.insert(segment1Start, segment2);

		// Finally we can remove segment 1
		sequence.remove(segment1);
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return Collections.singletonList(resource);
	}

	@Override
	public boolean validate(final ISequences<T> sequences) {

		// Validate basic parameters
		if (resource == null) {
			return false;
		}

		if (segment1Start < 0) {
			return false;
		}
		if (segment1End < segment1Start) {
			return false;
		}

		if (segment2Start < 0) {
			return false;
		}

		if (segment2End < segment2Start) {
			return false;
		}

		// Ensure segment 1 is before segment 2
		if (segment2Start < segment1End) {
			return false;
		}

		// Validate parameters against sequences object
		final Map<IResource, ISequence<T>> sequenceMap = sequences
				.getSequences();

		// Make sure resource exists
		if (sequenceMap.containsKey(resource) == false) {
			return false;
		}

		// Make sure end is within range. This implies start would also be
		// within range due to previous checks.
		final ISequence<T> sequence = sequenceMap.get(resource);
		if (segment1End > sequence.size()) {
			return false;
		}
		if (segment2End > sequence.size()) {
			return false;
		}

		return true;
	}

	public IResource getResource() {
		return resource;
	}

	public void setResource(final IResource resource) {
		this.resource = resource;
	}

	public int getSegment1Start() {
		return segment1Start;
	}

	public void setSegment1Start(final int segment1Start) {
		this.segment1Start = segment1Start;
	}

	public int getSegment1End() {
		return segment1End;
	}

	public void setSegment1End(final int segment1End) {
		this.segment1End = segment1End;
	}

	public int getSegment2Start() {
		return segment2Start;
	}

	public void setSegment2Start(final int segment2Start) {
		this.segment2Start = segment2Start;
	}

	public int getSegment2End() {
		return segment2End;
	}

	public void setSegment2End(final int segment2End) {
		this.segment2End = segment2End;
	}

	@Override
	public String toString() {
		return String.format("r1 (%s) [%d -> %d], [%d -> %d]", getResource(),
				getSegment1Start(), getSegment1End(), getSegment2Start(),
				getSegment2End());
	}
}
