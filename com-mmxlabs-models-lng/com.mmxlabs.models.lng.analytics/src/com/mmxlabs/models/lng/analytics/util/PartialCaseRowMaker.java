/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.port.RouteOption;

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

	public @NonNull PartialCaseRowMaker withLadenRouteOptions(RouteOption... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getLadenRoutes().clear();
		for (var opt : options) {
			row.getOptions().getLadenRoutes().add(opt);
		}

		return this;
	}

	public @NonNull PartialCaseRowMaker withBallastRouteOptions(RouteOption... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getBallastRoutes().clear();
		for (var opt : options) {
			row.getOptions().getBallastRoutes().add(opt);
		}

		return this;
	}

	public @NonNull PartialCaseRowMaker withLadenFuelChoices(FuelChoice... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getLadenFuelChoices().clear();
		for (var opt : options) {
			row.getOptions().getLadenFuelChoices().add(opt);
		}

		return this;
	}

	public @NonNull PartialCaseRowMaker withBallastFuelChoices(FuelChoice... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getBallastFuelChoices().clear();
		for (var opt : options) {
			row.getOptions().getBallastFuelChoices().add(opt);
		}

		return this;

	}

	public @NonNull PartialCaseRowMaker withLoadDates(LocalDateTime... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getLoadDates().clear();
		for (var opt : options) {
			LocalDateTimeHolder h = AnalyticsFactory.eINSTANCE.createLocalDateTimeHolder();
			h.setDateTime(opt);
			row.getOptions().getLoadDates().add(h);
		}

		return this;
	}

	public @NonNull PartialCaseRowMaker withDischargeDates(LocalDateTime... options) {

		if (row.getOptions() == null) {
			row.setOptions(AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions());
		}
		row.getOptions().getDischargeDates().clear();
		for (var opt : options) {
			LocalDateTimeHolder h = AnalyticsFactory.eINSTANCE.createLocalDateTimeHolder();
			h.setDateTime(opt);
			row.getOptions().getDischargeDates().add(h);
		}

		return this;
	}

}
