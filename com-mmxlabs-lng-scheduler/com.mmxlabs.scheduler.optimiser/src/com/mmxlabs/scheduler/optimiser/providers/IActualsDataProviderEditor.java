package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * 
 * @author Simon Goodall
 * 
 */
public interface IActualsDataProviderEditor extends IActualsDataProvider {

	void createLoadSlotActuals(ILoadOption slot, int arrivalTime, int visitDuration, long portCosts, int cargoCV, long startHeelInM3, long lngLoadVolumeInM3, long lngLoadVolumeInMMBTu,
			int purchasePricePerMMBTu, long ladenBaseFuelConsumptionInMT, int baseFuelPricePerMT, int charterRatePerDay, int ladenDistance, long ladenRouteCosts);

	void createDischargeSlotActuals(IDischargeOption slot, int arrivalTime, int visitDuration, long portCosts, int cargoCV, long endHeelInM3, long lngDischargeVolumeInM3,
			long lngDischargeVolumeInMMBTu, int salesPricePerMMBTu, long ballastBaseFuelConsumptionInMT, int ballastDistance, long ballastRouteCosts);

}
