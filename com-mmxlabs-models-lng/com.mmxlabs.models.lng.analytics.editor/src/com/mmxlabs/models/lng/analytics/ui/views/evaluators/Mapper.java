/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NewVesselAvailability;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class Mapper implements IMapperClass {
	private LNGScenarioModel scenarioModel;

	private Map<BuyOption, LoadSlot> buyMap_orig = new HashMap<>();
	private Map<BuyOption, LoadSlot> buyMap_be = new HashMap<>();
	private Map<BuyOption, LoadSlot> buyMap_change = new HashMap<>();
	private Map<SellOption, DischargeSlot> sellMap_orig = new HashMap<>();
	private Map<SellOption, DischargeSlot> sellMap_be = new HashMap<>();
	private Map<SellOption, DischargeSlot> sellMap_change = new HashMap<>();

	private Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_original = new HashMap<>();
	private Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_be = new HashMap<>();
	private Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_change = new HashMap<>();

	private Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_original = new HashMap<>();
	private Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_be = new HashMap<>();
	private Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_change = new HashMap<>();

	private Set<LoadSlot> extraLoads = new LinkedHashSet<>();
	private Set<DischargeSlot> extraDischarges = new LinkedHashSet<>();
	private Set<CharterInMarket> extraCharterInMarkets = new LinkedHashSet<>();
	private Set<VesselAvailability> extraVesselAvailabilities = new LinkedHashSet<>();

	Map<ShippingOption, VesselAvailability> fleetOptionMap = new HashMap<>();
	Map<RoundTripShippingOption, CharterInMarket> roundTripOptionMap = new HashMap<>();
	Map<ExistingCharterMarketOption, CharterInMarket> charterMarketOptionMap = new HashMap<>();

	public Mapper(LNGScenarioModel scenarioModel) {
		this.scenarioModel = scenarioModel;
	}

	@Override
	public LoadSlot getOriginal(final BuyOption buy) {
		return buyMap_orig.get(buy);
	}

	@Override
	public LoadSlot getBreakEven(final BuyOption buy) {
		return buyMap_be.get(buy);
	}

	@Override
	public LoadSlot getChangable(final BuyOption buy) {
		return buyMap_change.get(buy);
	}

	@Override
	public DischargeSlot getOriginal(final SellOption sell) {
		return sellMap_orig.get(sell);
	}

	@Override
	public DischargeSlot getBreakEven(final SellOption sell) {
		return sellMap_be.get(sell);
	}

	@Override
	public DischargeSlot getChangable(final SellOption sell) {
		return sellMap_change.get(sell);
	}

	@Override
	public void addMapping(final BuyOption buy, final LoadSlot original, final LoadSlot breakeven, final LoadSlot changable) {
		if (original != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(original)) {
			extraLoads.add(original);
		}

		if (breakeven != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(breakeven)) {
			extraLoads.add(breakeven);
		}

		if (changable != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(changable)) {
			extraLoads.add(changable);
		}

		buyMap_orig.put(buy, original);
		buyMap_be.put(buy, breakeven);
		buyMap_change.put(buy, changable);
	}

	@Override
	public void addMapping(final SellOption sell, final DischargeSlot original, final DischargeSlot breakeven, final DischargeSlot changable) {

		if (original != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(original)) {
			extraDischarges.add(original);
		}

		if (breakeven != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(breakeven)) {
			extraDischarges.add(breakeven);
		}

		if (changable != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(changable)) {
			extraDischarges.add(changable);
		}

		sellMap_orig.put(sell, original);
		sellMap_be.put(sell, breakeven);
		sellMap_change.put(sell, changable);

	}

	@Override
	public void addMapping(SpotMarket market, YearMonth date, final LoadSlot original, LoadSlot slot_breakEven, LoadSlot slot_changable) {

		Pair<YearMonth, SpotMarket> key = new Pair<>(date, market);

		buyMarketMap_original.put(key, original);
		buyMarketMap_be.put(key, slot_breakEven);
		buyMarketMap_change.put(key, slot_changable);

		if (original != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(original)) {
			extraLoads.add(original);
		}

		if (slot_breakEven != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(slot_breakEven)) {
			extraLoads.add(slot_breakEven);
		}

		if (slot_changable != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().contains(slot_changable)) {
			extraLoads.add(slot_changable);
		}
	}

	@Override
	public void addMapping(SpotMarket market, YearMonth date, final DischargeSlot original, DischargeSlot slot_breakEven, DischargeSlot slot_changable) {
		Pair<YearMonth, SpotMarket> key = new Pair<>(date, market);

		sellMarketMap_original.put(key, original);
		sellMarketMap_be.put(key, slot_breakEven);
		sellMarketMap_change.put(key, slot_changable);

		if (original != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(original)) {
			extraDischarges.add(original);
		}

		if (slot_breakEven != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(slot_breakEven)) {
			extraDischarges.add(slot_breakEven);
		}

		if (slot_changable != null && !ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().contains(slot_changable)) {
			extraDischarges.add(slot_changable);
		}
	}

	@Override
	public DischargeSlot getSalesMarketBreakEven(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_be.get(new Pair<>(date, market));
	}

	@Override
	public DischargeSlot getSalesMarketChangable(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_change.get(new Pair<>(date, market));
	}

	@Override
	public LoadSlot getPurchaseMarketBreakEven(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_be.get(new Pair<>(date, market));
	}

	@Override
	public LoadSlot getPurchaseMarketChangable(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_change.get(new Pair<>(date, market));
	}

	@Override
	public DischargeSlot getSalesMarketOriginal(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_original.get(new Pair<>(date, market));

	}

	@Override
	public LoadSlot getPurchaseMarketOriginal(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_original.get(new Pair<>(date, market));

	}

	@Override
	public CharterInMarket get(RoundTripShippingOption roundTripShippingOption) {
		return roundTripOptionMap.get(roundTripShippingOption);
	}

	@Override
	public VesselAvailability get(FleetShippingOption fleetShippingOption) {
		return fleetOptionMap.get(fleetShippingOption);
	}

	@Override
	public void addMapping(RoundTripShippingOption roundTripShippingOption, CharterInMarket newMarket) {
		if (!ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterInMarkets().contains(newMarket)) {
			extraCharterInMarkets.add(newMarket);
		}
		roundTripOptionMap.put(roundTripShippingOption, newMarket);
	}

	@Override
	public void addMapping(FleetShippingOption fleetShippingOption, VesselAvailability vesselAvailability) {
		if (!ScenarioModelUtil.getCargoModel(scenarioModel).getVesselAvailabilities().contains(vesselAvailability)) {
			extraVesselAvailabilities.add(vesselAvailability);
		}
		fleetOptionMap.put(fleetShippingOption, vesselAvailability);
	}

	@Override
	public void addMapping(ExistingVesselAvailability shippingOption, VesselAvailability vesselAvailability) {
		if (!ScenarioModelUtil.getCargoModel(scenarioModel).getVesselAvailabilities().contains(vesselAvailability)) {
			extraVesselAvailabilities.add(vesselAvailability);
		}
		fleetOptionMap.put(shippingOption, vesselAvailability);
	}

	@Override
	public void addMapping(NewVesselAvailability shippingOption, VesselAvailability vesselAvailability) {
		if (!ScenarioModelUtil.getCargoModel(scenarioModel).getVesselAvailabilities().contains(vesselAvailability)) {
			extraVesselAvailabilities.add(vesselAvailability);
		}
		fleetOptionMap.put(shippingOption, vesselAvailability);
	}

	@Override
	public ExtraDataProvider getExtraDataProvider() {
		return new ExtraDataProvider(new ArrayList<>(extraVesselAvailabilities), new ArrayList<>(extraCharterInMarkets), Collections.emptyList(), new ArrayList<>(extraLoads),
				new ArrayList<>(extraDischarges));
	}

	@Override
	public void addMapping(ExistingCharterMarketOption shippingOption, CharterInMarket newMarket) {
		if (!ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterInMarkets().contains(newMarket)) {
			extraCharterInMarkets.add(newMarket);
		}
		charterMarketOptionMap.put(shippingOption, newMarket);
	}

	@Override
	public CharterInMarket get(ExistingCharterMarketOption existingCharterMarketOption) {
		return charterMarketOptionMap.get(existingCharterMarketOption);
	}
}
