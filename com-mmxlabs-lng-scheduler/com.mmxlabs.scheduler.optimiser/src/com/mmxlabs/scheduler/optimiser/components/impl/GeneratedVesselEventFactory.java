package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Provider;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * This class is injected into a {@link GeneratedCharterOutVesselEventPortSlot} to create a per slot vessel event. This is intended to be injected into the
 * {@link GeneratedCharterOutVesselEventPortSlot} as a {@link Provider} with the {@link PerChainUnitScope} scope so that optimisations off the same transformed data, but running in different threads
 * do not change each others state. This class itself is not thread safe as it is expected to be created on a per-thread basis.
 * 
 * @author Simon Goodall
 *
 */
public class GeneratedVesselEventFactory {

	@NonNull
	private final Map<IGeneratedCharterOutVesselEventPortSlot, GeneratedCharterOutVesselEvent> slotToEventMap = new HashMap<>();

	public GeneratedCharterOutVesselEvent getEvent(@NonNull final IGeneratedCharterOutVesselEventPortSlot slot, @NonNull final IPort startPort) {
		GeneratedCharterOutVesselEvent event = slotToEventMap.get(slot);
		if (event == null) {
			event = new GeneratedCharterOutVesselEvent();
			event.setStartPort(startPort);
			slotToEventMap.put(slot, event);
		}
		return event;
	}

}
