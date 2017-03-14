/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.moves.InsertOptionalElements;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.ReplaceMoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapOptionalElements;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * A module for the {@link ConstrainedMoveGenerator} which handles moves around optional slots.
 * 
 * @author hinton
 * 
 */
public class InsertOptionalElementMoveHandler implements IMoveGenerator {
	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private IMoveHelper helper;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager lookupManager, @NonNull Random random) {

		if (optionalElementsProvider.getOptionalElements().isEmpty()) {
			return new NullMove("RemoveOptionalElementMoveHandler", "No optional elements");
		}

		final List<@NonNull ISequenceElement> optionalElements = new ArrayList<>(optionalElementsProvider.getOptionalElements());
		Collections.shuffle(optionalElements, random);

		// for (final ISequenceElement optional : optionalElements) {
		// final Pair<IResource, Integer> location = lookupManager.lookup(optional);
		// if (location.getFirst() == null) {
		// return generateAddingMove(optional, location.getSecond());
		// }
		// }

		final ISequenceElement optional = RandomHelper.chooseElementFrom(random, optionalElementsProvider.getOptionalElements());
		final Pair<IResource, Integer> location = lookupManager.lookup(optional);

		if (location.getFirst() == null) {
			return generateAddingMove(optional, location.getSecond(), lookupManager, random);
		}

		return new NullMove("InsertOptionalElement", "Non-Null First Element");
	}

	public IMove generateAddingMove(final @NonNull ISequenceElement unused, final int unusedIndex, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {
		// the element is currently not in the solution, so try and add it

		// find something which can go after this element
		final Followers<@NonNull ISequenceElement> followers = followersAndPreceders.getValidFollowers(unused);

		if (followers.size() > 0) {

			LOOP_TRIES: for (int t = 0; t < 10; ++t) {
				// there is an element which can follow this element.
				final ISequenceElement follower = followers.get(RandomHelper.nextIntBetween(random, 0, followers.size() - 1));
				// check whether follower is already in the solution somewhere
				final Pair<IResource, Integer> followerPosition = lookupManager.lookup(follower);
				if (followerPosition.getSecond() == -1) {
					continue;
				}

				final IResource resource = followerPosition.getFirst();
				if (resource != null) {
					// follower is not currently unused, so we need to find what's before it
					final int position = followerPosition.getSecond();
					// this is the element currently before the follower
					final ISequenceElement beforeFollower = lookupManager.getRawSequences().getSequence(resource).get(position - 1);
					// these are the elements which can go after what's currently before the follower
					final Followers<ISequenceElement> beforeFollowerFollowers = followersAndPreceders.getValidFollowers(beforeFollower);
					if (!helper.legacyCheckResource(unused, resource)) {
						continue LOOP_TRIES;
					}

					int numChoices = 1;
					final boolean canInsert = beforeFollowerFollowers.contains(unused);
					final boolean canSwap = optionalElementsProvider.getOptionalElements().contains(beforeFollower);

					if (canInsert) {
						++numChoices;
					}
					if (canSwap) {
						++numChoices;
					}
					final int choice = random.nextInt(numChoices);

					if ((numChoices == 3 && choice == 0) || numChoices == 2 && choice == 0 && canInsert) {
						// we can insert directly
						return new InsertOptionalElements(resource, position - 1, new int[] { unusedIndex });
					} else if ((numChoices == 3 && choice == 1) || numChoices == 2 && choice == 1 && canSwap) {
						// We can swap!
						return new SwapOptionalElements(resource, position - 1, unusedIndex);
					} else {
						// we need to find something else to pop in
						// which (a) can go after what's currently before f
						// and (b) has unused in its follower set
						final Set<ISequenceElement> bffSet = new LinkedHashSet<ISequenceElement>(beforeFollowerFollowers.size());
						for (final ISequenceElement e : beforeFollowerFollowers) {
							bffSet.add(e);
						}
						final Followers<ISequenceElement> unusedPreceeders = followersAndPreceders.getValidPreceders(unused);
						final Set<ISequenceElement> upSet = new LinkedHashSet<ISequenceElement>(unusedPreceeders.size());
						for (final ISequenceElement e : unusedPreceeders) {
							upSet.add(e);
						}
						// Keep intersection
						bffSet.retainAll(upSet);

						final List<@NonNull ISequenceElement> candidates = new ArrayList<>(bffSet);
						Collections.shuffle(candidates, random);

						LOOP_ELEMENTS: for (final ISequenceElement candidate : candidates) {
							final Pair<IResource, Integer> candidatePosition = lookupManager.lookup(candidate);
							if (candidatePosition.getSecond() == -1) {
								continue;
							}
							if (followersAndPreceders.getValidFollowers(candidate).contains(unused)) {
								final IResource candidateResource = candidatePosition.getFirst();
								if (candidateResource == null) {
									// candidate is currently spare, and can go between beforeFollower and unused, so we have
									// [... beforeFollower, +candidate, +unused, follower]
									return new InsertOptionalElements(resource, position - 1, new int[] { candidatePosition.getSecond(), unusedIndex });
								} else {
									// candidate already exists somewhere else in the solution; we should try and move it and backfill thus:
									// before move:
									// S1 : [beforeCandidate, candidate, afterCandidate]
									// S2 : [beforeFollower, follower]
									// spares : unused, filler
									// =>
									// S1: [beforeCandidate, +filler, afterCandidate]
									// S2: [beforeFollower, +candidate, +unused, follower]
									// need to find filler element
									final ISequence candidateSequence = lookupManager.getRawSequences().getSequence(candidateResource);
									final ISequenceElement beforeCandidate = candidateSequence.get(candidatePosition.getSecond() - 1);
									final ISequenceElement afterCandidate = candidateSequence.get(candidatePosition.getSecond() + 1);

									if (!helper.legacyCheckResource(candidate, resource)) {
										continue LOOP_ELEMENTS;
									}

									if (followersAndPreceders.getValidFollowers(beforeCandidate).contains(afterCandidate)) {

										// we can just cut out candidate
										// TODO MOVE HERE
										return new MoveAndFill(candidateResource, resource, candidatePosition.getSecond(), unusedIndex, followerPosition.getSecond(), false);
									} else {
										final Followers<@NonNull ISequenceElement> beforeFollowers = followersAndPreceders.getValidFollowers(beforeCandidate);

										final List<@NonNull ISequenceElement> spares = new ArrayList<>(lookupManager.getRawSequences().getUnusedElements());
										Collections.shuffle(spares, random);
										LOOP_SPARES: for (final ISequenceElement spare : spares) {

											if (!helper.legacyCheckResource(spare, candidateResource)) {
												continue LOOP_SPARES;
											}

											// TODO this loop could be a hashset intersection; not sure what's faster.
											if (beforeFollowers.contains(spare) && followersAndPreceders.getValidFollowers(spare).contains(afterCandidate)) {

												// we have a working filler element to do the move above.
												final Pair<IResource, Integer> fillerPosition = lookupManager.lookup(spare);
												// TODO these checks appear duplicated, and do not seem to be used
												// final boolean check = checkResource(candidate, resource);
												// checkResource(candidate, resource);

												return new ReplaceMoveAndFill(candidateResource, resource, candidatePosition.getSecond(), followerPosition.getSecond(), fillerPosition.getSecond(),
														unusedIndex, false);
											}
										}
									}
								}
							}
						}
					}
				} else {
					// follower is currently unused, so we can pop both new elements in somewhere
					// add [unused, follower] to solution somewhere feasible.

					// pick something live and insert next to it.
					// these are the things which can come after follower
					final Followers<@NonNull ISequenceElement> followerFollowers = followersAndPreceders.getValidFollowers(follower);

					final List<Integer> elements = new ArrayList<Integer>(followerFollowers.size());
					for (int i = 0; i < followerFollowers.size(); ++i) {
						elements.add(i);
					}
					Collections.shuffle(elements, random);

					LOOP_ELEMENTS: for (final int idx : elements) {
						final ISequenceElement insertElement = followerFollowers.get(idx);
						// final ISequenceElement insertElement = followerFollowers.get(RandomHelper.nextIntBetween(owner.random, 0, followerFollowers.size() - 1));
						final Pair<IResource, Integer> insertPosition = lookupManager.lookup(insertElement);
						final IResource insertResource = insertPosition.getFirst();
						if (insertResource != null) {
							final int insertBefore = insertPosition.getSecond();

							if (!helper.legacyCheckResource(insertElement, insertResource)) {
								continue LOOP_ELEMENTS;
							}
							return new InsertOptionalElements(insertResource, insertBefore - 1, new int[] { unusedIndex, followerPosition.getSecond() });
						}
					}
				}
			}
		}
		return null;
	}
}
