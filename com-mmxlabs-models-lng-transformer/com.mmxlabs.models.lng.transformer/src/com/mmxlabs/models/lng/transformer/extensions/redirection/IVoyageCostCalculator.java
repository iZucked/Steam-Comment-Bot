package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IVoyageCostCalculator {

	VoyagePlan calculateShippingCosts(final ILoadPriceCalculator loadPriceCalculator, final IPort loadPort, final IPort dischargePort, final int loadTime, final long loadVolumeInM3,
			final IVessel vessel, final int cargoCVValue, final String route);

}
