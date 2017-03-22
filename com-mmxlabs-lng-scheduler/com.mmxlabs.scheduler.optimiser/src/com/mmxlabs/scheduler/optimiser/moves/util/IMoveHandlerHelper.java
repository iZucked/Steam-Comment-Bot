/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IMoveHandlerHelper {

	@NonNull
	List<ISequenceElement> extractSegment(@NonNull ISequence fromSequence, @NonNull ISequenceElement element);

	Triple<String, Pair<IResource, Integer>, Pair<IResource, Integer>> selectInitialPositions(List<Pair<ISequenceElement, ISequenceElement>> validBreaks, ILookupManager lookupManager, Random random);

	Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> findEdge(List<Pair<ISequenceElement, ISequenceElement>> validBreaks, ILookupManager lookupManager, Random random);

	boolean checkPositions(Pair<IResource, Integer> pos1, Pair<IResource, Integer> pos2);

	List<Pair<Integer, Integer>> determineViableSecondBreaks(IResource sequence1, int position1, int position2, ISequence seq2, boolean valid2opt2, ILookupManager lookupManager,
			IFollowersAndPreceders followersAndPreceders);
}