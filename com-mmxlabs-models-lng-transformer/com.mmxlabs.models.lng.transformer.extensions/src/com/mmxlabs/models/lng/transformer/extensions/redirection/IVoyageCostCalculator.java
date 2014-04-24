/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IVoyageCostCalculator {

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration, @NonNull IVessel vessel,
			int vesselCharterInRatePerDay, long startHeelInM3, int notionalBallastSpeed, int cargoCVValue, @NonNull String route, int baseFuelPricePerMT, int salesPricePerMMBTu);

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration, int returnTime,
			@NonNull IVessel vessel, int vesselCharterInRatePerDay, long startHeelInM3, int cargoCVValue, @NonNull String route, int baseFuelPricePerMT, int salesPricePerMMBTu);
}
