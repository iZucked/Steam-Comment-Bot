/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultLightWeightPostOptimisationStateModifier implements ILightWeightPostOptimisationStateModifier {
	@Inject
	private IRoundTripVesselPermissionProviderEditor roundTripProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

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
	}
}
