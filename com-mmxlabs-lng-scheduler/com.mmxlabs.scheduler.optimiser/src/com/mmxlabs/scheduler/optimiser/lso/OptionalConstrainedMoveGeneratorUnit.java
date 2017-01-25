/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
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
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.moves.InsertOptionalElements;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.RemoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.RemoveOptionalElement;
import com.mmxlabs.scheduler.optimiser.lso.moves.ReplaceMoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapOptionalElements;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * A module for the {@link ConstrainedMoveGenerator} which handles moves around optional slots.
 * 
 * @author hinton
 * 
 */
public class OptionalConstrainedMoveGeneratorUnit implements IConstrainedMoveGeneratorUnit {
	private final ConstrainedMoveGenerator owner;

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private IResourceAllocationConstraintDataComponentProvider racDCP;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	public OptionalConstrainedMoveGeneratorUnit(final ConstrainedMoveGenerator owner) {
		super();
		this.owner = owner;
	}

	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {
		// select an optional element at random
		final ISequenceElement optional = RandomHelper.chooseElementFrom(random, optionalElementsProvider.getOptionalElements());
		final Pair<IResource, Integer> location = stateManager.lookup(optional);

		if (location.getFirst() == null) {
			return generateAddingMove(optional, location.getSecond(), stateManager, rawSequences, random);
		} else {
			return generateRemovingMove(optional, location, stateManager, rawSequences, random);
		}
	}

	/**
	 * Attempt to remove the optional element at the given location, patching up the solution if possible.
	 * 
	 * @param unused
	 * @param location
	 * @return
	 */
	private IMove generateRemovingMove(final ISequenceElement element, final Pair<IResource, Integer> location, final ILookupManager stateManager, final ISequences rawSequences, final Random random) {
		final Integer locationIndex = location.getSecond();
		final ISequence locationSequence = rawSequences.getSequence(location.getFirst());
		final ISequenceElement beforeElement = locationSequence.get(locationIndex - 1);
		final ISequenceElement afterElement = locationSequence.get(locationIndex + 1);

		// check whether beforeElement can be before afterElement
		if (random.nextBoolean() && followersAndPreceders.getValidFollowers(beforeElement).contains(afterElement)) {
			// we can just cut out the optional element
			return new RemoveOptionalElement(location.getFirst(), locationIndex);
		} else {
			// we need to do something to make the solution valid after removing this element
			// either we can pop in another unused element, or we can patch something else in instead,
			// or we can remove another element as well.
			// for now let's assume that we need to patch an element from elsewhere
			// 1. try and find another optional element which is in use

			// first check whether either neighbour is optional, and if so whether we can
			// take it out as well and get a good answer

			if (optionalElementsProvider.isElementOptional(beforeElement)) {
				// check whether we can skip out both
				final ISequenceElement beforeBeforeElement = locationSequence.get(locationIndex - 2);
				if (followersAndPreceders.getValidFollowers(beforeBeforeElement).contains(afterElement)) {
					// remove both
					return new RemoveOptionalElement(location.getFirst(), locationIndex, locationIndex - 1);
				}
			}

			if (optionalElementsProvider.isElementOptional(afterElement)) {
				final ISequenceElement afterAfterElement = locationSequence.get(locationIndex + 2);
				if (followersAndPreceders.getValidFollowers(beforeElement).contains(afterAfterElement)) {
					// remove both
					return new RemoveOptionalElement(location.getFirst(), locationIndex + 1, locationIndex);
				}
			}

			final ISequenceElement another = RandomHelper.chooseElementFrom(random, optionalElementsProvider.getOptionalElements());
			final Pair<IResource, Integer> location2 = stateManager.lookup(another);
			if (location2.getFirst() == null) {
				// this is a spare element, so we can rotate them
				return new SwapOptionalElements(location.getFirst(), locationIndex, location2.getSecond());
			} else {
				// try cutting them both out
				// TODO consider whether we can make this more efficient
				// as half the time we will pick L/L or D/D
				final ISequenceElement beforeAnother = rawSequences.getSequence(location2.getFirst()).get(location2.getSecond() - 1);
				final ISequenceElement afterAnother = rawSequences.getSequence(location2.getFirst()).get(location2.getSecond() + 1);
				// find whether we can move one of these into the gap
				if (followersAndPreceders.getValidFollowers(beforeElement).contains(afterAnother)) {
					// here afterAnother can go after before; resource2 is the thing whose guy gets moved.
					final IResource resource1 = location2.getFirst();
					final IResource resource2 = location.getFirst();
					return new RemoveAndFill(resource1, resource2, location2.getSecond(), locationIndex);
				} else if (followersAndPreceders.getValidFollowers(beforeAnother).contains(afterElement)) {
					// the converse of the above case.
					final IResource resource1 = location.getFirst();
					final IResource resource2 = location2.getFirst();
					return new RemoveAndFill(resource1, resource2, locationIndex, location2.getSecond());
				}
			}
		}

		return null;
	}

