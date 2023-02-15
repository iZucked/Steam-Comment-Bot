/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement;
import com.mmxlabs.models.lng.analytics.MarketabilityEndEvent;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityResultContainer;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public final class MarketabilityUtils {
	public static MarketabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name, final Integer vesselSpeed) {
		final MarketabilityModel model = AnalyticsFactory.eINSTANCE.createMarketabilityModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);

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

		for (Cargo c : cargoModel.getCargoes()) {
			Slot<?> slot1 = c.getSortedSlots().get(0);
			Slot<?> slot2 = c.getSortedSlots().get(1);
			if (slot1 instanceof LoadSlot loadSlot && slot2 instanceof DischargeSlot dischargeSlot) {
				ShippingOption shippingOption = null;
				if (c.getVesselAssignmentType() instanceof VesselCharter vesselCharter) {
					final ExistingVesselCharterOption evco = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
					evco.setVesselCharter(vesselCharter);
					shippingOption = evco;
				} else if (c.getVesselAssignmentType() instanceof CharterInMarket charterInMarket) {
//					if (charterInMarket != null && charterInMarket.getVessel() != null && charterInMarket.isEnabled() && charterInMarket.getSpotCharterCount() > 0) {
//						final ExistingCharterMarketOption ecmo = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
//						ecmo.setCharterInMarket(charterInMarket);
//						ecmo.setSpotIndex(c.getSpotIndex());
//						shippingOption = ecmo;
//					}
				}
				if(shippingOption == null) {
					continue;
				}
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(loadSlot);
				model.getBuys().add(buy);

				final SellReference sell = AnalyticsFactory.eINSTANCE.createSellReference();
				sell.setSlot(dischargeSlot);
				model.getSells().add(sell);

				model.getShippingTemplates().add(shippingOption);
				final MarketabilityRow row = AnalyticsFactory.eINSTANCE.createMarketabilityRow();
				row.setBuyOption(buy);
				row.setSellOption(sell);
				row.setShipping(shippingOption);
				model.getRows().add(row);

				final MarketabilityResultContainer container = AnalyticsFactory.eINSTANCE.createMarketabilityResultContainer();

				row.setResult(container);
			}
		}
		if(vesselSpeed != null) {
			model.setVesselSpeed(vesselSpeed);
		} else {
			model.unsetVesselSpeed();
		}
		
		return model;
	}

	private MarketabilityUtils() {
	}
}
