/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IAdpFrozenAssignmentProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultLightWeightPostOptimisationStateModifier implements ILightWeightPostOptimisationStateModifier {
	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IRoundTripVesselPermissionProviderEditor roundTripProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider;
	@Inject
	IAdpFrozenAssignmentProvider frozenAssignmentProvider;
	@Inject
	ModelEntityMap modelEntityMap;

	@Override
	public void modifyState(final ISequences outputSequences) {
		final List<Entry<@NonNull IResource, @NonNull ISequence>> entriesList = new LinkedList<>(outputSequences.getSequences().entrySet());

		final List<Entry<@NonNull IResource, @NonNull ISequence>> virtualSequences = entriesList.stream()
				.filter(e -> vesselProvider.getVesselCharter(e.getKey()).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP)
				.toList();

		for (final Entry<@NonNull IResource, @NonNull ISequence> entry : virtualSequences) {
			for (final ISequenceElement element : entry.getValue()) {
				if (portTypeProvider.getPortType(element) == PortType.Load || portTypeProvider.getPortType(element) == PortType.Discharge) {
					roundTripProvider.permitElementOnResource(element, entry.getKey(), vesselProvider.getVesselCharter(entry.getKey()));
				}
			}
		}
		List<IVesselCharter> lockedCharters = frozenAssignmentProvider.getLockedVesselCharters();
		final IVesselCharter nominalVesselCharter = (IVesselCharter) modelEntityMap.getNamedOptimiserObject(OptimiserConstants.DEFAULT_INTERNAL_VESSEL);
		assert nominalVesselCharter != null;
		final IResource nomResource = vesselProvider.getResource(nominalVesselCharter);
		for (IVesselCharter vc : lockedCharters) {
			for (final ICargo cargo : frozenAssignmentProvider.getCargoesFromVesselCharter(vc)) {
				for (final IPortSlot slot : cargo.getPortSlots()) {
					final ISequenceElement slotElement = portSlotProvider.getElement(slot);
					Collection<IResource> lockedResources = resourceAllocationProvider.getAllowedResources(slotElement);
					if (lockedResources != null) {
						List<IResource> newResources = new LinkedList<>(lockedResources);
						newResources.remove(nomResource);
						resourceAllocationProvider.setAllowedResources(slotElement, newResources);
					}
				}
			}
		}

	}
}
