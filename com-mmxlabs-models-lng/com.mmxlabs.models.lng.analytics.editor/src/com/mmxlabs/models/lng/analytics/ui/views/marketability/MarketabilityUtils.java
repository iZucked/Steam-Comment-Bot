/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public final class MarketabilityUtils {
	public static MarketabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name) {
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
		final Schedule schedule = ScenarioModelUtil.findSchedule(sm);
		for (Cargo c : cargoModel.getCargoes()) {
			Slot<?> slot1 = c.getSortedSlots().get(0);
			Slot<?> slot2 = c.getSortedSlots().get(1);
			if (slot1 instanceof LoadSlot loadSlot && slot2 instanceof DischargeSlot dischargeSlot && c.getVesselAssignmentType() instanceof VesselCharter vesselCharter) {

				final ExistingVesselCharterOption evco = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
				evco.setVesselCharter(vesselCharter);
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(loadSlot);
				model.getBuys().add(buy);

				final SellReference sell = AnalyticsFactory.eINSTANCE.createSellReference();
				sell.setSlot(dischargeSlot);
				model.getSells().add(sell);

				model.getShippingTemplates().add(evco);
				final MarketabilityRow row = AnalyticsFactory.eINSTANCE.createMarketabilityRow();
				row.setBuyOption(buy);
				row.setSellOption(sell);
				row.setShipping(evco);
				model.getRows().add(row);
				if (schedule != null) {
					addScheduleEventsToRow(row, loadSlot, dischargeSlot, schedule);
				}
			}
		}
		return model;
	}

	private static void addScheduleEventsToRow(final MarketabilityRow row, final LoadSlot load, final DischargeSlot discharge, final Schedule schedule) {

		SlotAllocation buySlotAllocation = schedule.getSlotAllocations().stream().filter(x -> x.getSlot() == load).findAny().get();
		row.setBuySlotAllocation(buySlotAllocation);

		SlotAllocation dischargeSlotAllocation = schedule.getSlotAllocations().stream().filter(x -> x.getSlot() == discharge).findAny().get();
		row.setSellSlotAllocation(dischargeSlotAllocation);
		Event nextEvent = dischargeSlotAllocation.getSlotVisit().getNextEvent();
		while (nextEvent != null) {
			if (nextEvent instanceof SlotVisit slotVisit) {
				row.setNextSlotVisit(slotVisit);
				return;
			}
			nextEvent = nextEvent.getNextEvent();
		}
		row.setNextSlotVisit(null);

	}

	private MarketabilityUtils() {
	}
}
