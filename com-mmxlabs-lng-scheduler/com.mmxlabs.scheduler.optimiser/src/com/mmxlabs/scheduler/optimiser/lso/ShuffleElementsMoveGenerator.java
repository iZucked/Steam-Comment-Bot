/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator.Followers;
import com.mmxlabs.scheduler.optimiser.lso.moves.ShuffleElements.ShuffleElementsBuilder;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;

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
	private IResourceAllocationConstraintDataComponentProvider racDCP;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	private List<ISequenceElement> targetElements;

	public ShuffleElementsMoveGenerator(final ConstrainedMoveGenerator owner) {
		super();
		this.owner = owner;
	}
	
	@Inject
	public void init() {
		targetElements = new ArrayList<ISequenceElement>(owner.context.getOptimisationData().getSequenceElements().size());
		// Determine possible slots which can be moved.
		for (final ISequenceElement e : owner.context.getOptimisationData().getSequenceElements()) {
			// TODO: check new API - null might be events or start/ends?
			// TODO: Really need port type in here
			final Collection<IResource> resources = racDCP.getAllowedResources(e);
			if ((resources != null && resources.size() > 1) || optionalElementsProvider.isElementOptional(e)) {
				targetElements.add(e);
			} else if (alternativeElementProvider.hasAlternativeElement(e)) {
				targetElements.add(e);
				targetElements.add(alternativeElementProvider.getAlternativeElement(e));
			}
		}
	}

	@Override
	public void setSequences(final ISequences sequences) {

	}

	@Override
	public IMove generateMove() {

		// TODO: Disable move generator completely in such cases
		if (targetElements.isEmpty()) {
			return null;
		}

		final ShuffleElementsBuilder builder = new ShuffleElementsBuilder();

		// Set of elements already considered in our move. They should no longer be considered available or in the follower or preceders lists
		final Set<ISequenceElement> touchedElements = new HashSet<ISequenceElement>();
		final ISequenceElement rawElement = RandomHelper.chooseElementFrom(owner.getRandom(), targetElements);
		final Pair<Integer, Integer> elementPosition = owner.reverseLookup.get(rawElement);
		final IResource elementResource = elementPosition.getFirst() == null ? null : owner.getSequences().getResources().get(elementPosition.getFirst());
		final ISequence elementSequence = owner.getSequences().getSequence(elementResource);
		touchedElements.add(rawElement);
		
		if (elementPosition.getSecond() == -1) {
			return null;
		}

		// If there is an alternative, randomly choose to use it.
		// TODO: Work into the decisions more directly to be more effective.
		final ISequenceElement alternativeElement;
		if (alternativeElementProvider.hasAlternativeElement(rawElement) && owner.getRandom().nextBoolean()) {
			alternativeElement = alternativeElementProvider.getAlternativeElement(rawElement);
		} else {
			alternativeElement = null;
		}
		final ISequenceElement element = alternativeElement == null ? rawElement : alternativeElement;

		boolean foundMove = false;
		// Link to a follower or preceder
		final boolean findFollower = owner.getRandom().nextBoolean();
		// Fix up destination part
		if (findFollower) {
			final Followers<ISequenceElement> followers = owner.validFollowers.get(element);
			if (followers.size() == 0) {
				return null;
			}
			for (final ISequenceElement follower : shuffleFollowers(followers)) {
				final Pair<Integer, Integer> followerPosition = owner.reverseLookup.get(follower);

				// Element is also not used!
				if (followerPosition.getFirst() == null) {
					continue;
				}

				// Check we can move our element to this resource
				final IResource followerResource = owner.getSequences().getResources().get(followerPosition.getFirst());
				if (followerResource == elementResource) {
					continue;
				}
				if (!checkResource(element, followerResource)) {
					continue;
				}

				final ISequence followerSequence = owner.getSequences().getSequence(followerResource);

				final ISequenceElement followerMinus1 = followerSequence.get(followerPosition.getSecond() - 1);

				if (owner.validFollowers.get(followerMinus1).contains(element)) {
					// We can link the head and tail together directly - no need to do more.
					builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
					builder.addTo(followerResource, followerPosition.getSecond(), 1);
					foundMove = true;
					break;
				}
				if (followerPosition.getSecond() > 2) {
					final ISequenceElement followerMinus2 = followerSequence.get(followerPosition.getSecond() - 2);
					if (owner.getRandom().nextBoolean() && owner.validFollowers.get(followerMinus2).contains(element)) {

						if (removeOrBackfill(builder, followerMinus1, touchedElements)) {
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
					final Set<ISequenceElement> candidates = findPossibleUnusedElement(followerResource, owner.validFollowers.get(followerMinus1), owner.validPreceeders.get(element));
					candidates.removeAll(touchedElements);
					if (candidates.isEmpty()) {
						continue;
					}
					final List<ISequenceElement> l = new ArrayList<ISequenceElement>(candidates);

					// pick random candidate
					{
						final ISequenceElement candidate = l.get(owner.getRandom().nextInt(l.size()));
						final Pair<Integer, Integer> candidatePosition = owner.reverseLookup.get(candidate);
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
			final Followers<ISequenceElement> preceders = owner.validPreceeders.get(element);
			if (preceders.size() == 0) {
				return null;
			}
			for (final ISequenceElement preceder : shuffleFollowers(preceders)) {
				final Pair<Integer, Integer> precederPosition = owner.reverseLookup.get(preceder);
				// Element is also not used!
				if (precederPosition.getFirst() == null) {
					continue;
				}

				// Check we can move our element to this resource
				final IResource precederResource = owner.getSequences().getResources().get(precederPosition.getFirst());
				if (precederResource == elementResource) {
					continue;
				}
				if (!checkResource(element, precederResource)) {
					continue;
				}
				final ISequence precederSequence = owner.getSequences().getSequence(precederResource);

				final ISequenceElement precederPlus1 = precederSequence.get(precederPosition.getSecond() + 1);

				if (owner.validPreceeders.get(precederPlus1).contains(element)) {
					builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
					builder.addTo(precederResource, precederPosition.getSecond() + 1, 1);
					// We can link the head and tail together directly - no need to do more.
					foundMove = true;
					break;
				}

				if (owner.getRandom().nextBoolean() && precederSequence.size() > precederPosition.getSecond() + 2) {
					final ISequenceElement precederPlus2 = precederSequence.get(precederPosition.getSecond() + 2);
					if (owner.validPreceeders.get(precederPlus2).contains(element)) {
						if (removeOrBackfill(builder, precederPlus1, touchedElements)) {
							builder.addFrom(elementResource, elementPosition.getSecond(), rawElement, alternativeElement);
							builder.addTo(precederResource, precederPosition.getSecond() + 1, 1);

							touchedElements.add(precederPlus1);
							foundMove = true;
							break;
						}
					}
				}
				{
					final Set<ISequenceElement> candidates = findPossibleUnusedElement(precederResource, owner.validFollowers.get(element), owner.validPreceeders.get(precederPlus1));
					candidates.removeAll(touchedElements);
					if (candidates.isEmpty()) {
						continue;
					}
					final List<ISequenceElement> l = new ArrayList<ISequenceElement>(candidates);

					// pick random candidate
					{
						final ISequenceElement candidate = l.get(owner.getRandom().nextInt(l.size()));
						final Pair<Integer, Integer> candidatePosition = owner.reverseLookup.get(candidate);
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
			return null;
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

			if (owner.validFollowers.get(elementMinus1).contains(elementPlus1)) {
				// We can link the head and tail together directly - no need to do more.
				return builder.getMove();
			}

			// Head and tail cannot be joined together. We have three options. Find an unused element to insert into the gap, remove element - 1 or remove element + 1. If we remove the element,
			{
				// Find optional element
				final Set<ISequenceElement> candidates = findPossibleUnusedElement(elementResource, owner.validFollowers.get(elementMinus1), owner.validPreceeders.get(elementPlus1));
				candidates.removeAll(touchedElements);
				if (!candidates.isEmpty()) {
					final List<ISequenceElement> l = new ArrayList<ISequenceElement>(candidates);

					// pick random candidate
					final ISequenceElement candidate = l.get(owner.getRandom().nextInt(l.size()));
					final Pair<Integer, Integer> candidatePosition = owner.reverseLookup.get(candidate);
					// Finally! an element we can add to the sequence to make it valid.
					builder.addFrom(null, candidatePosition.getSecond(), candidate);
					builder.addTo(elementResource, elementPosition.getSecond(), 1);

					touchedElements.add(candidate);

					return builder.getMove();
				}

			}
			if (elementPosition.getSecond() >= 2) {
				final ISequenceElement elementMinus2 = elementSequence.get(elementPosition.getSecond() - 2);
				if (owner.validFollowers.get(elementMinus2).contains(elementPlus1)) {
					// We can link the head and tail together directly - no need to do more.
					if (removeOrBackfill(builder, elementMinus1, touchedElements)) {
						touchedElements.add(elementMinus1);
						return builder.getMove();
					}
				}
			}
			if (elementSequence.size() > elementPosition.getSecond() + 2) {
				final ISequenceElement elementPlus2 = elementSequence.get(elementPosition.getSecond() + 2);
				if (owner.validFollowers.get(elementMinus1).contains(elementPlus2)) {
					if (removeOrBackfill(builder, elementPlus1, touchedElements)) {
						touchedElements.add(elementPlus1);

						return builder.getMove();
					}
				}

			}
		}

		return null;

	}

	private boolean removeOrBackfill(final ShuffleElementsBuilder builder, final ISequenceElement target, final Set<ISequenceElement> touchedElements) {
		// We can link the smaller head and tail together directly - need to do something the the extra element
		final Pair<Integer, Integer> targetPosition = owner.reverseLookup.get(target);
		final IResource targetResource = owner.getSequences().getResources().get(targetPosition.getFirst());
		if (optionalElementsProvider.isElementOptional(target) && owner.getRandom().nextBoolean()) {
			// Can stick on unused elements list
			builder.addFrom(targetResource, targetPosition.getSecond(), target);
			builder.addTo(null, 0, 1);
			return true;
		}
		// Else to to find a backfill option
		if (owner.getRandom().nextBoolean()) {
			// Find a preceder
			final Followers<ISequenceElement> preceders = owner.validPreceeders.get(target);
			// TODO: randomise
			for (final ISequenceElement p : shuffleFollowers(preceders)) {
				if (touchedElements.contains(p)) {
					continue;
				}
				final Pair<Integer, Integer> position = owner.reverseLookup.get(p);
				if (position.getFirst() == null) {
					continue;
				}
				final IResource resource = owner.getSequences().getResources().get(position.getFirst());
				final ISequence sequence = owner.getSequences().getSequence(resource);

				final Set<ISequenceElement> candidates = findPossibleUnusedElement(resource, owner.validFollowers.get(target), owner.validPreceeders.get(sequence.get(position.getSecond() + 1)));
				candidates.removeAll(touchedElements);
				if (candidates.isEmpty()) {
					continue;
				}
				final List<ISequenceElement> l = new ArrayList<ISequenceElement>(candidates);

				// pick random candidate
				{
					final ISequenceElement candidate = l.get(owner.getRandom().nextInt(l.size()));
					final Pair<Integer, Integer> candidatePosition = owner.reverseLookup.get(candidate);
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
			final Followers<ISequenceElement> followers = owner.validFollowers.get(target);
			// TODO: randomise
			for (final ISequenceElement f : shuffleFollowers(followers)) {
				if (touchedElements.contains(f)) {
					continue;
				}
				final Pair<Integer, Integer> position = owner.reverseLookup.get(f);
				if (position.getFirst() == null) {
					continue;
				}
				final IResource resource = owner.getSequences().getResources().get(position.getFirst());
				final ISequence sequence = owner.getSequences().getSequence(resource);

				final Set<ISequenceElement> candidates = findPossibleUnusedElement(resource, owner.validFollowers.get(sequence.get(position.getSecond() - 1)), owner.validPreceeders.get(target));
				candidates.removeAll(touchedElements);
				if (candidates.isEmpty()) {
					continue;
				}
				final List<ISequenceElement> l = new ArrayList<ISequenceElement>(candidates);

				// pick random candidate
				{
					final ISequenceElement candidate = l.get(owner.getRandom().nextInt(l.size()));
					final Pair<Integer, Integer> candidatePosition = owner.reverseLookup.get(candidate);
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

	private boolean checkResource(final ISequenceElement elmenet, final IResource resource) {

		final Collection<IResource> allowedResources = racDCP.getAllowedResources(elmenet);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}

	private Set<ISequenceElement> findPossibleUnusedElement(final IResource resource, final ConstrainedMoveGenerator.Followers<ISequenceElement> followers,
			final ConstrainedMoveGenerator.Followers<ISequenceElement> preceeders) {
		final Set<ISequenceElement> possibleFillers = new LinkedHashSet<ISequenceElement>();
		{ // Find the set of unused elements which could be replace candidate
			for (final ISequenceElement e : followers) {
				if (checkResource(e, resource)) {
					possibleFillers.add(e);
				}
			}
			if (!possibleFillers.isEmpty()) {
				final HashSet<ISequenceElement> temp = new HashSet<ISequenceElement>();
				for (final ISequenceElement e : preceeders) {
					if (checkResource(e, resource)) {
						temp.add(e);
					}
				}
				possibleFillers.retainAll(temp);
			}
			if (!possibleFillers.isEmpty()) {

				final HashSet<ISequenceElement> temp = new HashSet<ISequenceElement>();
				for (final ISequenceElement e : owner.sequences.getUnusedElements()) {
					if (checkResource(e, resource)) {
						temp.add(e);
					}
				}
				possibleFillers.retainAll(temp);
			}
		}
		return possibleFillers;
	}

	private List<ISequenceElement> shuffleFollowers(final ConstrainedMoveGenerator.Followers<ISequenceElement> followers) {
		final List<ISequenceElement> l = Lists.newArrayList(followers);
		Collections.shuffle(l, owner.getRandom());
		return l;

	}
}
