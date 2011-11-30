/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * <p>
 * A move generator which tries to create moves that respect timed constraints,
 * to avoid wasting too many cycles testing clearly impossible moves. Suggested
 * in ticket #9.
 * </p>
 * <ol>
 * The moves generated should try to respect the following aspects, where
 * possible.
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
 * @param <T>
 */
public class ConstrainedMoveGenerator<T> implements IMoveGenerator<T> {
	/**
	 * A structure caching the output of the {@link LegalSequencingChecker}. If
	 * an element x is in the set mapped to by key y, x can legally follow y
	 * under some circumstance
	 */
	protected final Map<T, Followers<T>> validFollowers = new HashMap<T, Followers<T>>();

	/**
	 * A reverse lookup table from elements to positions
	 */
	protected final Map<T, Pair<Integer, Integer>> reverseLookup = new HashMap<T, Pair<Integer, Integer>>();

	/**
	 * A reference to the current set of sequences, which will be used in
	 * generating moves
	 */
	protected ISequences<T> sequences = null;

	protected Random random;

	int breakableVertexCount = 0;

	int breakpointCount = 0;
	// private IOptimisationContext<T> context;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs
	 * whose first element is the start of the edge and second the end.
	 */
	final protected ArrayList<Pair<T, T>> validBreaks = new ArrayList<Pair<T, T>>();
	
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
		public Q get(int nextInt) {
			return backingList.get(nextInt);
		}
		/**
		 * @param firstElementInSegment
		 * @return
		 */
		public boolean contains(Q firstElementInSegment) {
			return containsSet.contains(firstElementInSegment);
		}

		@Override
		public Iterator<Q> iterator() {
			return backingList.iterator();
		}
	}



	private final LegalSequencingChecker<T> checker;

	private final SequencesConstrainedMoveGeneratorUnit<T> sequencesMoveGenerator;

	protected final IOptimisationContext<T> context;

	public ConstrainedMoveGenerator(final IOptimisationContext<T> context) {
		this.context = context;
		this.checker = new LegalSequencingChecker<T>(context);
		checker.disallowLateness();
		final IOptimisationData<T> data = context.getOptimisationData();

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<T> portTypeProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portTypeProvider,
						IPortTypeProvider.class);

		// create a massive lookup table, caching all legal sequencing decisions
		// this might be a terrible idea, we could just keep the checker instead
		// also need to fix the resource binding now
		for (final T e1 : data.getSequenceElements()) {
			if (!portTypeProvider.getPortType(e1).equals(PortType.End)) {
				breakableVertexCount++;
			}

			reverseLookup.put(e1, new Pair<Integer, Integer>(0, 0));

			final LinkedHashSet<T> followers = new LinkedHashSet<T>();

			for (final T e2 : data.getSequenceElements()) {
				if (e1 == e2)
					continue;
				if (checker.allowSequence(e1, e2)) {
					if (followers.size() == 1) {
						validBreaks.add(new Pair<T, T>(e1, followers.iterator()
								.next()));
					}
					followers.add(e2);
					if (followers.size() > 1) {
						validBreaks.add(new Pair<T, T>(e1, e2));
					}
				}
			}
			
			validFollowers.put(e1, new Followers<T>(followers));
		}

		this.sequencesMoveGenerator = new SequencesConstrainedMoveGeneratorUnit<T>(this);
	}
	
	@Override
	public IMove<T> generateMove() {
		return sequencesMoveGenerator.generateMove();
	}

	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;

		for (int i = 0; i < sequences.size(); i++) {
			final ISequence<T> sequence = sequences.getSequence(i);
			for (int j = 0; j < sequence.size(); j++) {
				reverseLookup.get(sequence.get(j)).setBoth(i, j);
			}
		}
		int x = 0;
		for (final T element : sequences.getUnusedElements()) {
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
			throw new IllegalStateException(
					"Random number generator has not been set");
		}
	}
}