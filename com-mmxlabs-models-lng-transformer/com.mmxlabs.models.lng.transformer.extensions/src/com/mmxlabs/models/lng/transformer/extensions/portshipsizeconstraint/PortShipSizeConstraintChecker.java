/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

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
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 */
public class PortShipSizeConstraintChecker implements IPairwiseConstraintChecker, IResourceElementConstraintChecker {

	/**
	 * The name of this constraint checker.
	 */
	private final String name;
	
	@Inject
	private IPortShipSizeProvider portShipSizeProvider;

	@Inject
	@NonNull
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;
	
	public PortShipSizeConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
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
			IVessel vessel = getVessel(resource);
			
			//IS it a FOB or DES resource.
			if (vessel == null) continue;
			
			final ISequence sequence = sequences.getSequence(resource);
			for (final ISequenceElement current : sequence) {
				if (!checkVesselElementConstraint(vessel, current)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Get the vessel associated with a particular resource, if there is one.
	 * @param resource
	 * @return a vessel, or null, if it is a DES_PURCHASE or FOB_SALE with a dummy vessel.
	 */
	@Nullable
	private IVessel getVessel(IResource resource) {
		IVessel vessel = nominatedVesselProvider.getNominatedVessel(resource);
		if (vessel == null) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE && 
				vesselAvailability.getVesselInstanceType() != VesselInstanceType.FOB_SALE) { 
				vessel = vesselAvailability.getVessel();
			}
		}
		return vessel;
	}
	
	private boolean checkResourceElementConstraint(@NonNull final IResource resource, @NonNull final ISequenceElement sequenceElement) {
		final IVessel vessel = getVessel(resource);
		if (vessel != null) {
			return checkVesselElementConstraint(vessel, sequenceElement);
		}
		else {
			//If it a FOB or DES sequence, return true.
			return true;
		}
	}
	
	private boolean checkVesselElementConstraint(@NonNull final IVessel vessel, @NonNull final ISequenceElement sequenceElement) {
		return this.portShipSizeProvider.isCompatible(vessel, sequenceElement);
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource) {
		return checkResourceElementConstraint(resource, first) && checkResourceElementConstraint(resource, second);
	}

	@Override
	public @Nullable String explain(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource) {
		return null;
	}

	@Override
	public boolean checkElement(@NonNull ISequenceElement element, @NonNull IResource resource) {
		return checkResourceElementConstraint(resource, element);
	}
}
