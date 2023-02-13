/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class SalesContractAllocationRow extends AllocationRow {

	private final SalesContract salesContract;
	
	public SalesContractAllocationRow(final com.mmxlabs.models.lng.adp.SalesContractAllocationRow eSalesContractAllocationRow) {
		super(eSalesContractAllocationRow);
		@Nullable
		final SalesContract pSalesContract = eSalesContractAllocationRow.getContract();
		if (pSalesContract == null) {
			throw new IllegalStateException("Sales contract must not be null");
		}
		this.salesContract = pSalesContract;
		validateConstruction();
	}

	public SalesContractAllocationRow(final SalesContract salesContract, final int weight, final List<Vessel> vessels) {
		super(weight, vessels);
		this.salesContract = salesContract;
		this.validateConstruction();
	}

	public SalesContract getSalesContract() {
		return salesContract;
	}

}
