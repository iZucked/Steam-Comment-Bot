package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.transfers.TransfersLookupData;

/**
 * Allows adding data into the transfer model data provider
 * @author FM
 *
 */
public interface ITransferModelDataProviderEditor extends ITransferModelDataProvider {
	void reconsileIPortSlotWithLookupData(IPortSlot slot);
	
	void setLookupData(TransfersLookupData lookupdata);
}
