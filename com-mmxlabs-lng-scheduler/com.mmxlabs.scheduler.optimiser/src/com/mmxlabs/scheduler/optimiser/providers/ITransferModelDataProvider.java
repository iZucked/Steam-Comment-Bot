/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
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
}
