/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move2over2;
import com.mmxlabs.optimiser.lso.impl.Move3over2;
import com.mmxlabs.optimiser.lso.impl.Move4over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.optimiser.lso.impl.NullMove2Over2;
import com.mmxlabs.optimiser.lso.impl.NullMove3Over2;
import com.mmxlabs.optimiser.lso.impl.NullMove4Over2;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * Refactoring of the sequences-related CMG logic into a helper class.
 * 
 * This uses protected access to a lot of {@link ConstrainedMoveGenerator} fields, which isn't great, but if needs be they could all be made accessors. The code is very tightly connected anyway, so it
 * doesn't make that much difference.
 * 
 * @author hinton
 * 
 */
public class SequencesConstrainedMoveGeneratorUnit implements IConstrainedMoveGeneratorUnit {
	/**
	 * This setting is an injectable from the OptimiserSettingsModule, to speed up non-client optimisation
	 */
	public final static String OPTIMISER_ENABLE_FOUR_OPT_2 = "enableOptimiser4Opt2";

	private int FOUR_OPT2_MOVES;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Inject
	public void setFourOpt2Fix(@Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2) boolean ENABLE_4_OPT_2_FIX) {
		if (ENABLE_4_OPT_2_FIX) {
			FOUR_OPT2_MOVES = 0; // 0 = 4 opt 2, 1 = no 4 opt 2
		} else {
			FOUR_OPT2_MOVES = 1;
		}
		// System.out.println("FOUR_OPT2_MOVES:"+FOUR_OPT2_MOVES);
	}

	final protected ConstrainedMoveGenerator owner;

	class Move2over2A extends Move2over2 {

	}

	class Move2over2B extends Move2over2 {

	}

	class Move2over2C extends Move2over2 {

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

	public SequencesConstrainedMoveGeneratorUnit(@NonNull final ConstrainedMoveGenerator owner) {
		super();
		this.owner = owner;

	}

	@Override
	public void setSequences(final ISequences sequences) {

	}
	

	public Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> findEdge(){
		
		
		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;
		
		
		final Pair<ISequenceElement, ISequenceElement> newPair = RandomHelper.chooseElementFrom(owner.random, owner.getValidBreaks());
				pos1 = owner.reverseLookup.get(newPair.getFirst());
				pos2 = owner.reverseLookup.get(newPair.getSecond());
	
		
		Pair<Pair<IResource, Integer>,Pair<IResource, Integer>> positions = new Pair(pos1,pos2);
		return positions;
	}
	

	@Override
	public IMove generateMove() {
		if (owner.getValidBreaks().isEmpty()) {
			return new NullMoveA();
		}
		
		// get resources for a random edge
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> positions = findEdge();
		
		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;
		
		// if no resource found then we have a part of the edge in the unused list
		// exit! (let another move solve this)
		if (positions != null) {
			pos1 = positions.getFirst();
			pos2 = positions.getSecond();
			if ((pos1.getFirst() == null) || (pos2.getFirst() == null)) {
				return new NullMoveB();
			}
		} else {
			return new NullMoveB();
		}
		
		final IResource sequence1 = pos1.getFirst();
		final IResource sequence2 = pos2.getFirst();
		final int position1 = pos1.getSecond();
		int position2 = pos2.getSecond();

		if (position1 == -1 || position2 == -1) {
			return new NullMoveC();
		}

		// are both these elements currently in the same route
		if (sequence1 == sequence2) {
			// we have found a segment which we can legally excise from a route;
			// now we must
			// choose somewhere to insert it.
			// the only two (currently implemented) options here are 3opt2 and
			// 4opt1
			// I think 3opt2 is worth looking for first, as more requirements =>
			// less feasible.

			final ISequence sequence = owner.sequences.getSequence(sequence1);
			final int beforeFirstCut = Math.min(position1, position2);
			final int beforeSecondCut = Math.max(position1, position2) - 1;
			// Zero length segment
			if (beforeFirstCut == beforeSecondCut) {
				return new NullMove3Over2();
			}

			final ISequenceElement firstElementInSegment = sequence.get(beforeFirstCut + 1);
			final ISequenceElement lastElementInSegment = sequence.get(beforeSecondCut);

			// Collect the elements which can go after the segment we are cutting out
			final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(lastElementInSegment);

			// Pick one of these followers and find where it is at the moment
			if (followers.size() == 0) {
				return null;
			}
			final ISequenceElement precursor = followers.get(owner.random.nextInt(followers.size()));
			final Pair<IResource, Integer> posPrecursor = owner.reverseLookup.get(precursor);

			// now check whether the element before the precursor can precede
			// the first element in the segment
			final IResource first = posPrecursor.getFirst();
			if (first == null) {
				return new NullMove3Over2();
			}
			final ISequenceElement beforeInsert = owner.sequences.getSequence(first).get(posPrecursor.getSecond() - 1);
			if (followersAndPreceders.getValidFollowers(beforeInsert).contains(firstElementInSegment)) {
				// we have a legal 3opt2, so do that. It might be a 3opt1
				// really, but that's OK
				// so long as we don't insert a segment into itself.
				if (first.equals(sequence1)) {
					// check for stupidity
					final int position3 = posPrecursor.getSecond();
					if ((position3 >= beforeFirstCut) && (position3 <= beforeSecondCut)) {
						return new NullMove3Over2(); // stupidity has happened.
					}
				}

				final Move3over2 result = new Move3over2();
				result.setResource1(sequence1);
				result.setResource1Start(beforeFirstCut + 1);
				result.setResource1End(beforeSecondCut);

				result.setResource2(first);
				result.setResource2Position(posPrecursor.getSecond() + 1);
				return result;
			} else {
				// we chose a bad place to insert ; the segment will not fit
				// TODO could stick this in a loop and try a few times before
				// bailing out
				// maybe search for a 4opt1 in here? but will a 4opt1 work if a
				// 3opt1 won't? probably not!
				return new NullMove3Over2();
			}
		} else {
			// we have found a potentially valid situation for an opt2 move of
			// some sort
			// what we can do here is move forward from pos1 and pos2 until we
			// find another legal
			// conjunction and then construct a suitable move

			// check if it'd be a legal 2opt2

			final ISequence seq1 = owner.sequences.getSequence(sequence1);
			final ISequence seq2 = owner.sequences.getSequence(sequence2);

			boolean valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));

			while (!valid2opt2 && (position2 > 1)) {
				// rewind position 2? after all if we don't have a valid 2opt2
				// we probably won't get a valid 4opt2 out of it either?
				position2--;
				valid2opt2 = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1)).contains(seq1.get(position1 + 1));
			}

			// if it would be, maybe do it
			if (valid2opt2 && (owner.random.nextDouble() < 0.05)) {
				// make 2opt2
				final Move2over2 result = new Move2over2A();
				result.setResource1(sequence1);
				result.setResource2(sequence2);
				// add 1 because the positions are inclusive, and we need to cut
				// after the first element
				result.setResource1Position(position1 + 1);
				result.setResource2Position(position2);
				return result;
			} else if (position2 > 0) {
				/*
				 * We have this situation
				 * 
				 * 0----------A--------------0 S1 \ <- the possible break we have found 0------------B---------------0 S2
				 * 
				 * we want to iterate over the elements following B and see if any of them can precede anything in S1 after or including A.
				 */

				final Followers<ISequenceElement> followersOfSecondElementsPredecessor = followersAndPreceders.getValidFollowers(seq2.get(position2 - 1));

				final List<Pair<Integer, Integer>> viableSecondBreaks = new ArrayList<Pair<Integer, Integer>>();
				for (int i = position2 + FOUR_OPT2_MOVES; i < (seq2.size() - 1); i++) { // ignore last element
					final ISequenceElement here = seq2.get(i);
					for (final ISequenceElement elt : followersAndPreceders.getValidFollowers(here)) {
						final Pair<IResource, Integer> loc = owner.reverseLookup.get(elt);
						final IResource first = loc.getFirst();
						if (first == null) {
							// Most likely an optional element no longer in sequences
							continue;
						}
						if (first == sequence1) {
							// it can be adjacent to something in sequence 1,
							// that's good
							if (loc.getSecond() > position1) {
								// it's something after A, that's even better!
								// now we need to check that we can put the
								// chunk cut out of S1 into S2 here

								if (loc.getSecond() == (position1 + 1)) {
									// 3opt1 check
									if (followersOfSecondElementsPredecessor.contains(seq2.get(i + 1))) {
										viableSecondBreaks.add(new Pair<Integer, Integer>(i, loc.getSecond()));
									}
								} else {
									// 4opt2 check
									if (valid2opt2 && followersAndPreceders.getValidFollowers(owner.sequences.getSequence(first).get(loc.getSecond() - 1)).contains(seq2.get(i + 1))) {
										viableSecondBreaks.add(new Pair<Integer, Integer>(i, loc.getSecond()));
									}
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
						final Move2over2 result = new Move2over2B();
						result.setResource1(sequence1);
						result.setResource2(sequence2);
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
						return new NullMove2Over2();
					}
				}

				final Pair<Integer, Integer> selectedSecondBreak = RandomHelper.chooseElementFrom(owner.random, viableSecondBreaks);
				// so now we have two breaks, which either means a 4opt2 or a
				// 3opt2, so we just have to decode these and see.
				// second element of the pair is in sequence1, first is in
				// sequence2.

				final int secondPosition1 = selectedSecondBreak.getSecond();
				final int secondPosition2 = selectedSecondBreak.getFirst();

				if (secondPosition1 == (position1 + 1)) {
					// 3opt2
					final Move3over2 result = new Move3over2();

					result.setResource2(sequence1);
					result.setResource2Position(position1 + 1);

					result.setResource1(sequence2);
					result.setResource1Start(position2); // inclusive
					result.setResource1End(secondPosition2 + 1); // exclusive

					return result;
				} else {
					// 4opt2
					final Move4over2 result = new Move4over2(sequence1, position1 + 1, secondPosition1, sequence2, position2, secondPosition2 + 1);

					return result;
				}
			} else {
				return new NullMove4Over2();
			}
		}
	}
}
