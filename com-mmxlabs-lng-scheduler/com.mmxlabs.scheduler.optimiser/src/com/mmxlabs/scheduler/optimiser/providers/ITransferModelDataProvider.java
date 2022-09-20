
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferRecord;
import com.mmxlabs.scheduler.optimiser.transfers.TransfersLookupData;

/**
 * Provides essential information for Transfer Annotation computation
 * @author FM
 *
 */
public interface ITransferModelDataProvider extends IDataComponentProvider {
	boolean isSlotTransferred(IPortSlot slot);
	
	List<BasicTransferRecord> getTransferRecordsForSlot(IPortSlot slot);
	
	TransfersLookupData getTransferLookupData();
	
	int getTransferPrice(String priceExpression, int pricingDate, int internalSalesPrice, boolean isBasis);
}
