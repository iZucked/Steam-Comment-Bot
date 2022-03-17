/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScope;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;

@NonNullByDefault
@NotInjectedScope
public interface IVoyageSpecificationProvider {

	@Nullable
	ERouteOption getVoyageRouteOption(IPortSlot from, IPortSlot to);

	@Nullable
	TravelFuelChoice getFuelChoice(IPortSlot from, IPortSlot to);
	
	@Nullable
	Integer getArrivalTime(IPortSlot slot);
}
