package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProviderEditor;

public class HashMapNominatedVesselProvider implements INominatedVesselProviderEditor {

	private final String name;

	private final Map<ISequenceElement, IVessel> map = new HashMap<>();

	public HashMapNominatedVesselProvider(final String name) {
		this.name = name;
	}

	@Override
	@Nullable
	public IVessel getNominatedVessel(@NonNull ISequenceElement element) {
		return map.get(element);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		map.clear();
	}

	@Override
	public void setNominatedVessel(@NonNull ISequenceElement element, @NonNull IVessel vessel) {
		map.put(element, vessel);
	}

}
