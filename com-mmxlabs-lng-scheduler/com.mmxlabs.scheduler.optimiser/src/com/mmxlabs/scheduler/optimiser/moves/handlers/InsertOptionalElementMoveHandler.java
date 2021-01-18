/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertSegmentMove;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.ReplaceMoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapOptionalElements;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * A module for the {@link ConstrainedMoveGenerator} which handles moves around optional slots.
 * 
 * @author hinton
 * 
 */
public class InsertOptionalElementMoveHandler implements IMoveGenerator {
	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IMoveHelper helper;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;
	
	@Inject 
	private IPortSlotProvider portSlotProvider;
	
	private enum Action {
	    INSERT, SWAP, OTHER, NOTHING
	}
	
	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager lookupManager, @NonNull Random random) {

		if (phaseOptimisationData.getOptionalElements().isEmpty()) {
			return new NullMove("RemoveOptionalElementMoveHandler", "No optional elements");
		}

		final List<@NonNull ISequenceElement> optionalElements = new ArrayList<>(phaseOptimisationData.getOptionalElements());
		Collections.shuffle(optionalElements, random);

		// for (final ISequenceElement optional : optionalElements) {
		// final Pair<IResource, Integer> location = lookupManager.lookup(optional);
		// if (location.getFirst() == null) {
		// return generateAddingMove(optional, location.getSecond());
		// }
		// }
		
		if(phaseOptimisationData.getOptionalElements().isEmpty()){
			return new NullMove("InsertOptionalElement", "No Optional Elements");
		}

		final ISequenceElement optional = RandomHelper.chooseElementFrom(random, phaseOptimisationData.getOptionalElements());
		final Pair<IResource, Integer> location = lookupManager.lookup(optional);

		if (location.getFirst() == null) {
			return generateAddingMove(optional, location.getSecond(), lookupManager, random);
		}

		return new NullMove("InsertOptionalElement", "Non-Null First Element");
	}

	public IMove generateAddingMove(final @NonNull ISequenceElement unused, final int unusedIndex, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {
		// the element is currently not in the solution, so try and add it
		
		List<ISequenceElement> orderedElements = new ArrayList<>(1);
		orderedElements.add(unused);
		
		IPortSlot portSlot = portSlotProvider.getPortSlot(unused);
		if (portSlot instanceof IVesselEventPortSlot) {
			IVesselEventPortSlot vesselEventPortSlot = (IVesselEventPortSlot) portSlot; 
			orderedElements = vesselEventPortSlot.getEventSequenceElements();
		}
		
		// find something which can go after this element
		final Followers<@NonNull ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedElements.get(orderedElements.size() - 1));
        
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

					final boolean canInsert = beforeFollowerFollowers.contains(orderedElements.get(0));
					final boolean canSwap = phaseOptimisationData.getOptionalElements().contains(beforeFollower);

					// TODO: Be able to activate code path manually 
					// Create the possible choice set depending on insert and swap condition
					Action choice= Action.NOTHING;
					List<Action> choices = new ArrayList<>(3);
					
					choices.add(Action.NOTHING);
					
					if (canInsert) {
						choices.add(Action.INSERT);
					}

					if (canSwap) {
						choices.add(Action.SWAP);
					}
					
					if (choices.size() > 0) {
						choice = RandomHelper.chooseElementFrom(random, choices);
					}
					
					if (choice == Action.INSERT) {
						final InsertSegmentMove.Builder builder = InsertSegmentMove.Builder.newMove();

						builder.withElements(null, orderedElements)
							.withInsertBefore(resource, follower); 

						return builder.create();
					} 
					else if (choice == Action.SWAP) {
							return new SwapOptionalElements(resource, position - 1, unusedIndex);
					} else {
						// we need to find something else to pop in
						// which (a) can go after what's currently before f
						// and (b) has unused in its follower set
						final Set<ISequenceElement> bffSet = new LinkedHashSet<ISequenceElement>(beforeFollowerFollowers.size());
						for (final ISequenceElement e : beforeFollowerFollowers) {
							bffSet.add(e);
						}
						final Followers<ISequenceElement> unusedPreceeders = followersAndPreceders.getValidPreceders(orderedElements.get(0));
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
									
									final InsertSegmentMove.Builder builder = InsertSegmentMove.Builder.newMove();
									List<ISequenceElement> sequenceSegment = new ArrayList<>(orderedElements);
									
									sequenceSegment.add(0, candidate);
									builder.withElements(null, sequenceSegment) 
										.withInsertAfter(resource, beforeFollower); 

									return builder.create();
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

							final InsertSegmentMove.Builder builder = InsertSegmentMove.Builder.newMove();
							
							List<ISequenceElement> sequenceSegment = new ArrayList<>(orderedElements);
							sequenceSegment.add(follower);
							
							builder.withElements(null, sequenceSegment)
									.withInsertBefore(insertResource, insertElement);
							
							return builder.create();

						}
					}
				}
			}
		}
		return null;
	}
}