	private IMove generateAddingMove(final ISequenceElement unused, final int unusedIndex, final ILookupManager stateManager, final ISequences rawSequences, final Random random) {
		// the element is currently not in the solution, so try and add it

		// find something which can go after this element
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(unused);

		if (followers.size() > 0) {

			LOOP_TRIES: for (int t = 0; t < 10; ++t) {
				// there is an element which can follow this element.
				final ISequenceElement follower = followers.get(RandomHelper.nextIntBetween(random, 0, followers.size() - 1));
				// check whether follower is already in the solution somewhere
				final Pair<IResource, Integer> followerPosition = stateManager.lookup(follower);
				if (followerPosition.getSecond() == -1) {
					continue;
				}

				if (followerPosition.getFirst() != null) {
					// follower is not currently unused, so we need to find what's before it
					final IResource resource = followerPosition.getFirst();
					final int position = followerPosition.getSecond();
					// this is the element currently before the follower
					final ISequenceElement beforeFollower = rawSequences.getSequence(resource).get(position - 1);
					// these are the elements which can go after what's currently before the follower
					Followers<ISequenceElement> beforeFollowerFollowers = followersAndPreceders.getValidFollowers(beforeFollower);
					if (!checkResource(unused, resource)) {
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
						for (ISequenceElement e : beforeFollowerFollowers) {
							bffSet.add(e);
						}
						final Followers<ISequenceElement> unusedPreceeders = followersAndPreceders.getValidPreceders(unused);
						final Set<ISequenceElement> upSet = new LinkedHashSet<ISequenceElement>(unusedPreceeders.size());
						for (ISequenceElement e : unusedPreceeders) {
							upSet.add(e);
						}
						// Keep intersection
						bffSet.retainAll(upSet);

						List<ISequenceElement> candidates = new ArrayList<ISequenceElement>(bffSet);
						Collections.shuffle(candidates, random);

						LOOP_ELEMENTS: for (final ISequenceElement candidate : candidates) {
							final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
							if (candidatePosition.getSecond() == -1) {
								continue;
							}
							if (followersAndPreceders.getValidFollowers(candidate).contains(unused)) {
								if (candidatePosition.getFirst() == null) {
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
									final ISequence candidateSequence = rawSequences.getSequence(candidatePosition.getFirst());
									final ISequenceElement beforeCandidate = candidateSequence.get(candidatePosition.getSecond() - 1);
									final ISequenceElement afterCandidate = candidateSequence.get(candidatePosition.getSecond() + 1);

									if (!checkResource(candidate, resource)) {
										continue LOOP_ELEMENTS;
									}

									final IResource candidateResource = candidatePosition.getFirst();
									if (followersAndPreceders.getValidFollowers(beforeCandidate).contains(afterCandidate)) {

										// we can just cut out candidate
										// TODO MOVE HERE
										return new MoveAndFill(candidateResource, resource, candidatePosition.getSecond(), unusedIndex, followerPosition.getSecond(), false);
									} else {
										final Followers<ISequenceElement> beforeFollowers = followersAndPreceders.getValidFollowers(beforeCandidate);

										final List<ISequenceElement> spares = new ArrayList<>(rawSequences.getUnusedElements());
										Collections.shuffle(spares, random);
										LOOP_SPARES: for (final ISequenceElement spare : spares) {

											if (!checkResource(spare, candidateResource)) {
												continue LOOP_SPARES;
											}

											// TODO this loop could be a hashset intersection; not sure what's faster.
											if (beforeFollowers.contains(spare) && followersAndPreceders.getValidFollowers(spare).contains(afterCandidate)) {

												// we have a working filler element to do the move above.
												final Pair<IResource, Integer> fillerPosition = stateManager.lookup(spare);
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
					final Followers<ISequenceElement> followerFollowers = followersAndPreceders.getValidFollowers(follower);

					final List<Integer> elements = new ArrayList<Integer>(followerFollowers.size());
					for (int i = 0; i < followerFollowers.size(); ++i) {
						elements.add(i);
					}
					Collections.shuffle(elements, random);

					LOOP_ELEMENTS: for (final int idx : elements) {
						final ISequenceElement insertElement = followerFollowers.get(idx);
						// final ISequenceElement insertElement = followerFollowers.get(RandomHelper.nextIntBetween(owner.random, 0, followerFollowers.size() - 1));
						final Pair<IResource, Integer> insertPosition = stateManager.lookup(insertElement);
						if (insertPosition.getFirst() != null) {
							final IResource insertResource = insertPosition.getFirst();
							final int insertBefore = insertPosition.getSecond();

							if (!checkResource(insertElement, insertResource)) {
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

	private boolean checkResource(final ISequenceElement element, final IResource resource) {

		final Collection<IResource> allowedResources = racDCP.getAllowedResources(element);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}
}
