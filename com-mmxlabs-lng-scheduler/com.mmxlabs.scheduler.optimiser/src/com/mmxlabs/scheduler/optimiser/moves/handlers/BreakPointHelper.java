package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

public class BreakPointHelper implements IBreakPointHelper {

//	@Inject
//	private IPortTypeProvider portTypeProvider;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

//	private int breakableVertexCount = 0;
//
//	private final int breakpointCount = 0;
//	// private IOptimisationContext context;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs whose first element is the start of the edge and second the end.
	 */
	private final ArrayList<Pair<ISequenceElement, ISequenceElement>> validBreaks = new ArrayList<>();

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.moves.handlers.IBreakPointHelper#getValidBreaks()
	 */
	@Override
	public ArrayList<Pair<ISequenceElement, ISequenceElement>> getValidBreaks() {
		return validBreaks;
	}
	
	@Inject
	private LegalSequencingChecker checker;
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.moves.handlers.IBreakPointHelper#init(com.mmxlabs.optimiser.core.scenario.IOptimisationData)
	 */
	@Override
	@Inject
	public void init(final IOptimisationData data) {
		// this.checker = injector.getInstance(LegalSequencingChecker.class);
				// LegalSequencingChecker checker2 = new LegalSequencingChecker(context);
		int initialMaxLateness = checker.getMaxLateness();
		checker.disallowLateness();
		
		// create a massive lookup table, caching all legal sequencing decisions
		// this might be a terrible idea, we could just keep the checker instead
		// also need to fix the resource binding now
		for (final ISequenceElement e1 : data.getSequenceElements()) {
//			if (!portTypeProvider.getPortType(e1).equals(PortType.End)) {
//				breakableVertexCount++;
//			}

			final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(e1);
			if (validFollowers.size() > 1) {
				for (final ISequenceElement e2 : validFollowers) {
					validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
				}
			}
		}
		
		checker.setMaxLateness(initialMaxLateness);

	}
}
