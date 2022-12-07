/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public final class MarketabilityUtils {
	public static MarketabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name) {
		final MarketabilityModel model = AnalyticsFactory.eINSTANCE.createMarketabilityModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);

//		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
//			final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
//			buy.setSlot(slot);
//			model.getBuys().add(buy);
//		}
		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null && spotMarket.isEnabled()) {
						model.getMarkets().add(spotMarket);
				}
			}
		}
		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
		if (smgFS != null) {
			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
				if (spotMarket != null && spotMarket.isEnabled()) {
						model.getMarkets().add(spotMarket);
				}
			}
		}
		cargoModel.getLoadSlots().stream().forEach( loadSlot -> {
			Cargo c = loadSlot.getCargo();
			if(c != null && c.getVesselAssignmentType() instanceof VesselCharter vesselCharter) {
				final ExistingVesselCharterOption evco = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
				evco.setVesselCharter(vesselCharter);
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(loadSlot);
				model.getBuys().add(buy);
				model.getShippingTemplates().add(evco);
				
				final MarketabilityRow row = AnalyticsFactory.eINSTANCE.createMarketabilityRow();
				row.setBuyOption(buy);
				row.setShipping(evco);
				model.getRows().add(row);
			}
		});
//		populateModel(model);
		return model;
	}
	
	public static void populateModel(final @NonNull MarketabilityModel model) {
		
		for (final BuyOption bo : model.getBuys()) {
			for (final ShippingOption so : model.getShippingTemplates()) {
				final MarketabilityRow row = AnalyticsFactory.eINSTANCE.createMarketabilityRow();
				row.setBuyOption(bo);
				row.setShipping(so);
				model.getRows().add(row);
			}
		}
	}
	private MarketabilityUtils() {}
}
