/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;

public class PartialCaseRowMaker {
	@NonNull
	protected final SandboxModelBuilder builder;

	protected PartialCaseRow row = null;

	public PartialCaseRowMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
	}

	public @NonNull PartialCaseRow build() {
		this.builder.getOptionAnalysisModel().getPartialCase().getPartialCase().add(row);
		return row;
	}

	public @NonNull PartialCaseRowMaker withBuyOptions(BuyOption... options) {
		for (BuyOption opt : options) {
			row.getBuyOptions().add(opt);
		}
		return this;
	}

	public @NonNull PartialCaseRowMaker withSellOptions(SellOption... options) {
		for (SellOption opt : options) {
			row.getSellOptions().add(opt);
		}
		return this;
	}

	public @NonNull PartialCaseRowMaker withVesselEventOptions(VesselEventOption... options) {
		for (VesselEventOption opt : options) {
			row.getVesselEventOptions().add(opt);
		}
		return this;
	}

	public @NonNull PartialCaseRowMaker withShippingOptions(ShippingOption... options) {
		for (ShippingOption opt : options) {
			row.getShipping().add(opt);
		}
		return this;
	}

}
