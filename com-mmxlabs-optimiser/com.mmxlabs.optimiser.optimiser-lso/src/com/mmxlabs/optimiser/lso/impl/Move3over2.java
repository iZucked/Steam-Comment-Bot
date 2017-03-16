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
 * The {@link Move3over2} move type moves a segment from one {@link ISequence} into another {@link ISequence}.
 * 
 * @author Simon Goodall
 * 
 */
public class Move3over2 implements IMove {

	private IResource resource1;

	private IResource resource2;

	private int resource1Start = -1;

	private int resource1End = -1;

	private int resource2Position = -1;

	@Override
	public final void apply(@NonNull final IModifiableSequences sequences) {

		// Get sequences
		final IModifiableSequence sequence1 = sequences.getModifiableSequence(resource1);
		final IModifiableSequence sequence2 = sequences.getModifiableSequence(resource2);

		// Get the moving segments
		final ISegment segment1 = sequence1.getSegment(resource1Start, resource1End);
		if (resource1 == resource2) {
			if (resource2Position < resource1Start) {
				// insert earlier in sequence, remove then add
				sequence1.remove(segment1);
				sequence2.insert(resource2Position, segment1);
			} else {
				// insert later in sequence, insert then remove
				sequence2.insert(resource2Position, segment1);
				sequence1.remove(segment1);

			}
		} else {

			// Insert the segments in the other sequence
			sequence2.insert(resource2Position, segment1);

			// Remove segments from original sequences
			sequence1.remove(segment1);
		}
	}

	@Override
	@NonNull
	public final Collection<@NonNull IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(resource1, resource2);
	}

	@Override
	public final boolean validate(@NonNull final ISequences sequences) {

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

		if (resource2Position < 0) {
			return false;
		}

		// insert sequence will be of 0 length
		if (resource1Start == resource1End) {
			return false;
		}

		// Validate parameters against sequences object
		final Map<IResource, ISequence> sequenceMap = sequences.getSequences();

		// Make sure resources exist
		if (sequenceMap.containsKey(resource1) == false) {
			return false;
		}
		if (sequenceMap.containsKey(resource2) == false) {
			return false;
		}

		// Make sure end is within range. This implies start would also be
		// within range due to previous checks.
		final ISequence sequence1 = sequenceMap.get(resource1);
		if (resource1End > sequence1.size()) {
			return false;
		}

		final ISequence sequence2 = sequenceMap.get(resource2);
		if (resource2Position > sequence2.size()) {
			return false;
		}

		return true;
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

	public final int getResource1Start() {
		return resource1Start;
	}

	public final void setResource1Start(final int resource1Start) {
		this.resource1Start = resource1Start;
	}

	public final int getResource1End() {
		return resource1End;
	}

	public final void setResource1End(final int resource1End) {
		this.resource1End = resource1End;
	}

	public final int getResource2Position() {
		return resource2Position;
	}

	/**
	 * Set the position at which the segment from resource 1's sequence will be inserted into resource 2's sequence. The insert is inclusive, so to insert a segment between elements A and B, pass the
	 * position of B into this method.
	 * 
	 * @param resource2Position
	 */
	public final void setResource2Position(final int resource2Position) {
		this.resource2Position = resource2Position;
	}

	@Override
	public final String toString() {
		return String.format("r1 (%s) [%d -> %d], r2 (%s) [%d]", getResource1(), getResource1Start(), getResource1End(), getResource2(), getResource2Position());
	}
}
