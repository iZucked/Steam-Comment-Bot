/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public interface IMapperClass {

	VesselEvent getOriginal(VesselEventOption opt);
	
	LoadSlot getOriginal(BuyOption buy);

	LoadSlot getBreakEven(BuyOption buy);

	LoadSlot getChangable(BuyOption buy);

	DischargeSlot getOriginal(SellOption sell);

	DischargeSlot getBreakEven(SellOption sell);

	DischargeSlot getChangable(SellOption sell);

	void addMapping(VesselEventOption opt, VesselEvent original);

	void addMapping(CommodityCurveOverlay overlay);

	void addMapping(BuyOption buy, LoadSlot original, LoadSlot breakEven, LoadSlot changable);

	void addMapping(SellOption sell, DischargeSlot original, DischargeSlot breakEven, DischargeSlot changable);

	void addMapping(SpotMarket market, YearMonth date, LoadSlot slot_original, LoadSlot slot_breakEven, LoadSlot slot_changable);

	void addMapping(SpotMarket market, YearMonth date, DischargeSlot slot_original, DischargeSlot slot_breakEven, DischargeSlot slot_changable);

	DischargeSlot getSalesMarketBreakEven(SpotMarket market, @NonNull YearMonth from);

	DischargeSlot getSalesMarketChangable(SpotMarket market, @NonNull YearMonth date);

	DischargeSlot getSalesMarketOriginal(SpotMarket market, @NonNull YearMonth date);

	LoadSlot getPurchaseMarketBreakEven(SpotMarket market, @NonNull YearMonth date);

	LoadSlot getPurchaseMarketChangable(SpotMarket market, @NonNull YearMonth date);

	LoadSlot getPurchaseMarketOriginal(SpotMarket market, @NonNull YearMonth date);

	ExtraDataProvider getExtraDataProvider();

	void addMapping(RoundTripShippingOption shippingOption, CharterInMarket newMarket);

	void addMapping(SimpleVesselCharterOption shippingOption, VesselCharter vesselCharter);

	void addMapping(ExistingVesselCharterOption shippingOption, VesselCharter vesselCharter);

	void addMapping(FullVesselCharterOption shippingOption, VesselCharter vesselCharter);
	
	void addMapping(ExistingCharterMarketOption shippingOption, CharterInMarket newMarket);

	VesselCharter get(SimpleVesselCharterOption fleetShippingOption);

	CharterInMarket get(RoundTripShippingOption roundTripShippingOption);
	
	CharterInMarket get(ExistingCharterMarketOption existingCharterMarketOption);

	boolean isCreateBEOptions();
}
