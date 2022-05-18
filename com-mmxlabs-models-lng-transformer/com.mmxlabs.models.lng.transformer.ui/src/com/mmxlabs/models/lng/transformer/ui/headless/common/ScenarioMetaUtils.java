/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ScenarioMetaUtils {
	
	private static final int HOURS_IN_DAY = 24;
	private static final int DAYS_IN_WEEK = 7;
	private static final int WEEKS_IN_MONTH = 4;


	public static Map<String, Integer> calculateActiveSpotMarkets(SpotMarketsModel spotMarketsModel) {
		List<SpotMarketGroup> spotMarkets = getSpotMarkets(spotMarketsModel);
		
		Map<String, Integer> spotMarketsActive = new HashMap<>();
		for (SpotMarketGroup marketGroup : spotMarkets) {
			//get markets from market group
			List<SpotMarket> spotsPerGroup = marketGroup.getMarkets();
			//keep counter of available spots
			int numAvailable = 0;
			
			for (SpotMarket market : spotsPerGroup) {
				numAvailable += market.isEnabled() ? market.getAvailability().getConstant() : 0;
			}
				
			//add number of availabilities and type of market to map
			spotMarketsActive.put(marketGroup.getType().getName(), numAvailable);
		}
	
		return spotMarketsActive;
	}
	
	public static int calculateShippedCargoes(List<Cargo> cargoes) {
		int numShippedCargoes = 0;
		for (Cargo cargo : cargoes) {
			List<Slot<?>> slots = cargo.getSortedSlots();
			// Assume: there is exactly one load and discharge each
			assert slots.size() == 2;
			
			LoadSlot loadSlot = (LoadSlot) slots.get(0);
			DischargeSlot dischargeSlot = (DischargeSlot) slots.get(1);
			
			if (!loadSlot.isDESPurchase() && !dischargeSlot.isFOBSale()) {
				numShippedCargoes++;
			}
		}
		
		return numShippedCargoes;
	}
	
	public static Map<String, Integer> calculateVesselEvents(List<VesselEvent> vesselEvents) {
		Map<String, Integer> vesselEventCounts = new HashMap<>(); 
		vesselEvents.forEach(event -> vesselEventCounts.merge(event.getClass().getSimpleName(), 1, Integer::sum));
		
		return vesselEventCounts;
	}
	
	public static int calculateCharterOutSpots(SpotMarketsModel spotMarketsModel) {
		int charterOutSpots = 0;
		List<CharterOutMarket> charterOutMarkets = spotMarketsModel.getCharterOutMarkets();
		
		for (CharterOutMarket market : charterOutMarkets) {
			charterOutSpots += market.isEnabled() ? 1 : 0;
		}
			
		return charterOutSpots;
	}
	
	public static int calculateCharterInSpots(SpotMarketsModel spotMarketsModel) {
		int charterInSpots = 0;
		List<CharterInMarket> charterInMarkets = spotMarketsModel.getCharterInMarkets();
				
		for (CharterInMarket market : charterInMarkets) {
			charterInSpots += market.isEnabled() ? 1 : 0;
		}
			
		return charterInSpots;
	}
	
	private static Map<Integer, Integer> calculateSlotDurations(List<Slot<?>> slots) {
		Map<Integer, Integer> durations = new HashMap<>(); 
		slots.forEach(slot -> durations.merge(slot.getSchedulingTimeWindow().getDuration(),
				1, Integer::sum));
		
		return durations;
	}
	
	private static Map<String, Integer> calculateSlotWindows(List<Slot<?>> slots) {
		List<Integer> times = slots.stream().
				map(Slot::getSchedulingTimeWindow).
				map(SchedulingTimeWindow::getSizeInHours).
				toList();
		
		return bucketByHours(times);
	}
	
	private static List<Integer> calculateMinTimeWindows(List<VesselAvailability> availabilities) {
		return availabilities.stream().map(VesselAvailability::getCharterOrDelegateMinDuration).toList();
	}
	
	private static List<Integer> calculateMaxTimeWindows(List<VesselAvailability> availabilities) {
		return availabilities.stream().map(VesselAvailability::getCharterOrDelegateMaxDuration).toList();
	}
	
	public static ScenarioMeta writeOptimisationMetrics(IScenarioDataProvider scenarioDataProvider, 
			UserSettings userSettings) {
		ScenarioMeta scenarioMeta = new ScenarioMeta();
		
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider); 
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);

		List<Cargo> cargoes = cargoModel.getCargoes();
		List<VesselAvailability> availabilities = cargoModel.getVesselAvailabilities();
		List<Slot<?>> loadSlots = new ArrayList<>(cargoModel.getLoadSlots());
		List<Slot<?>> dischargeSlots = new ArrayList<>(cargoModel.getDischargeSlots());
		
		scenarioMeta.setNumCargoes(cargoes.size());
		scenarioMeta.setNumVessels(availabilities.size());
		scenarioMeta.setNumLoads(loadSlots.size());
		scenarioMeta.setNumDischarges(dischargeSlots.size());
		scenarioMeta.setNumSalesContracts(commercialModel.getSalesContracts().size());
		scenarioMeta.setNumPurchaseContracts(commercialModel.getPurchaseContracts().size());
		scenarioMeta.setShippedCargoes(calculateShippedCargoes(cargoes));
		scenarioMeta.setVesselEvents(calculateVesselEvents(cargoModel.getVesselEvents()));	
		scenarioMeta.setCharterInSpotsCount(calculateCharterInSpots(spotMarketsModel));
		
		if (userSettings != null) {
			if(userSettings.isWithSpotCargoMarkets()) {
				scenarioMeta.setSpotMarketOptions(calculateActiveSpotMarkets(spotMarketsModel));
			}
			
			if(userSettings.isGenerateCharterOuts()) {
				scenarioMeta.setCharterOutSpotsCount(calculateCharterOutSpots(spotMarketsModel));
			}
		}
		
		scenarioMeta.setMinTimeWindows(calculateMinTimeWindows(availabilities));
		scenarioMeta.setMaxTimeWindows(calculateMaxTimeWindows(availabilities));
		
		scenarioMeta.setLoadTimeWindow(calculateSlotWindows(loadSlots));
		scenarioMeta.setDischargeTimeWindow(calculateSlotWindows(dischargeSlots));
		scenarioMeta.setLoadSlotDuration(calculateSlotDurations(loadSlots));
		scenarioMeta.setDischargeSlotDuration(calculateSlotDurations(dischargeSlots));
		
		return scenarioMeta;
	}
	
	private static List<SpotMarketGroup> getSpotMarkets(SpotMarketsModel spotMarketsModel) {
		List<SpotMarketGroup> spotMarkets = new ArrayList<>();
		spotMarkets.add(spotMarketsModel.getDesPurchaseSpotMarket());
		spotMarkets.add(spotMarketsModel.getDesSalesSpotMarket());
		spotMarkets.add(spotMarketsModel.getFobPurchasesSpotMarket());
		spotMarkets.add(spotMarketsModel.getFobSalesSpotMarket());
		
		return spotMarkets;
	}
	
	private static Map<String, Integer> bucketByHours(List<Integer> times) {
		Map<String, Integer> timeBuckets = new HashMap<>();
		
		for (Integer hours : times) {
			if (hours < HOURS_IN_DAY) {
				timeBuckets.merge(hours + " hours", 1, Integer::sum);
				continue;
			}
			
			double days = (double) hours / HOURS_IN_DAY;
			if (days < DAYS_IN_WEEK) {
				timeBuckets.merge((int)(Math.ceil(days)) + " days", 1, Integer::sum);
				continue;
			}
			
			double weeks = days / DAYS_IN_WEEK;
			if (weeks < WEEKS_IN_MONTH) {
				timeBuckets.merge((int)(Math.ceil(weeks)) + " weeks", 1, Integer::sum);
				continue;
			}
			
			double months = weeks / WEEKS_IN_MONTH;
			timeBuckets.merge((int)(Math.ceil(months)) + " months", 1, Integer::sum);
		}
		
		return timeBuckets;
	}
}
