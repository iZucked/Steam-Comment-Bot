package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IFobSaleRotationProviderEditor;

@NonNullByDefault
public class DefaultFobSaleRotationProviderEditor implements IFobSaleRotationProviderEditor {
	private Map<@NonNull IDischargeOption, @Nullable IVesselCharter> dischargeOptionToVesselCharterMap = new HashMap<>();

	@Override
	public @Nullable IVesselCharter getVesselCharter(IDischargeOption dischargeOption) {
		return dischargeOptionToVesselCharterMap.get(dischargeOption);
	}

	@Override
	public void addMapping(IVesselCharter vesselCharter, IDischargeOption dischargeOption) {
		dischargeOptionToVesselCharterMap.put(dischargeOption, vesselCharter);
	}

}
