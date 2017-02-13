/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Singleton;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

@Singleton
public class ParallelFollowersAndPrecedersProviderImpl implements IFollowersAndPreceders {

	@Inject
	private LegalSequencingChecker checker;

	@Inject
	private IOptimisationData optimisationData;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	/**
	 * A structure caching the output of the {@link LegalSequencingChecker}. If an element x is in the set mapped to by key y, x can legally follow y under some circumstance
	 */
	private final Map<@NonNull ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> validFollowers = new ConcurrentHashMap<>();
	private final Map<@NonNull ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> validPreceders = new ConcurrentHashMap<>();

	/**
	 */
	public Map<@NonNull ISequenceElement, Followers<@NonNull ISequenceElement>> getValidFollowers() {
		return validFollowers;
	}

	/**
	 */
	public Map<@NonNull ISequenceElement, Followers<@NonNull ISequenceElement>> getValidPreceeders() {
		return validPreceders;
	}

	/**
	 */
	@Override
	public Followers<@NonNull ISequenceElement> getValidFollowers(@NonNull ISequenceElement element) {
		return validFollowers.get(element);
	}

	/**
	 */
	@Override
	public Followers<@NonNull ISequenceElement> getValidPreceders(@NonNull ISequenceElement element) {
		return validPreceders.get(element);
	}

	@Inject
	public void buildCache() {

		checker.disallowLateness();

		// Build of a map of special cargo elements for FOB/DES cargoes.
		final Map<ISequenceElement, IResource> spotElementMap = new HashMap<>();
		final Map<ISequenceElement, IResource> terminalElementMap = new HashMap<>();
		for (final IResource resource : optimisationData.getResources()) {
			
			{
				final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
				final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);

				terminalElementMap.put(startElement, resource);
				terminalElementMap.put(endElement, resource);
			}
			
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability != null) {
				if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
					final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
					final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
					final ISequenceElement virtualElement = virtualVesselSlotProvider.getElementForVesselAvailability(vesselAvailability);

					spotElementMap.put(startElement, resource);
					spotElementMap.put(virtualElement, resource);
					spotElementMap.put(endElement, resource);
				}
			}
		}

		// Do lookups in parallel
		optimisationData.getSequenceElements().parallelStream() //
				.forEach(e1 -> {
					final LinkedHashSet<ISequenceElement> followers = new LinkedHashSet<ISequenceElement>();
					final LinkedHashSet<ISequenceElement> preceders = new LinkedHashSet<ISequenceElement>();

					for (final ISequenceElement e2 : optimisationData.getSequenceElements()) {
						if (e1 == e2) {
							continue;
						}

						// Check for special cargo elements. A special element can only go on one resource and all three elements in the set must be on the same one.
						if (spotElementMap.containsKey(e1) && spotElementMap.containsKey(e2)) {
							final IResource r1 = spotElementMap.get(e1);
							final IResource r2 = spotElementMap.get(e2);
							if (r1 != r2) {
								continue;
							}
						}
						// Check for special cargo elements. A special element can only go on one resource and all three elements in the set must be on the same one.
						boolean expectFalse = false;
						if (terminalElementMap.containsKey(e1) && terminalElementMap.containsKey(e2)) {
							final IResource r1 = terminalElementMap.get(e1);
							final IResource r2 = terminalElementMap.get(e2);
							if (r1 != r2) {
//								continue;
								expectFalse = true;
							}
						}
						// This code segment yields a large speed up, but breaks ITS

						// If any of e1 or e2 is a special spot element then there is only one resource it is permitted to go on. Ignore the rest for sequencing checks.
						IResource spotResource = null;
						if (spotElementMap.containsKey(e1)) {
							spotResource = spotElementMap.get(e1);
						} else if (spotElementMap.containsKey(e2)) {
							spotResource = spotElementMap.get(e2);
						}

						final boolean allowForwardSequence = spotResource == null ? checker.allowSequence(e1, e2, false) : checker.allowSequence(e1, e2, spotResource, false);
						if (allowForwardSequence) {
							if (expectFalse) {
								int ii = 0;
							}
							followers.add(e2);
						}

						final boolean allowReverseSequence = spotResource == null ? checker.allowSequence(e2, e1, false) : checker.allowSequence(e2, e1, spotResource, false);
						if (allowReverseSequence) {
							if (expectFalse) {
								int ii = 0;
							}
							preceders.add(e2);
						}
					}

					validFollowers.put(e1, new Followers<>(followers));
					validPreceders.put(e1, new Followers<>(preceders));
				});
		//
		//
		// optimisationData.getSequenceElements().stream() //
		// .filter(e2 -> (e1 != e2)) //
		// .forEach(e2 -> {
		//
		// });
		//// )
		//// .map(e2 -> new Pair<>(e1, e2)))
		//// .forEach(p -> {
		////
		//// });
		//
		//
		//
		// // create a massive lookup table, caching all legal sequencing decisions
		// // this might be a terrible idea, we could just keep the checker instead
		// // also need to fix the resource binding now
		// for (final ISequenceElement e1 : optimisationData.getSequenceElements()) {
		//
		// final LinkedHashSet<ISequenceElement> followers = new LinkedHashSet<ISequenceElement>();
		// final LinkedHashSet<ISequenceElement> preceders = new LinkedHashSet<ISequenceElement>();
		//
		// for (final ISequenceElement e2 : optimisationData.getSequenceElements()) {
		// if (e1 == e2) {
		// continue;
		// }
		//
		// // Check for special cargo elements. A special element can only go on one resource and all three elements in the set must be on the same one.
		// if (spotElementMap.containsKey(e1) && spotElementMap.containsKey(e2)) {
		// final IResource r1 = spotElementMap.get(e1);
		// final IResource r2 = spotElementMap.get(e2);
		// if (r1 != r2) {
		// continue;
		// }
		// }
		//
		// // This code segment yields a large speed up, but breaks ITS
		//
		// // If any of e1 or e2 is a special spot element then there is only one resource it is permitted to go on. Ignore the rest for sequencing checks.
		// IResource spotResource = null;
		// if (spotElementMap.containsKey(e1)) {
		// spotResource = spotElementMap.get(e1);
		// } else if (spotElementMap.containsKey(e2)) {
		// spotResource = spotElementMap.get(e2);
		// }
		//
		// final boolean allowForwardSequence = spotResource == null ? checker.allowSequence(e1, e2, false) : checker.allowSequence(e1, e2, spotResource, false);
		// if (allowForwardSequence) {
		// followers.add(e2);
		// }
		//
		// final boolean allowReverseSequence = spotResource == null ? checker.allowSequence(e2, e1, false) : checker.allowSequence(e2, e1, spotResource, false);
		// if (allowReverseSequence) {
		// preceders.add(e2);
		// }
		// }
		//
		// validFollowers.put(e1, new Followers<>(followers));
		// validPreceders.put(e1, new Followers<>(preceders));
		// }
	}
}
