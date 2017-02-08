/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.moves.ShuffleElements.ShuffleElementsBuilder;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A module for the {@link ConstrainedMoveGenerator} handles moves on a per slot basis. This moved generator selects single slot and attempts to move it next to another preceder or follower in another
 * {@link ISequence}. Typically slots need to be grouped as a pair of {@link ILoadOption} - {@link IDischargeOption}. This means moving a single slot will often require moving another slot or two.
 * This move generator will attempt to remove or add a new slot on the destination side to maintain the pairs. It will also try to do the same on the source {@link ISequence}. Typically removing
 * additional slots will either place the slot in the {@link ISequences#getUnusedElements()} list or attempt to find another unused slot and place it into another {@link ISequence}.
 * 
 * @author Simon Goodall.
 * 
 */
public class ShuffleElementsMoveGenerator implements IConstrainedMoveGeneratorUnit {
	private final ConstrainedMoveGenerator owner;

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IResourceAllocationConstraintDataComponentProvider racDCP;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	private List<@NonNull ISequenceElement> targetElements;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	public ShuffleElementsMoveGenerator(final ConstrainedMoveGenerator owner) {
		super();
		this.owner = owner;
	}

	@Inject
	public void init() {
		targetElements = new ArrayList<ISequenceElement>(owner.data.getSequenceElements().size());
		// Determine possible slots which can be moved.
		for (final ISequenceElement e : owner.data.getSequenceElements()) {
			// TODO: check new API - null might be events or start/ends?
			// TODO: Really need port type in here
			final Collection<IResource> resources = racDCP.getAllowedResources(e);
			if (resources == null || (resources.size() > 1) || optionalElementsProvider.isElementOptional(e)) {
				final PortType portType = portTypeProvider.getPortType(e);

				if (portType == PortType.Load || portType == PortType.Discharge) {
					targetElements.add(e);
				}
			} else if (alternativeElementProvider.hasAlternativeElement(e)) {
				targetElements.add(e);
				targetElements.add(alternativeElementProvider.getAlternativeElement(e));
			}
		}
	}

	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {

		// TODO: Disable move generator completely in such cases
		if (targetElements.isEmpty()) {
			return new NullShuffleElementsMove();
		}

		final ShuffleElementsBuilder builder = new ShuffleElementsBuilder();

		// Set of elements already considered in our move. They should no longer be considered available or in the follower or preceders lists
		final Set<@NonNull ISequenceElement> touchedElements = new HashSet<ISequenceElement>();
		final ISequenceElement rawElement = RandomHelper.chooseElementFrom(random, targetElements);
		final Pair<IResource, Integer> elementPosition = stateManager.lookup(rawElement);
		final IResource elementResource = elementPosition.getFirst();
		if (elementResource == null) {
			return new NullShuffleElementsMove();
		}
		final ISequence elementSequence = rawSequences.getSequence(elementResource);

		touchedElements.add(rawElement);

		if (elementPosition.getSecond() == -1) {
			return new NullShuffleElementsMove();
		}

		// If there is an alternative, randomly choose to use it.
		// TODO: Work into the decisions more directly to be more effective.
		final ISequenceElement alternativeElement;
		if (alternativeElementProvider.hasAlternativeElement(rawElement) && random.nextBoolean()) {
			alternativeElement = alternativeElementProvider.getAlternativeElement(rawElement);
		} else {
			alternativeElement = null;
		}
		final ISequenceElement element = alternativeElement == null ? rawElement : alternativeElement;

		boolean foundMove = false;
		// Link to a follower or preceder
		final boolean findFollower = random.nextBoolean();
		// Fix up destination part
		if (findFollower) {
			final Followers<@NonNull ISequenceElement> followers = followersAndPreceders.getValidFollowers(element);
			if (followers.size() == 0) {
				return new NullShuffleElementsMove();
			}
			for (final ISequenceElement follower : shuffleFollowers(followers, random)) {
				final Pair<IResource, Integer> followerPosition = stateManager.lookup(follower);

				// Element is also not used!
				if (followerPosition.getFirst() == null) {

					// If this is an unused FOB Sales , we can try to link the FOB sale to the purchase and remove the original discharge
					// TODO: Add similar case for FOB Sale
					final IVesselAvailability vesselAvailabilityForElement = virtualVesselSlotProvider.getVesselAvailabilityForElement(follower);
					if (vesselAvailabilityForElement != null) {
						// Should be an unused FOB Sale route
						final IResource precederResource = vesselProvider.getResource(vesselAvailabilityForElement);
						assert precederResource != null;
						if (!checkResource(element, precederResource)) {
							continue;
						}
						// Use same offset (and insert in reverse order) as the builder code will correct for this.
						builder.addFrom(null, followerPosition.getSecond(), follower);
						builder.addTo(precederResource, 1, 1);
						builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
						builder.addTo(precederResource, 1, 1);

						touchedElements.add(follower);
						foundMove = true;
						break;
					} else {
						continue;
					}
				}

				// Check we can move our element to this resource
				final IResource followerResource = followerPosition.getFirst();
				if (followerResource == null || followerResource == elementResource) {
					continue;
				}
				if (!checkResource(element, followerResource)) {
					continue;
				}

				final ISequence followerSequence = rawSequences.getSequence(followerResource);

				final ISequenceElement followerMinus1 = followerSequence.get(followerPosition.getSecond() - 1);

				if (followersAndPreceders.getValidFollowers(followerMinus1).contains(element)) {
					// We can link the head and tail together directly - no need to do more.
					builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
					builder.addTo(followerResource, followerPosition.getSecond(), 1);
					foundMove = true;
					break;
				}
				if (followerPosition.getSecond() > 2) {
					final ISequenceElement followerMinus2 = followerSequence.get(followerPosition.getSecond() - 2);
					if (random.nextBoolean() && followersAndPreceders.getValidFollowers(followerMinus2).contains(element)) {

						if (removeOrBackfill(builder, followerMinus1, touchedElements, stateManager, rawSequences, random)) {
							builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
							builder.addTo(followerResource, followerPosition.getSecond(), 1);
							// Yey1
							touchedElements.add(followerMinus1);
							foundMove = true;
							break;
						}
					}
				}
				{
					final Set<@NonNull ISequenceElement> candidates = findPossibleUnusedElement(followerResource, followersAndPreceders.getValidFollowers(followerMinus1),
							followersAndPreceders.getValidPreceders(element), rawSequences);
					candidates.removeAll(touchedElements);
					if (candidates.isEmpty()) {
						continue;
					}
					final List<@NonNull ISequenceElement> l = new ArrayList<>(candidates);

					// pick random candidate
					{
						final ISequenceElement candidate = l.get(random.nextInt(l.size()));
						final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
						// Finally! an element we can add to the sequence to make it valid.
						builder.addFrom(null, candidatePosition.getSecond());
						builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
						builder.addTo(followerResource, followerPosition.getSecond(), 2);

						touchedElements.add(follower);
						touchedElements.add(candidate);
						foundMove = true;
						break;

					}
				}
			}

		} else {
			final Followers<@NonNull ISequenceElement> preceders = followersAndPreceders.getValidPreceders(element);

			if (preceders.size() == 0) {
				return new NullShuffleElementsMove();
			}

			for (final ISequenceElement preceder : shuffleFollowers(preceders, random)) {
				final Pair<IResource, Integer> precederPosition = stateManager.lookup(preceder);
				// Element is also not used!
				if (precederPosition.getFirst() == null) {

					// If this is an unused DES Purchase, we can try to link the DES purchase to the sale and remove the original load
					final IVesselAvailability vesselAvailabilityForElement = virtualVesselSlotProvider.getVesselAvailabilityForElement(preceder);
					if (vesselAvailabilityForElement != null) {
						// Should be an unused DES route
						final IResource precederResource = vesselProvider.getResource(vesselAvailabilityForElement);
						assert precederResource != null;
						if (!checkResource(element, precederResource)) {
							continue;
						}
						// Use same offset (and insert in reverse order) as the builder code will correct for this.
						builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
						builder.addTo(precederResource, 1, 1);
						builder.addFrom(null, precederPosition.getSecond(), preceder);
						builder.addTo(precederResource, 1, 1);

						touchedElements.add(preceder);
						foundMove = true;
						break;
					} else {
						// Skip
						continue;
					}
				}

				// Check we can move our element to this resource
				final IResource precederResource = precederPosition.getFirst();
				if (precederResource == null || precederResource == elementResource) {
					continue;
				}
				if (!checkResource(element, precederResource)) {
					continue;
				}
				final ISequence precederSequence = rawSequences.getSequence(precederResource);

				final ISequenceElement precederPlus1 = precederSequence.get(precederPosition.getSecond() + 1);

				if (followersAndPreceders.getValidPreceders(precederPlus1).contains(element)) {
					builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
					builder.addTo(precederResource, precederPosition.getSecond() + 1, 1);
					// We can link the head and tail together directly - no need to do more.
					foundMove = true;
					break;
				}

				if (random.nextBoolean() && precederSequence.size() > precederPosition.getSecond() + 2) {
					final ISequenceElement precederPlus2 = precederSequence.get(precederPosition.getSecond() + 2);
					if (followersAndPreceders.getValidPreceders(precederPlus2).contains(element)) {
						if (removeOrBackfill(builder, precederPlus1, touchedElements, stateManager, rawSequences, random)) {
							builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
							builder.addTo(precederResource, precederPosition.getSecond() + 1, 1);

							touchedElements.add(precederPlus1);
							foundMove = true;
							break;
						}
					}
				}
				{
					final Set<@NonNull ISequenceElement> candidates = findPossibleUnusedElement(precederResource, followersAndPreceders.getValidFollowers(element),
							followersAndPreceders.getValidPreceders(precederPlus1), rawSequences);
					candidates.removeAll(touchedElements);
					if (candidates.isEmpty()) {
						continue;
					}
					final List<@NonNull ISequenceElement> l = new ArrayList<>(candidates);

					// pick random candidate
					{
						final ISequenceElement candidate = l.get(random.nextInt(l.size()));
						final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
						// Finally! an element we can add to the sequence to make it valid.
						builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
						builder.addFrom(null, candidatePosition.getSecond(), candidate);
						builder.addTo(precederResource, precederPosition.getSecond() + 1, 2);

						touchedElements.add(preceder);
						touchedElements.add(candidate);
						foundMove = true;
						break;

					}
				}
			}
		}
		if (!foundMove) {
			return new NullShuffleElementsMove();
		}

		// Fix up source part
		{
			if (elementPosition.getFirst() == null) {
				// Element is unused, no need to do more.
				return builder.getMove();
			}
			assert elementSequence != null;
			final ISequenceElement elementMinus1 = elementSequence.get(elementPosition.getSecond() - 1);
			final ISequenceElement elementPlus1 = elementSequence.get(elementPosition.getSecond() + 1);

			if (followersAndPreceders.getValidFollowers(elementMinus1).contains(elementPlus1)) {
				// We can link the head and tail together directly - no need to do more.
				return builder.getMove();
			}

			// Head and tail cannot be joined together. We have three options. Find an unused element to insert into the gap, remove element - 1 or remove element + 1. If we remove the element,
			{
				// Find optional element
				final Set<@NonNull ISequenceElement> candidates = findPossibleUnusedElement(elementResource, followersAndPreceders.getValidFollowers(elementMinus1),
						followersAndPreceders.getValidPreceders(elementPlus1), rawSequences);
				candidates.removeAll(touchedElements);
				if (!candidates.isEmpty()) {
					final List<@NonNull ISequenceElement> l = new ArrayList<>(candidates);

					// pick random candidate
					final ISequenceElement candidate = l.get(random.nextInt(l.size()));
					final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
					// Finally! an element we can add to the sequence to make it valid.
					builder.addFrom(null, candidatePosition.getSecond(), candidate);
					builder.addTo(elementResource, elementPosition.getSecond(), 1);

					touchedElements.add(candidate);

					return builder.getMove();
				}

			}
			if (elementPosition.getSecond() >= 2) {
				final ISequenceElement elementMinus2 = elementSequence.get(elementPosition.getSecond() - 2);
				if (followersAndPreceders.getValidFollowers(elementMinus2).contains(elementPlus1)) {
					// We can link the head and tail together directly - no need to do more.
					if (removeOrBackfill(builder, elementMinus1, touchedElements, stateManager, rawSequences, random)) {
						touchedElements.add(elementMinus1);
						return builder.getMove();
					}
				}
			}
			if (elementSequence.size() > elementPosition.getSecond() + 2) {
				final ISequenceElement elementPlus2 = elementSequence.get(elementPosition.getSecond() + 2);
				if (followersAndPreceders.getValidFollowers(elementMinus1).contains(elementPlus2)) {
					if (removeOrBackfill(builder, elementPlus1, touchedElements, stateManager, rawSequences, random)) {
						touchedElements.add(elementPlus1);

						return builder.getMove();
					}
				}

			}
		}

		return new NullShuffleElementsMove();

	}

	private boolean removeOrBackfill(final ShuffleElementsBuilder builder, final @NonNull ISequenceElement target, final Set<@NonNull ISequenceElement> touchedElements,
			final ILookupManager stateManager, final ISequences rawSequences, Random random) {
		// We can link the smaller head and tail together directly - need to do something the the extra element
		final Pair<IResource, Integer> targetPosition = stateManager.lookup(target);
		final IResource targetResource = targetPosition.getFirst();
		if (optionalElementsProvider.isElementOptional(target) && random.nextBoolean()) {
			// Can stick on unused elements list
			builder.addFrom(targetResource, targetPosition.getSecond(), target);
			builder.addTo(null, 0, 1);
			return true;
		}
		// Else to to find a backfill option
		if (random.nextBoolean()) {
			// Find a preceder
			final Followers<@NonNull ISequenceElement> preceders = followersAndPreceders.getValidPreceders(target);
			// TODO: randomise
			for (final ISequenceElement p : shuffleFollowers(preceders, random)) {
				if (touchedElements.contains(p)) {
					continue;
				}
				final Pair<IResource, Integer> position = stateManager.lookup(p);
				if (position.getFirst() == null) {
					continue;
				}
				final IResource resource = position.getFirst();
				if (resource == null) {
					continue;
				}
				final ISequence sequence = rawSequences.getSequence(resource);

				final Set<@NonNull ISequenceElement> candidates = findPossibleUnusedElement(resource, followersAndPreceders.getValidFollowers(target),
						followersAndPreceders.getValidPreceders(sequence.get(position.getSecond() + 1)), rawSequences);
				candidates.removeAll(touchedElements);
				if (candidates.isEmpty()) {
					continue;
				}
				final List<@NonNull ISequenceElement> l = new ArrayList<>(candidates);

				// pick random candidate
				{
					final ISequenceElement candidate = l.get(random.nextInt(l.size()));
					final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
					// Finally! an element we can add to the sequence to make it valid.
					builder.addFrom(targetResource, targetPosition.getSecond(), target);
					builder.addFrom(null, candidatePosition.getSecond(), candidate);
					builder.addTo(resource, position.getSecond(), 2);

					touchedElements.add(p);
					touchedElements.add(candidate);

					return true;
				}
			}

			// Did not find an option, so true to remove if optional (assumes the previous check resulted in false)
			if (optionalElementsProvider.isElementOptional(target)) {
				// Can stick on unused elements list
				builder.addFrom(targetResource, targetPosition.getSecond(), target);
				builder.addTo(null, 0, 1);
				return true;
			}

			return false;
		} else {
			// // Find a follower
			final Followers<@NonNull ISequenceElement> followers = followersAndPreceders.getValidFollowers(target);
			// TODO: randomise
			for (final ISequenceElement f : shuffleFollowers(followers, random)) {
				if (touchedElements.contains(f)) {
					continue;
				}
				final Pair<IResource, Integer> position = stateManager.lookup(f);
				if (position.getFirst() == null) {
					continue;
				}
				final IResource resource = position.getFirst();
				if (resource == null) {
					continue;
				}
				final ISequence sequence = rawSequences.getSequence(resource);

				final Set<ISequenceElement> candidates = findPossibleUnusedElement(resource, followersAndPreceders.getValidFollowers(sequence.get(position.getSecond() - 1)),
						followersAndPreceders.getValidPreceders(target), rawSequences);
				candidates.removeAll(touchedElements);
				if (candidates.isEmpty()) {
					continue;
				}
				final List<@NonNull ISequenceElement> l = new ArrayList<>(candidates);

				// pick random candidate
				{
					final ISequenceElement candidate = l.get(random.nextInt(l.size()));
					final Pair<IResource, Integer> candidatePosition = stateManager.lookup(candidate);
					// Finally! an element we can add to the sequence to make it valid.
					builder.addFrom(null, candidatePosition.getSecond(), candidate);
					builder.addFrom(targetResource, targetPosition.getSecond(), target);
					builder.addTo(resource, position.getSecond(), 2);

					touchedElements.add(f);
					touchedElements.add(candidate);

					return true;
				}
			}

			// Did not find an option, so true to remove if optional (assumes the previous check resulted in false)
			if (optionalElementsProvider.isElementOptional(target)) {
				// Can stick on unused elements list
				builder.addFrom(targetResource, targetPosition.getSecond(), target);
				builder.addTo(null, 0, 1);
				return true;
			}

			return false;
		}
	}

	private boolean checkResource(final @NonNull ISequenceElement element, final @NonNull IResource resource) {

		// Disable re-wiring on round trips.
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}

		final Collection<@NonNull IResource> allowedResources = racDCP.getAllowedResources(element);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}

	private Set<@NonNull ISequenceElement> findPossibleUnusedElement(final @NonNull IResource resource, final Followers<@NonNull ISequenceElement> followers,
			final Followers<@NonNull ISequenceElement> preceders, final ISequences rawSequences) {
		final Set<@NonNull ISequenceElement> possibleFillers = new LinkedHashSet<>();
		{ // Find the set of unused elements which could be replace candidate
			for (final ISequenceElement e : followers) {
				if (checkResource(e, resource)) {
					possibleFillers.add(e);
				}
			}
			if (!possibleFillers.isEmpty()) {
				final HashSet<ISequenceElement> temp = new HashSet<ISequenceElement>();
				for (final ISequenceElement e : preceders) {
					if (checkResource(e, resource)) {
						temp.add(e);
					}
				}
				possibleFillers.retainAll(temp);
			}
			if (!possibleFillers.isEmpty()) {

				final HashSet<ISequenceElement> temp = new HashSet<ISequenceElement>();
				for (final ISequenceElement e : rawSequences.getUnusedElements()) {
					if (checkResource(e, resource)) {
						temp.add(e);
					}
				}
				possibleFillers.retainAll(temp);
			}
		}
		return possibleFillers;
	}

	private List<@NonNull ISequenceElement> shuffleFollowers(final Followers<@NonNull ISequenceElement> followers, Random random) {
		final List<@NonNull ISequenceElement> l = Lists.newArrayList(followers);
		Collections.shuffle(l, random);
		return l;

	}
}
