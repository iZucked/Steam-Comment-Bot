/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.transfers;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store the TransfersModel data in basic format
 * @author FM
 *
 */
public class TransfersLookupData {
	// List of all agreements
	public List<BasicTransferAgreement> agreements = new ArrayList<>();
	// List of all records
	public List<BasicTransferRecord> records = new ArrayList<>();
	
	public TransfersLookupData(List<BasicTransferAgreement> agreements, List<BasicTransferRecord> records) {
		super();
		this.agreements = agreements;
		this.records = records;
	}
}
