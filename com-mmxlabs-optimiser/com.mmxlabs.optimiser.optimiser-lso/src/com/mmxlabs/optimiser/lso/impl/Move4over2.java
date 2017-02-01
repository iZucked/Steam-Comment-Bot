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
 * The {@link Move4over2} class swaps segments between two sequences.
 * 
 * @author Simon Goodall
 * 
 */
public final class Move4over2 implements IMove {
	@NonNull
	private final IResource resource1;

	@NonNull
	private final IResource resource2;

	private final int resource1Start;
	private final int resource1End;

	private final int resource2Start;
	private final int resource2End;

	public Move4over2(@NonNull final IResource resource1, final int resource1Start, final int resource1End, @NonNull final IResource resource2, final int resoure2Start, final int resource2End) {
		this.resource1 = resource1;
		this.resource1Start = resource1Start;
		this.resource1End = resource1End;
		this.resource2 = resource2;
		resource2Start = resoure2Start;
		this.resource2End = resource2End;
	}

	@Override
	public final void apply(@NonNull final IModifiableSequences sequences) {

		// Get sequences
		final IModifiableSequence sequence1 = sequences.getModifiableSequence(resource1);
		final IModifiableSequence sequence2 = sequences.getModifiableSequence(resource2);

		// Get the moving segments
		final ISegment segment1 = sequence1.getSegment(resource1Start, resource1End);
		final ISegment segment2 = sequence2.getSegment(resource2Start, resource2End);

		// Insert the segments in the other sequence
		sequence1.insert(resource1Start, segment2);
		sequence2.insert(resource2Start, segment1);

		// Remove segments from original sequences
		sequence1.remove(segment1);
		sequence2.remove(segment2);
	}

	@Override
	@NonNull
	public final Collection<@NonNull IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(resource1, resource2);
	}

	@Override
	public final boolean validate(@NonNull final ISequences sequences) {

		// Validate basic parameters
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

		// swapping empty sequences
		if (resource1Start == resource1End && resource2Start == resource2End) {
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
		if (resource2End > sequence2.size()) {
			return false;
		}

		return true;
	}

	public final IResource getResource1() {
		return resource1;
	}

	public final IResource getResource2() {
		return resource2;
	}

	public final int getResource1Start() {
		return resource1Start;
	}

	public final int getResource1End() {
		return resource1End;
	}

	public final int getResource2Start() {
		return resource2Start;
	}

	public final int getResource2End() {
		return resource2End;
	}

	@Override
	public final String toString() {
		return String.format("r1 (%s) [%d -> %d], r2 (%s) [%d -> %d]", getResource1(), getResource1Start(), getResource1End(), getResource2(), getResource2Start(), getResource2End());
	}
}
