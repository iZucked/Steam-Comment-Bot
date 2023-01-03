/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Singleton;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

@Singleton
public class LazyFollowersAndPrecedersProviderImpl implements IFollowersAndPreceders {

	@Inject
	private LegalSequencingChecker checker;

	@Inject
	private IPhaseOptimisationData optimisationData;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	private Map<ISequenceElement, IResource> spotElementMap;

	private Map<ISequenceElement, IResource> terminalElementMap;
	/**
	 * A structure caching the output of the {@link LegalSequencingChecker}. If an element x is in the set mapped to by key y, x can legally follow y under some circumstance
	 */
	private final Map<@NonNull ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> validFollowers = new ConcurrentHashMap<>();
	private final Map<@NonNull ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> validPreceders = new ConcurrentHashMap<>();

	private Function<ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> computeFollowers;
	private Function<ISequenceElement, @NonNull Followers<@NonNull ISequenceElement>> computePreceeders;

	/**
	 */
	@Override
	public Followers<@NonNull ISequenceElement> getValidFollowers(@NonNull ISequenceElement element) {
		return validFollowers.computeIfAbsent(element, computeFollowers);
	}

	/**
	 */
	@Override
	public Followers<@NonNull ISequenceElement> getValidPreceders(@NonNull ISequenceElement element) {
		return validPreceders.computeIfAbsent(element, computePreceeders);
	}

	@Inject
	public void buildCache() {

		checker.disallowLateness();

		spotElementMap = new HashMap<>();
		terminalElementMap = new HashMap<>();
		for (final IResource resource : optimisationData.getResources()) {

			{
				final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
				final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);

				terminalElementMap.put(startElement, resource);
				terminalElementMap.put(endElement, resource);
			}

			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter != null) {
				if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
					final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
					final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
					final ISequenceElement virtualElement = virtualVesselSlotProvider.getElementForVesselCharter(vesselCharter);

					spotElementMap.put(startElement, resource);
					spotElementMap.put(virtualElement, resource);
					spotElementMap.put(endElement, resource);
				}
			}
		}

		computeFollowers = (e1) -> {

			final LinkedHashSet<ISequenceElement> followers = new LinkedHashSet<>();

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
						// continue;
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

				final boolean allowForwardSequence = spotResource == null ? checker.allowSequence(e1, e2) : checker.allowSequence(e1, e2, spotResource);
				if (allowForwardSequence) {
					if (expectFalse) {
						int ii = 0;
					}
					followers.add(e2);
				}

			}

			return new Followers<>(followers);
		};
		computePreceeders = (e1) -> {

			final LinkedHashSet<ISequenceElement> preceders = new LinkedHashSet<>();

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
						// continue;
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

				final boolean allowReverseSequence = spotResource == null ? checker.allowSequence(e2, e1) : checker.allowSequence(e2, e1, spotResource);
				if (allowReverseSequence) {
					if (expectFalse) {
						int ii = 0;
					}
					preceders.add(e2);
				}
			}

			return new Followers<>(preceders);
		};
	}

}
