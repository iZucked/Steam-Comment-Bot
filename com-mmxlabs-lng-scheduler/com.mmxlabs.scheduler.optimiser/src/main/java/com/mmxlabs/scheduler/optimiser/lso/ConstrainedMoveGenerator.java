/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.Move2over2;
import com.mmxlabs.optimiser.lso.impl.Move3over2;
import com.mmxlabs.optimiser.lso.impl.Move4over2;
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
	private final Map<T, Followers<T>> validFollowers = new HashMap<T, Followers<T>>();

	/**
	 * A reverse lookup table from elements to positions
	 */
	private final Map<T, Pair<Integer, Integer>> reverseLookup = new HashMap<T, Pair<Integer, Integer>>();

	/**
	 * A reference to the current set of sequences, which will be used in
	 * generating moves
	 */
	private ISequences<T> sequences = null;

	private Random random;

	int breakableVertexCount = 0;

	int breakpointCount = 0;
	// private IOptimisationContext<T> context;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs
	 * whose first element is the start of the edge and second the end.
	 */
	final private ArrayList<Pair<T, T>> validBreaks = new ArrayList<Pair<T, T>>();
	
	final private class Followers<Q> implements Iterable<Q> {
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

	class Move2over2A extends Move2over2<T> {

	}

	class Move2over2B extends Move2over2<T> {

	}

	class Move2over2C extends Move2over2<T> {

	}

	class NullMove implements IMove<T> {

		@Override
		public Collection<IResource> getAffectedResources() {

			return Collections.emptyList();
		}

		@Override
		public void apply(final IModifiableSequences<T> sequences) {

		}

		@Override
		public boolean validate(final ISequences<T> sequences) {
			return true;
		}

	}

	class NullMoveA extends NullMove {
	}

	class NullMoveB extends NullMove {
	}

	class NullMoveC extends NullMove {
	}

	class NullMoveD extends NullMove {
	}

	class NullMoveE extends NullMove {
	}

	NullMove nullMoveA = new NullMoveA();
	NullMove nullMoveB = new NullMoveB();
	NullMove nullMoveC = new NullMoveC();
	NullMove nullMoveD = new NullMoveD();

	private final LegalSequencingChecker<T> checker;

	public ConstrainedMoveGenerator(final IOptimisationContext<T> context) {
		// this.context = context;
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
	}

	@Override
	public IMove<T> generateMove() {
		final Pair<T, T> newPair = RandomHelper.chooseElementFrom(random,
				validBreaks);
		final Pair<Integer, Integer> pos1 = reverseLookup.get(newPair
				.getFirst());
		final Pair<Integer, Integer> pos2 = reverseLookup.get(newPair
				.getSecond());
		final List<IResource> resources = sequences.getResources();

		final int sequence1 = pos1.getFirst();
		final int sequence2 = pos2.getFirst();
		final int position1 = pos1.getSecond();
		int position2 = pos2.getSecond();

		// are both these elements currently in the same route
		if (sequence1 == sequence2) {
			// we have found a segment which we can legally excise from a route;
			// now we must
			// choose somewhere to insert it.
			// the only two (currently implemented) options here are 3opt2 and
			// 4opt1
			// I think 3opt2 is worth looking for first, as more requirements =>
			// less feasible.

			final ISequence<T> sequence = sequences.getSequence(sequence1);
			final int beforeFirstCut = Math.min(position1, position2);
			final int beforeSecondCut = Math.max(position1, position2) - 1;
			final T firstElementInSegment = sequence.get(beforeFirstCut + 1);
			final T lastElementInSegment = sequence.get(beforeSecondCut);

			final Followers<T> followers = validFollowers
					.get(lastElementInSegment);

			// pick a follower and do a reverse-lookup
			final T precursor = followers.get(random.nextInt(followers.size()));
			final Pair<Integer, Integer> posPrecursor = reverseLookup
					.get(precursor);

			// now check whether the element before the precursor can precede
			// the first element in the segment
			final T beforeInsert = sequences.getSequence(
					posPrecursor.getFirst()).get(posPrecursor.getSecond());
			if (validFollowers.get(beforeInsert)
					.contains(firstElementInSegment)) {
				// we have a legal 3opt2, so do that. It might be a 3opt1
				// really, but that's OK
				// so long as we don't insert a segment into itself.
				if (posPrecursor.getFirst().equals(sequence1)) {
					// check for stupidity
					final int position3 = posPrecursor.getSecond();
					if (position3 >= beforeFirstCut
							&& position3 <= beforeSecondCut) {
						return null; // stupidity has happened.
					}
				}

				final Move3over2<T> result = new Move3over2<T>();
				result.setResource1(resources.get(sequence1));
				result.setResource1Start(beforeFirstCut + 1);
				result.setResource1End(beforeSecondCut);

				result.setResource2(resources.get(posPrecursor.getFirst()));
				result.setResource2Position(posPrecursor.getSecond() + 1);
				return result;
			} else {
				// we chose a bad place to insert ; the segment will not fit
				// TODO could stick this in a loop and try a few times before
				// bailing out
				// maybe search for a 4opt1 in here? but will a 4opt1 work if a
				// 3opt1 won't? probably not!
				return null;
			}
		} else {
			// we have found a potentially valid situation for an opt2 move of
			// some sort
			// what we can do here is move forward from pos1 and pos2 until we
			// find another legal
			// conjunction and then construct a suitable move

			// check if it'd be a legal 2opt2

			final ISequence<T> seq1 = sequences.getSequence(sequence1);
			final ISequence<T> seq2 = sequences.getSequence(sequence2);

			boolean valid2opt2 = validFollowers.get(seq2.get(position2 - 1))
					.contains(seq1.get(position1 + 1));

			while (!valid2opt2 && position2 > 1) {
				// rewind position 2? after all if we don't have a valid 2opt2
				// we probably won't get a valid 4opt2 out of it either?
				position2--;
				valid2opt2 = validFollowers.get(seq2.get(position2 - 1))
						.contains(seq1.get(position1 + 1));
			}

			// if it would be, maybe do it
			if (valid2opt2 && random.nextBoolean()) {
				// make 2opt2
				final Move2over2<T> result = new Move2over2A();
				result.setResource1(resources.get(sequence1));
				result.setResource2(resources.get(sequence2));
				// add 1 because the positions are inclusive, and we need to cut
				// after the first element
				result.setResource1Position(position1 + 1);
				result.setResource2Position(position2);
				return result;
			} else {
				/*
				 * We have this situation
				 * 
				 * 0----------A--------------0 S1 \ <- the possible break we
				 * have found 0------------B---------------0 S2
				 * 
				 * we want to iterate over the elements following B and see if
				 * any of them can precede anything in S1 after or including A.
				 */

				final Followers<T> followersOfSecondElementsPredecessor = validFollowers
						.get(seq2.get(position2 - 1));

				final List<Pair<Integer, Integer>> viableSecondBreaks = new ArrayList<Pair<Integer, Integer>>();
				for (int i = position2 + 1; i < seq2.size() - 1; i++) { // ignore
																		// last
																		// element
					final T here = seq2.get(i);
					for (final T elt : validFollowers.get(here)) {
						final Pair<Integer, Integer> loc = reverseLookup
								.get(elt);
						if (loc.getFirst().intValue() == sequence1) {
							// it can be adjacent to something in sequence 1,
							// that's good
							if (loc.getSecond() > position1) {
								// it's something after A, that's even better!
								// now we need to check that we can put the
								// chunk cut out of S1 into S2 here

								if (loc.getSecond() == position1 + 1) {
									// 3opt1 check
									if (followersOfSecondElementsPredecessor
											.contains(seq2.get(i + 1)))
										viableSecondBreaks
												.add(new Pair<Integer, Integer>(
														i, loc.getSecond()));
								} else {
									// 4opt2 check
									if (valid2opt2
											&& validFollowers
													.get(sequences
															.getSequence(
																	loc.getFirst())
															.get(loc.getSecond() - 1))
													.contains(seq2.get(i + 1)))
										viableSecondBreaks
												.add(new Pair<Integer, Integer>(
														i, loc.getSecond()));
								}

							}
						}
					}
				}
				// So, we have collected some possible breaks, pick one
				// TODO it might be worth caching this as a source of possible
				// moves to allow
				// quick generation on subsequent calls, if this move is
				// rejected

				if (viableSecondBreaks.isEmpty()) {
					if (valid2opt2) {
						final Move2over2<T> result = new Move2over2B();
						result.setResource1(resources.get(sequence1));
						result.setResource2(resources.get(sequence2));
						// add 1 because the positions are inclusive, and we
						// need to cut after the first element
						result.setResource1Position(position1 + 1);
						result.setResource2Position(position2);
						return result;
					} else {
						// System.err.println("No valid 2opt2");
						//
						// System.err.println("Disallowed by:"
						// +checker.getSequencingProblems(
						// seq2.get(position2-1), seq1.get(position1+1),
						// resources.get(sequence2)));
						//
						return null;
					}
				}

				final Pair<Integer, Integer> selectedSecondBreak = RandomHelper
						.chooseElementFrom(random, viableSecondBreaks);
				// so now we have two breaks, which either means a 4opt2 or a
				// 3opt2, so we just have to decode these and see.
				// second element of the pair is in sequence1, first is in
				// sequence2.

				final int secondPosition1 = selectedSecondBreak.getSecond();
				final int secondPosition2 = selectedSecondBreak.getFirst();

				if (secondPosition1 == position1 + 1) {
					// 3opt2
					final Move3over2<T> result = new Move3over2<T>();

					result.setResource2(resources.get(sequence1));
					result.setResource2Position(position1 + 1);

					result.setResource1(resources.get(sequence2));
					result.setResource1Start(position2); // inclusive
					result.setResource1End(secondPosition2 + 1); // exclusive

					return result;
				} else {
					// 4opt2
					final Move4over2<T> result = new Move4over2<T>();

					result.setResource1(resources.get(sequence1));
					result.setResource2(resources.get(sequence2));

					result.setResource1Start(position1 + 1);
					result.setResource1End(secondPosition1);

					result.setResource2Start(position2);
					result.setResource2End(secondPosition2 + 1);

					return result;
				}
			}
		}
	}

	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;

		/*
		 * TODO profile this horrible thing
		 */
		for (int i = 0; i < sequences.size(); i++) {
			final ISequence<T> sequence = sequences.getSequence(i);
			for (int j = 0; j < sequence.size(); j++) {
				reverseLookup.get(sequence.get(j)).setBoth(i, j);
			}
		}
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