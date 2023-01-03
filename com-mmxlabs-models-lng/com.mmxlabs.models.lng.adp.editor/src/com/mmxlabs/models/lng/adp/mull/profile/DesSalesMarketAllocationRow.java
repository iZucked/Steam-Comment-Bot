/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

@NonNullByDefault
public class DesSalesMarketAllocationRow extends AllocationRow {

	private final DESSalesMarket desSalesMarket;

	public DesSalesMarketAllocationRow(final DESSalesMarketAllocationRow eDesSalesMarketAllocationRow) {
		super(eDesSalesMarketAllocationRow);
		
		@Nullable
		final DESSalesMarket pDesSalesMarket = eDesSalesMarketAllocationRow.getDesSalesMarket();
		if (pDesSalesMarket == null) {
			throw new IllegalStateException("DES sales market must not be null");
		}
		this.desSalesMarket = pDesSalesMarket;
		validateConstruction();
	}

	public DesSalesMarketAllocationRow(final DESSalesMarket desSalesMarket, final int weight, final List<Vessel> vessels) {
		super(weight, vessels);
		this.desSalesMarket = desSalesMarket;
		validateConstruction();
	}

	public DESSalesMarket getDesSalesMarket() {
		return desSalesMarket;
	}
}
