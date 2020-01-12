/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IVoyageCostCalculator {

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration, 
			@NonNull final IVessel vessel, @NonNull ICharterCostCalculator charterCostCalculator, long startHeelInM3, int notionalBallastSpeed, int cargoCVValue, @NonNull ERouteOption route, int[] baseFuelPriceInMT,
			int salesPricePerMMBTu);

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration, int returnTime,
			@NonNull final IVessel vessel, @NonNull ICharterCostCalculator charterCostCalculator, long startHeelInM3, int cargoCVValue, @NonNull ERouteOption route, int[] baseFuelPricePerMT,
			int salesPricePerMMBTu);
}
