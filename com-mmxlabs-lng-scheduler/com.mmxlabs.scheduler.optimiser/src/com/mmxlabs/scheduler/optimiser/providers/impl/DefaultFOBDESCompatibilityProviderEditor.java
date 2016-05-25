package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IFOBDESCompatibilityProviderEditor;

public class DefaultFOBDESCompatibilityProviderEditor implements IFOBDESCompatibilityProviderEditor {

	private final Map<IPortSlot, Collection<@NonNull IVesselAvailability>> portSlotMap = new HashMap<>();
	private final Map<ISequenceElement, Collection<@NonNull IResource>> elementMap = new HashMap<>();

	@Override
	public boolean isPermittedOnResource(@NonNull final IPortSlot portSlot, @NonNull final IVesselAvailability vesselAvailability) {

		final Collection<@NonNull IVesselAvailability> c = portSlotMap.get(portSlot);
		if (c != null) {
			return c.contains(vesselAvailability);
		}
		return false;
	}

	@Override
	public boolean isPermittedOnResource(@NonNull final ISequenceElement element, @NonNull final IResource resource) {
		final Collection<@NonNull IResource> c = elementMap.get(element);
		if (c != null) {
			return c.contains(resource);
		}
		return false;
	}

	@Override
	public void permitElementOnResource(@NonNull final ISequenceElement element, @NonNull final IPortSlot portSlot, @NonNull final IResource resource,
			@NonNull final IVesselAvailability vesselAvailability) {

		elementMap.merge(element, Sets.newHashSet(resource), (c, r) -> {
			c.addAll(r);
			return new HashSet<>(c);
		});

		portSlotMap.merge(portSlot, Sets.newHashSet(vesselAvailability), (c, r) -> {
			c.addAll(r);
			return new HashSet<>(c);
		});
	}

}
