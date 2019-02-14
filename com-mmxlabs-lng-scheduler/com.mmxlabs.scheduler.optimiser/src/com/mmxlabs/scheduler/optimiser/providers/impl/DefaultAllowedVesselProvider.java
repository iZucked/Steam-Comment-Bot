/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;

public class DefaultAllowedVesselProvider implements IAllowedVesselProviderEditor {

	private final Map<@NonNull IPortSlot, @Nullable Collection<@NonNull IVessel>> permittedVesselMap = new HashMap<>();

	@Override
	public @Nullable Collection<@NonNull IVessel> getPermittedVessels(@NonNull final IPortSlot portSlot) {
		return permittedVesselMap.getOrDefault(portSlot, null);
	}

	@Override
	public void setPermittedVesselAndClasses(@NonNull final IPortSlot portSlot, @Nullable final Collection<@NonNull IVessel> permittedVessels) {
		permittedVesselMap.put(portSlot, permittedVessels);
	}

	@Override
	public boolean isPermittedOnVessel(final IPortSlot portSlot, final IVessel vessel) {

		boolean allowedOnVessel = true;

		@Nullable
		final Collection<@NonNull IVessel> permittedVessels = getPermittedVessels(portSlot);
		if (permittedVessels != null) {
			if (vessel == null) {
				allowedOnVessel = false;
			} else if (!permittedVessels.contains(vessel)) {
				allowedOnVessel = false;
			}
		}

		if (permittedVessels == null) {
			// No restrictions
			return true;
		} else {
			return allowedOnVessel;
		}
	}
}
