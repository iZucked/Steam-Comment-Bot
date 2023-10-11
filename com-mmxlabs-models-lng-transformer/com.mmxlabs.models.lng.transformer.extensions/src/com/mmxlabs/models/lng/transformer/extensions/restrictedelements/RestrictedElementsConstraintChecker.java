/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.InternalNameMapper;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class RestrictedElementsConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IRestrictedElementsProvider restrictedElementsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private InternalNameMapper internalNameMapper;

	public RestrictedElementsConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null && !checkPairwiseConstraint(prev, current, resource, messages)) {
					return false;
				}
				prev = current;
			}
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, List<String> messages) {

		final VesselInstanceType instanceType = vesselProvider.getVesselCharter(resource).getVesselInstanceType();
		if (instanceType == VesselInstanceType.ROUND_TRIP) {
			// Cargo pairs are independent of each other, so only check real load->discharge state and ignore rest
			final PortType t1 = portTypeProvider.getPortType(first);
			final PortType t2 = portTypeProvider.getPortType(second);
			// Accept, or fall through
			if (!(t1 == PortType.Load && t2 == PortType.Discharge)) {
				return true;
			}
		}

		boolean result = true;
		String msgFmt = "'%s' cannot go to '%s'. See port and contract restrictions.";
		if (restrictedElementsProvider.getRestrictedFollowerElements(first).contains(second)) {
			result = false;
			if (messages != null) {
				messages.add(String.format(msgFmt, internalNameMapper.generateString(first), internalNameMapper.generateString(second)));
			}
		} else if (restrictedElementsProvider.getRestrictedPrecedingElements(second).contains(first)) {
			result = false;
			if (messages != null) {
				messages.add(String.format(msgFmt, internalNameMapper.generateString(first), internalNameMapper.generateString(second)));
			}
		}

		return result;
	}
}
