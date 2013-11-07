package com.mmxlabs.models.lng.transformer.extensions.redirection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IVoyageCostCalculator {

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int dischargeTime, @NonNull IVessel vessel, int notionalBallastSpeed, int cargoCVValue,
			@NonNull String route, int baseFuelPricePerMT, @NonNull ISalesPriceCalculator salesPrice);

	@Nullable
	VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int dischargeTime, @NonNull IVessel vessel, int notionalBallastSpeed, int cargoCVValue,
			@NonNull String route, int baseFuelPricePerMT, int salesPricePerMMBTu);
}
