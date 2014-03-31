/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link MoveSnake} implements a snake-like move wherein {@link ISegment}s move between multiple {@link ISequence}s. E.g. a segment is moved from sequence A to sequence B, from sequence B to
 * sequence C and from sequence C to sequence A.
 * 
 * @author Simon Goodall
 * 
 */
public final class MoveSnake implements IMove {

	private List<IResource> fromResources;

	private List<IResource> toResources;

	private List<Integer> segmentStarts;

	private List<Integer> segmentEnds;

	private List<Integer> insertionPositions;

	@Override
	public void apply(final IModifiableSequences sequences) {

		final int numChanges = fromResources.size();

		final List<ISegment> segments = new ArrayList<ISegment>(numChanges);

		// Generate all the segments
		for (int i = 0; i < numChanges; ++i) {
			final IResource from = fromResources.get(i);
			final IModifiableSequence fromSequence = sequences.getModifiableSequence(from);
			final ISegment segment = fromSequence.getSegment(segmentStarts.get(i), segmentEnds.get(i));
			segments.add(i, segment);
		}

		// Add all segments their new sequences
		// Generate all the segments
		for (int i = 0; i < numChanges; ++i) {
			final IResource to = toResources.get(i);
			final IModifiableSequence toSequence = sequences.getModifiableSequence(to);
			toSequence.insert(insertionPositions.get(i), segments.get(i));
		}

		// Remove segments from old sequences
		for (int i = 0; i < numChanges; ++i) {
			final IResource from = fromResources.get(i);
			final IModifiableSequence fromSequence = sequences.getModifiableSequence(from);

			fromSequence.remove(segments.get(i));
		}
	}

	@Override
	public Collection<IResource> getAffectedResources() {

		final Set<IResource> affectedResources = new HashSet<IResource>();
		affectedResources.addAll(fromResources);
		affectedResources.addAll(toResources);
		return affectedResources;
	}

	@Override
	public boolean validate(final ISequences sequences) {

		if (fromResources == null) {
			return false;
		}

		if (toResources == null) {
			return false;
		}

		if (insertionPositions == null) {
			return false;
		}

		if (segmentStarts == null) {
			return false;
		}

		if (segmentEnds == null) {
			return false;
		}

		// Check unique froms
		final Set<IResource> froms = new HashSet<IResource>();
		for (final IResource from : fromResources) {
			if (froms.add(from) == false) {
				return false;
			}
		}

		// Check unique tos
		final Set<IResource> tos = new HashSet<IResource>();
		for (final IResource to : toResources) {
			if (tos.add(to) == false) {
				return false;
			}
		}

		final int numChanges = fromResources.size();

		for (int i = 0; i < numChanges; ++i) {
			if (segmentStarts.get(i) < 0) {
				return false;
			}

			if (segmentEnds.get(i) < 0) {
				return false;
			}

			if (insertionPositions.get(i) < 0) {
				return false;
			}

			if (segmentEnds.get(i) < segmentStarts.get(i)) {
				return false;
			}

			if (segmentEnds.get(i) > sequences.getSequence(i).size()) {
				return false;
			}

		}
		// TODO: Check for non-overlapping segments,
		// TODO: Check insertionPosition within range

		return true;
	}

	public List<IResource> getFromResources() {
		return fromResources;
	}

	public void setFromResources(final List<IResource> fromResources) {
		this.fromResources = fromResources;
	}

	public List<IResource> getToResources() {
		return toResources;
	}

	public void setToResources(final List<IResource> toResources) {
		this.toResources = toResources;
	}

	public List<Integer> getSegmentStarts() {
		return segmentStarts;
	}

	public void setSegmentStarts(final List<Integer> segmentStarts) {
		this.segmentStarts = segmentStarts;
	}

	public List<Integer> getSegmentEnds() {
		return segmentEnds;
	}

	public void setSegmentEnds(final List<Integer> segmentEnds) {
		this.segmentEnds = segmentEnds;
	}

	public List<Integer> getInsertionPositions() {
		return insertionPositions;
	}

	public void setInsertionPositions(final List<Integer> insertionPositions) {
		this.insertionPositions = insertionPositions;
	}

}
