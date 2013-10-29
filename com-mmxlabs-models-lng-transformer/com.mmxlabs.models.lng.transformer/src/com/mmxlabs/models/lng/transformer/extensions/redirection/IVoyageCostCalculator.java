package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IVoyageCostCalculator {

	VoyagePlan calculateShippingCosts(IPort loadPort, IPort dischargePort,
			int loadTime, int dischargeTime, IVessel vessel, int notionalSpeed,
			int cargoCVValue, String route, ISalesPriceCalculator salesPrice);
}
