package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ITransferModelDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferRecord;
import com.mmxlabs.scheduler.optimiser.transfers.TransfersLookupData;

public class DefaultTransferModelDataProviderEditor implements ITransferModelDataProviderEditor {
	
	private TransfersLookupData lookupData;
	
	private Map<IPortSlot, List<BasicTransferRecord>> slotsToTransferRecords = new HashMap<>();

	@Override
	public boolean isSlotTransferred(IPortSlot slot) {
		return (slotsToTransferRecords.containsKey(slot));
	}

	@Override
	public List<BasicTransferRecord> getTransferRecordsForSlot(IPortSlot slot) {
		return slotsToTransferRecords.getOrDefault(slot, Collections.emptyList());
	}

	@Override
	public TransfersLookupData getTransferLookupData() {
		return this.lookupData;
	}

	/**
	 * Do NOT call before look up data is set!
	 */
	@Override
	public void reconsileIPortSlotWithLookupData(IPortSlot slot) {
		if (this.lookupData != null) {
			List<BasicTransferRecord> temp = new ArrayList<>();
			for(final BasicTransferRecord record : lookupData.records) {
				if (record.getSlot().equals(slot)) {
					temp.add(record);
				}
			}
			if (!temp.isEmpty() && !slotsToTransferRecords.containsKey(slot)) {
				slotsToTransferRecords.put(slot, temp);
			}
		} else {
			throw new IllegalStateException("Must initialise the Transfer Look Up Data before reconsinling it with the slots.");
		}
	}

	@Override
	public void setLookupData(TransfersLookupData lookupdata) {
		this.lookupData = lookupdata;
	}

}
