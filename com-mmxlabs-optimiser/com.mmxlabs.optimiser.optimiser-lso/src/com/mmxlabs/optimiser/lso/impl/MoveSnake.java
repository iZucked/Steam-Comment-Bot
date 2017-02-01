/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * The {@link MoveSnake} implements a snake-like move wherein {@link ISegment}s move between multiple {@link ISequence}s. E.g. a segment is moved from sequence A to sequence B, from sequence B to
 * sequence C and from sequence C to sequence A.
 * 
 * @author Simon Goodall
 * 
 */
public final class MoveSnake implements IMove {

	private List<@NonNull IResource> fromResources;

	private List<@NonNull IResource> toResources;

	private List<@NonNull Integer> segmentStarts;

	private List<@NonNull Integer> segmentEnds;

	private List<@NonNull Integer> insertionPositions;

	@Override
	public void apply(@NonNull final IModifiableSequences sequences) {

		final int numChanges = fromResources.size();

		final List<@NonNull ISegment> segments = new ArrayList<>(numChanges);

		// Generate all the segments
		for (int i = 0; i < numChanges; ++i) {
			final IResource from = fromResources.get(i);
			assert from != null;
			final IModifiableSequence fromSequence = sequences.getModifiableSequence(from);
			final ISegment segment = fromSequence.getSegment(segmentStarts.get(i), segmentEnds.get(i));
			segments.add(i, segment);
		}

		// Add all segments their new sequences
		// Generate all the segments
		for (int i = 0; i < numChanges; ++i) {
			final IResource to = toResources.get(i);
			assert to != null;
			final IModifiableSequence toSequence = sequences.getModifiableSequence(to);
			final ISegment segment = segments.get(i);
			// assert segment != null;
			toSequence.insert(insertionPositions.get(i), segment);
		}

		// Remove segments from old sequences
		for (int i = 0; i < numChanges; ++i) {
			final IResource from = fromResources.get(i);
			assert from != null;
			final IModifiableSequence fromSequence = sequences.getModifiableSequence(from);

			final ISegment segment = segments.get(i);
			// assert segment != null;
			fromSequence.remove(segment);
		}
	}

	@Override
	@NonNull
	public Collection<@NonNull IResource> getAffectedResources() {

		final Set<@NonNull IResource> affectedResources = new HashSet<>();
		affectedResources.addAll(fromResources);
		affectedResources.addAll(toResources);
		return affectedResources;
	}

	@Override
	public boolean validate(@NonNull final ISequences sequences) {

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
		final Set<@NonNull IResource> froms = new HashSet<>();
		for (final IResource from : fromResources) {
			if (froms.add(from) == false) {
				return false;
			}
		}

		// Check unique tos
		final Set<@NonNull IResource> tos = new HashSet<>();
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

	public List<@NonNull IResource> getFromResources() {
		return fromResources;
	}

	public void setFromResources(final List<@NonNull IResource> fromResources) {
		this.fromResources = fromResources;
	}

	public List<@NonNull IResource> getToResources() {
		return toResources;
	}

	public void setToResources(final List<@NonNull IResource> toResources) {
		this.toResources = toResources;
	}

	public List<@NonNull Integer> getSegmentStarts() {
		return segmentStarts;
	}

	public void setSegmentStarts(final List<@NonNull Integer> segmentStarts) {
		this.segmentStarts = segmentStarts;
	}

	public List<@NonNull Integer> getSegmentEnds() {
		return segmentEnds;
	}

	public void setSegmentEnds(final List<@NonNull Integer> segmentEnds) {
		this.segmentEnds = segmentEnds;
	}

	public List<@NonNull Integer> getInsertionPositions() {
		return insertionPositions;
	}

	public void setInsertionPositions(final List<@NonNull Integer> insertionPositions) {
		this.insertionPositions = insertionPositions;
	}

}
