/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class ExtraDataProvider {
	public final List<VesselCharter> extraVesselCharters;
	public final List<CharterInMarketOverride> extraCharterInMarketOverrides;
	public final List<CharterInMarket> extraCharterInMarkets;

	public final List<LoadSlot> extraLoads;
	public final List<DischargeSlot> extraDischarges;
	public final List<VesselEvent> extraVesselEvents;
	public final List<SpotMarket> extraSpotCargoMarkets;
	public final List<CommodityCurveOverlay> extraPriceCurves;

	// TODO: These are coped from LNGScenarioTransformer.
	public static final String EXTRA_CHARTER_IN_MARKET_OVERRIDES = "extra_charter_in_market_overrides";
	public static final String EXTRA_CHARTER_IN_MARKETS = "extra_charter_in_markets";
	public static final String EXTRA_SPOT_CARGO_MARKETS = "extra_spot_cargo_markets";
	public static final String EXTRA_VESSEL_AVAILABILITIES = "extra_vessel_availabilities";
	public static final String EXTRA_VESSEL_EVENTS = "extra_vessel_events";
	public static final String EXTRA_LOAD_SLOTS = "extra_load_slots";
	public static final String EXTRA_DISCHARGE_SLOTS = "extra_discharge_slots";
	public static final String EXTRA_PRICE_CURVES = "extra_price_curves";

	public ExtraDataProvider() {
		this.extraVesselCharters = new LinkedList<>();
		this.extraCharterInMarkets = new LinkedList<>();
		this.extraCharterInMarketOverrides = new LinkedList<>();
		this.extraLoads = new LinkedList<>();
		this.extraDischarges = new LinkedList<>();
		this.extraVesselEvents = new LinkedList<>();
		this.extraSpotCargoMarkets = new LinkedList<>();
		this.extraPriceCurves = new LinkedList<>();
	}

	public ExtraDataProvider(List<VesselCharter> newAvailabilities, List<CharterInMarket> newCharterInMarkets, List<CharterInMarketOverride> newCharterInMarketOverrides,
			List<LoadSlot> extraLoads, List<DischargeSlot> extraDischarges, List<VesselEvent> extraVesselEvents, List<SpotMarket> extraSpotCargoMarkets, List<CommodityCurveOverlay> extraCommodityCurves) {
		this.extraVesselCharters = newAvailabilities;
		this.extraCharterInMarkets = newCharterInMarkets;
		this.extraCharterInMarketOverrides = newCharterInMarketOverrides;
		this.extraLoads = extraLoads;
		this.extraDischarges = extraDischarges;
		this.extraVesselEvents = extraVesselEvents;
		this.extraSpotCargoMarkets = extraSpotCargoMarkets;
		this.extraPriceCurves = extraCommodityCurves;
	}

	public synchronized void merge(@Nullable AbstractSolutionSet solutionSet) {
		if (solutionSet != null) {
			solutionSet.getExtraSlots().stream().filter(s -> s instanceof LoadSlot).forEach(s -> extraLoads.add((LoadSlot) s));
			solutionSet.getExtraSlots().stream().filter(s -> s instanceof DischargeSlot).forEach(s -> extraDischarges.add((DischargeSlot) s));

			extraVesselCharters.addAll(solutionSet.getExtraVesselCharters());
			extraCharterInMarkets.addAll(solutionSet.getExtraCharterInMarkets());
			extraCharterInMarketOverrides.addAll(solutionSet.getCharterInMarketOverrides());
			extraVesselEvents.addAll(solutionSet.getExtraVesselEvents());
			// extraSpotCargoMarkets.addAll(solutionSet.getExtraVesselEvents());
		}
	}

	public synchronized void merge(ExtraDataProvider extraDataProvider) {

		// Null Check
		if (extraDataProvider.extraVesselCharters != null) {
			extraVesselCharters.addAll(extraDataProvider.extraVesselCharters);
		}
		if (extraDataProvider.extraCharterInMarkets != null) {
			extraCharterInMarkets.addAll(extraDataProvider.extraCharterInMarkets);
		}
		if (extraDataProvider.extraCharterInMarketOverrides != null) {
			extraCharterInMarketOverrides.addAll(extraDataProvider.extraCharterInMarketOverrides);
		}
		if (extraDataProvider.extraLoads != null) {
			extraLoads.addAll(extraDataProvider.extraLoads);
		}
		if (extraDataProvider.extraDischarges != null) {
			extraDischarges.addAll(extraDataProvider.extraDischarges);
		}
		if (extraDataProvider.extraVesselEvents != null) {
			extraVesselEvents.addAll(extraDataProvider.extraVesselEvents);
		}
		if (extraDataProvider.extraSpotCargoMarkets != null) {
			extraSpotCargoMarkets.addAll(extraDataProvider.extraSpotCargoMarkets);
		}
		if (extraDataProvider.extraPriceCurves != null) {
			extraPriceCurves.addAll(extraDataProvider.extraPriceCurves);
		}
	}

	public com.google.inject.Module asModule() {
		return new AbstractModule() {

			@Override
			protected void configure() {
			}

			@Provides
			@Named(EXTRA_VESSEL_AVAILABILITIES)
			private List<VesselCharter> provideExtraAvailabilities() {
				return extraVesselCharters;
			}

			@Provides
			@Named(EXTRA_CHARTER_IN_MARKET_OVERRIDES)
			private List<CharterInMarketOverride> provideCharterInMarketOverrides() {
				return extraCharterInMarketOverrides;
			}

			@Provides
			@Named(EXTRA_CHARTER_IN_MARKETS)
			private List<CharterInMarket> provideCharterInMarkets() {
				return extraCharterInMarkets;
			}

			@Provides
			@Named(EXTRA_LOAD_SLOTS)
			private List<LoadSlot> provideLoadSlots() {
				return extraLoads;
			}

			@Provides
			@Named(EXTRA_DISCHARGE_SLOTS)
			private List<DischargeSlot> provideDischargeSlots() {
				return extraDischarges;
			}

			@Provides
			@Named(EXTRA_VESSEL_EVENTS)
			private List<VesselEvent> provideVesselEvents() {
				return extraVesselEvents;
			}

			@Provides
			@Named(EXTRA_SPOT_CARGO_MARKETS)
			private List<SpotMarket> provideSpotCargoMarkets() {
				return extraSpotCargoMarkets;
			}

			@Provides
			@Named(EXTRA_PRICE_CURVES)
			private List<CommodityCurveOverlay> providePriceCurves() {
				return extraPriceCurves;
			}
 		};
	}

	public static com.google.inject.Module createDefaultModule() {
		return new ExtraDataProvider().asModule();
	}
}