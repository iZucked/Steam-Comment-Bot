/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * 
 * @author Simon Goodall
 * 
 */
public interface IActualsDataProviderEditor extends IActualsDataProvider {

	void createLoadSlotActuals(ILoadOption slot, int arrivalTime, int visitDuration, long portCosts, int cargoCV, long startHeelInM3, long lngLoadVolumeInM3, long lngLoadVolumeInMMBTu,
			int purchasePricePerMMBTu, long portBaseFuelConsumptionInMT, long ladenBaseFuelConsumptionInMT, int baseFuelPricePerMT, long charterRatePerDay, int ladenDistance, long ladenRouteCosts,
			ERouteOption route);

	void createDischargeSlotActuals(IDischargeOption slot, int arrivalTime, int visitDuration, long portCosts, int cargoCV, long endHeelInM3, long lngDischargeVolumeInM3,
			long lngDischargeVolumeInMMBTu, int salesPricePerMMBTu, long portBaseFuelConsumptionInMT, long ballastBaseFuelConsumptionInMT, int ballastDistance, long ballastRouteCosts,
			ERouteOption route);

	void setNextDestinationActuals(IPortSlot lastSlot, IPort returnPort, int returnTime, long endHeelInM3);

}
