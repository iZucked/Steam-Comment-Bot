/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * <p>
 * A move generator which tries to create moves that respect timed constraints, to avoid wasting too many cycles testing clearly impossible moves. Suggested in ticket #9.
 * </p>
 * <ol>
 * The moves generated should try to respect the following aspects, where possible.
 * <li>Time Windows + Voyage Durations</li>
 * <li>Port visit durations</li>
 * <li>Port restrictions</li>
 * <li>Load / Discharge ordering</li>
 * <li>Fixed load/discharge pairs</li>
 * <li>Vessel events</li>
 * <li>Vessel or class assignments</li>
 * </ol>
 * 
 * @author hinton
 * 
 * @param
 */
public class ConstrainedMoveGenerator implements IMoveGenerator {
	/**
	 * The proportion of moves which are related to optional elements
	 * 
	 * TODO make this a parameter.
	 */
	private static final double optionalMoveFrequency = 0.10;
	private static final double shuffleMoveFrequency = 0.20;

	/**
	 * A structure caching the output of the {@link LegalSequencingChecker}. If an element x is in the set mapped to by key y, x can legally follow y under some circumstance
	 */
	protected final Map<ISequenceElement, Followers<ISequenceElement>> validFollowers = new HashMap<ISequenceElement, Followers<ISequenceElement>>();
	protected final Map<ISequenceElement, Followers<ISequenceElement>> validPreceeders = new HashMap<ISequenceElement, Followers<ISequenceElement>>();

	/**
	 * A reverse lookup table from elements to positions
	 */
	protected final Map<ISequenceElement, Pair<Integer, Integer>> reverseLookup = new HashMap<ISequenceElement, Pair<Integer, Integer>>();

	/**
	 * A reference to the current set of sequences, which will be used in generating moves
	 */
	protected ISequences sequences = null;

	protected Random random;

	int breakableVertexCount = 0;

	int breakpointCount = 0;
	// private IOptimisationContext context;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs whose first element is the start of the edge and second the end.
	 */
	final protected ArrayList<Pair<ISequenceElement, ISequenceElement>> validBreaks = new ArrayList<Pair<ISequenceElement, ISequenceElement>>();

	final protected class Followers<Q> implements Iterable<Q> {
		/**
		 * @param followers
		 */
		public Followers(final Collection<Q> followers) {
			backingList.addAll(followers);
			containsSet.addAll(followers);
		}

		private final List<Q> backingList = new ArrayList<Q>();
		private final Set<Q> containsSet = new HashSet<Q>();

		/**
		 * @return
		 */
		public int size() {
			return backingList.size();
		}

		/**
		 * @param nextInt
		 * @return
		 */
		public Q get(final int nextInt) {
			return backingList.get(nextInt);
		}

		/**
		 * @param firstElementInSegment
		 * @return
		 */
		public boolean contains(final Q firstElementInSegment) {
			return containsSet.contains(firstElementInSegment);
		}

		@Override
		public Iterator<Q> iterator() {
			return backingList.iterator();
		}
	}

	private final LegalSequencingChecker checker;

	private final SequencesConstrainedMoveGeneratorUnit sequencesMoveGenerator;
	private final OptionalConstrainedMoveGeneratorUnit optionalMoveGenerator;
	private final ShuffleElementsMoveGenerator shuffleMoveGenerator;

	protected final IOptimisationContext context;

	public ConstrainedMoveGenerator(final IOptimisationContext context) {
		this.context = context;
		this.checker = new LegalSequencingChecker(context);
		checker.disallowLateness();
		final IOptimisationData data = context.getOptimisationData();

		final IPortTypeProvider portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);

		// create a massive lookup table, caching all legal sequencing decisions
		// this might be a terrible idea, we could just keep the checker instead
		// also need to fix the resource binding now
		for (final ISequenceElement e1 : data.getSequenceElements()) {
			if (!portTypeProvider.getPortType(e1).equals(PortType.End)) {
				breakableVertexCount++;
			}

			reverseLookup.put(e1, new Pair<Integer, Integer>(0, 0));

			final LinkedHashSet<ISequenceElement> followers = new LinkedHashSet<ISequenceElement>();
			final LinkedHashSet<ISequenceElement> preceeders = new LinkedHashSet<ISequenceElement>();

			for (final ISequenceElement e2 : data.getSequenceElements()) {
				if (e1 == e2) {
					continue;
				}

				if (checker.allowSequence(e1, e2)) {

					if (followers.size() == 1) {
						validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, followers.iterator().next()));
					}
					followers.add(e2);
					if (followers.size() > 1) {
						validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
					}
				}

				if (checker.allowSequence(e2, e1)) {
					preceeders.add(e2);
				}
			}

			validFollowers.put(e1, new Followers<ISequenceElement>(followers));
			validPreceeders.put(e1, new Followers<ISequenceElement>(preceeders));
		}

		this.sequencesMoveGenerator = new SequencesConstrainedMoveGeneratorUnit(this);
		this.shuffleMoveGenerator = new ShuffleElementsMoveGenerator(this);
		if (context.getOptimisationData().getDataComponentProviders().contains(SchedulerConstants.DCP_optionalElementsProvider)) {
			final IOptionalElementsProvider optionalElementsProvider = context.getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_optionalElementsProvider,
					IOptionalElementsProvider.class);
			if (optionalElementsProvider.getOptionalElements().size() > 0) {
				this.optionalMoveGenerator = new OptionalConstrainedMoveGeneratorUnit(this);
			} else {
				this.optionalMoveGenerator = null;
			}
		} else {
			this.optionalMoveGenerator = null;
		}
	}

	@Override
	public IMove generateMove() {
		if ((optionalMoveGenerator != null) && (random.nextDouble() < optionalMoveFrequency)) {
			return optionalMoveGenerator.generateMove();
		} else if ((shuffleMoveGenerator != null) && (random.nextDouble() < shuffleMoveFrequency)) {
			return shuffleMoveGenerator.generateMove();
		} else {
			return sequencesMoveGenerator.generateMove();
		}
	}

	@Override
	public ISequences getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences sequences) {
		this.sequences = sequences;

		// build table for elements in conventional sequences
		for (int i = 0; i < sequences.size(); i++) {
			final ISequence sequence = sequences.getSequence(i);
			for (int j = 0; j < sequence.size(); j++) {
				reverseLookup.get(sequence.get(j)).setBoth(i, j);
			}
		}

		// build table for excluded elements
		int x = 0;
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			reverseLookup.get(element).setBoth(null, x);
			x++;
		}

		sequencesMoveGenerator.setSequences(sequences);
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(final Random random) {
		this.random = random;
	}

	public void init() {
		if (random == null) {
			throw new IllegalStateException("Random number generator has not been set");
		}
	}
}