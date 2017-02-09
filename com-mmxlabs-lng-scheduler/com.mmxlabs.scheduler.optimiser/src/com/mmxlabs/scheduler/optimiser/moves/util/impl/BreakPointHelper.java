package com.mmxlabs.scheduler.optimiser.moves.util.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class BreakPointHelper implements IBreakPointHelper {

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs whose first element is the start of the edge and second the end.
	 */
	private final ArrayList<Pair<ISequenceElement, ISequenceElement>> validBreaks = new ArrayList<>();

	@Override
	public ArrayList<Pair<ISequenceElement, ISequenceElement>> getValidBreaks() {
		return validBreaks;
	}

	@Override
	@Inject
	public void init(final IOptimisationData data) {

		for (final ISequenceElement e1 : data.getSequenceElements()) {

			final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(e1);
			if (validFollowers.size() > 1) {
				for (final ISequenceElement e2 : validFollowers) {
					validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
				}
			}
		}
	}
}
