package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IAdpFrozenAssignmentProviderEditor;

public class DefaultAdpFrozenProvider implements IAdpFrozenAssignmentProviderEditor {

	private Map<ILoadOption, IDischargeOption> pairs = new HashMap<>();
	private Map<ICargo, IVesselCharter> cargoToCharterMap = new HashMap<>();
	
	@Override
	public boolean isPaired(@NonNull IPortSlot slot) {
		
		return pairs.containsKey(slot) || pairs.containsValue(slot);
	}

	@Override
	public boolean isCargoPair(@NonNull ILoadOption slot1, @NonNull IDischargeOption slot2) {
		return pairs.get(slot1) == slot2;
	}

	@Override
	public boolean isLockedToVessel(@NonNull ILoadOption slot1, @NonNull IDischargeOption slot2) {
		return cargoToCharterMap.entrySet().stream().anyMatch(x -> x.getKey().getPortSlots().containsAll(List.of(slot1, slot2)));
	}

	@Override
	public @NonNull IVesselCharter getVesselCharter(@NonNull ILoadOption load, @NonNull IDischargeOption discharge) {
		
		for(Entry<ICargo, IVesselCharter> cargoEntry : cargoToCharterMap.entrySet()) {
			ICargo cargo = cargoEntry.getKey();
			if(cargo.getPortSlots().containsAll(Set.of(load, discharge))) {
				return cargoEntry.getValue();
			}
		}
		throw new IllegalArgumentException(String.format("Cannot find locked vessel charter with Load:%s Discharge:%s", load.getId(), discharge.getId()));
	}

	@Override
	public void setCargoPair(@NonNull ILoadOption load, @NonNull IDischargeOption discharge) {
		pairs.put(load, discharge);
		
	}

	@Override
	public void setVesselAssignment(@NonNull IVesselCharter charter, @NonNull ILoadOption load, @NonNull IDischargeOption discharge) {
		setCargoPair(load, discharge);
		cargoToCharterMap.put(() -> List.of(load, discharge), charter);
	}

	@Override
	@NonNullByDefault
	public  Map<ILoadOption, IDischargeOption> getCargoPairs() {
		return pairs;
	}

	@Override
	public @NonNull List<@NonNull IVesselCharter> getLockedVesselCharters() {
		return cargoToCharterMap.values().stream().distinct().toList();
	}

	@Override
	public @NonNull List<@NonNull ICargo> getCargoesFromVesselCharter(@NonNull IVesselCharter v) {
		return cargoToCharterMap.entrySet().stream().filter(x -> x.getValue().equals(v)).map(x -> x.getKey()).toList();
	}

}
