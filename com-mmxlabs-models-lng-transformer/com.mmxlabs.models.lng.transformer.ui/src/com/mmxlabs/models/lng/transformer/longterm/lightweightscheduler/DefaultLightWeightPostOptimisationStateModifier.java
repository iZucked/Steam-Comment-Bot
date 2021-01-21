/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultLightWeightPostOptimisationStateModifier implements ILightWeightPostOptimisationStateModifier {
	@Inject
	IRoundTripVesselPermissionProviderEditor roundTripProvider;
	@Inject
	IVesselProvider vesselProvider;
	@Inject
	IPortTypeProvider portTypeProvider;
	@Inject
	IPortSlotProvider portSlotProvider;

	@Override
	public void modifyState(ISequences outputSequences) {
		{
			LinkedList<Entry<@NonNull IResource, @NonNull ISequence>> entriesList = new LinkedList<>(outputSequences.getSequences().entrySet());
			List<Entry<@NonNull IResource, @NonNull ISequence>> virtualSequences = entriesList.stream()
			.filter(e -> vesselProvider
					.getVesselAvailability(e.getKey())
					.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP)
			.collect(Collectors.toList());
			for (Entry<@NonNull IResource, @NonNull ISequence> entry : virtualSequences) {
				 for (ISequenceElement element : entry.getValue()) {
					if (portTypeProvider.getPortType(element) == PortType.Load ||
							portTypeProvider.getPortType(element) == PortType.Discharge) {
						roundTripProvider.permitElementOnResource(element, portSlotProvider.getPortSlot(element), entry.getKey(), vesselProvider.getVesselAvailability(entry.getKey()));
					}
				}
			}
		}
	}

}
