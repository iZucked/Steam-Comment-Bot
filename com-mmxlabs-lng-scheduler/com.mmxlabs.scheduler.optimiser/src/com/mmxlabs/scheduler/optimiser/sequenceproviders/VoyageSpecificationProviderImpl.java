/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;

@NonNullByDefault
@NotInjectedScope
public class VoyageSpecificationProviderImpl implements IVoyageSpecificationProvider {

	private Map<IPortSlot, ERouteOption> voyageRouteOption = new HashMap<>();
	private Map<IPortSlot, TravelFuelChoice> fuelChoiceOption = new HashMap<>();
	private Map<IPortSlot, Integer> arrivalTimes = new HashMap<>();

	@Override
	public @Nullable ERouteOption getVoyageRouteOption(IPortSlot from, IPortSlot to) {
		return voyageRouteOption.get(from);
	}

	public void setVoyageRouteOption(IPortSlot from, ERouteOption option) {
		voyageRouteOption.put(from, option);
	}

	@Override
	public @Nullable TravelFuelChoice getFuelChoice(IPortSlot from, IPortSlot to) {
		return fuelChoiceOption.get(from);
	}

	public void setFuelChoice(IPortSlot from, TravelFuelChoice option) {
		fuelChoiceOption.put(from, option);
	}
	
	@Override
	public @Nullable Integer getArrivalTime(IPortSlot slot) {
		return arrivalTimes.get(slot);
	}
	
	public void setArrivalTime(IPortSlot slot, int time) {
		arrivalTimes.put(slot, time);
	}

}
