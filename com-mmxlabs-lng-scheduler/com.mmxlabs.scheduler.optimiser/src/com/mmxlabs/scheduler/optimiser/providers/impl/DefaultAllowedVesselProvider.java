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
	public @Nullable Collection<@NonNull IVessel> getPermittedVessels(@NonNull IPortSlot portSlot) {
		return permittedVesselMap.getOrDefault(portSlot, null);
	}

	@Override
	public @Nullable Collection<@NonNull IVesselClass> getPermittedVesselClasses(@NonNull IPortSlot portSlot) {
		return permittedVesselClassMap.getOrDefault(portSlot, null);
	}

	@Override
	public void setPermittedVesselAndClasses(@NonNull IPortSlot portSlot, @Nullable Collection<@NonNull IVessel> permittedVessels, @Nullable Collection<@NonNull IVesselClass> permittedVesselClasses) {
		permittedVesselMap.put(portSlot, permittedVessels);
		permittedVesselClassMap.put(portSlot, permittedVesselClasses);
	}

}
