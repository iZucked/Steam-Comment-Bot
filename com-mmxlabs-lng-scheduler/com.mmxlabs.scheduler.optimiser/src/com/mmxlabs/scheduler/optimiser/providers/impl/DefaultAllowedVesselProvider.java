/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;

public class DefaultAllowedVesselProvider implements IAllowedVesselProviderEditor {

	private final Map<@NonNull IPortSlot, @Nullable Collection<@NonNull IVessel>> permittedVesselMap = new HashMap<>();
	private final Map<@NonNull IPortSlot, @Nullable Collection<@NonNull IVesselClass>> permittedVesselClassMap = new HashMap<>();

	@Override
	public @Nullable Collection<@NonNull IVessel> getPermittedVessels(@NonNull final IPortSlot portSlot) {
		return permittedVesselMap.getOrDefault(portSlot, null);
	}

	@Override
	public @Nullable Collection<@NonNull IVesselClass> getPermittedVesselClasses(@NonNull final IPortSlot portSlot) {
		return permittedVesselClassMap.getOrDefault(portSlot, null);
	}

	@Override
	public void setPermittedVesselAndClasses(@NonNull final IPortSlot portSlot, @Nullable final Collection<@NonNull IVessel> permittedVessels,
			@Nullable final Collection<@NonNull IVesselClass> permittedVesselClasses) {
		permittedVesselMap.put(portSlot, permittedVessels);
		permittedVesselClassMap.put(portSlot, permittedVesselClasses);
	}

	@Override
	public boolean isPermittedOnVessel(final IPortSlot portSlot, final IVessel vessel, final IVesselClass vesselClass) {

		boolean allowedOnVessel = true;
		boolean allowedOnVesselClass = true;

		@Nullable
		final Collection<@NonNull IVessel> permittedVessels = getPermittedVessels(portSlot);
		if (permittedVessels != null) {
			if (vessel == null) {
				allowedOnVessel = false;
			} else if (!permittedVessels.contains(vessel)) {
				allowedOnVessel = false;
			}
		}

		@Nullable
		final Collection<@NonNull IVesselClass> permittedVesselClasses = getPermittedVesselClasses(portSlot);
		if (permittedVesselClasses != null) {
			if (vesselClass == null) {
				allowedOnVesselClass = false;
			} else if (!permittedVesselClasses.contains(vesselClass)) {
				allowedOnVesselClass = false;
			}
		}

		if (permittedVessels == null && permittedVesselClasses == null) {
			// No restrictions
			return true;
		} else if (permittedVessels != null && permittedVesselClasses == null) {
			return allowedOnVessel;
		} else if (permittedVessels == null && permittedVesselClasses != null) {
			return allowedOnVesselClass;
		} else {
			return allowedOnVessel || allowedOnVesselClass;
		}
	}
}
