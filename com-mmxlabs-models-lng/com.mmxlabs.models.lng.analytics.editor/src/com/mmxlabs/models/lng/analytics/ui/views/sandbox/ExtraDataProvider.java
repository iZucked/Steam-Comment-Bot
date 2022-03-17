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
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class ExtraDataProvider {
	public final List<VesselAvailability> extraVesselAvailabilities;
	public final List<CharterInMarketOverride> extraCharterInMarketOverrides;
	public final List<CharterInMarket> extraCharterInMarkets;

	public final List<LoadSlot> extraLoads;
	public final List<DischargeSlot> extraDischarges;
	public final List<VesselEvent> extraVesselEvents;
	public final List<SpotMarket> extraSpotCargoMarkets;

	// TODO: These are coped from LNGScenarioTransformer.
	public static final String EXTRA_CHARTER_IN_MARKET_OVERRIDES = "extra_charter_in_market_overrides";
	public static final String EXTRA_CHARTER_IN_MARKETS = "extra_charter_in_markets";
	public static final String EXTRA_SPOT_CARGO_MARKETS = "extra_spot_cargo_markets";
	public static final String EXTRA_VESSEL_AVAILABILITIES = "extra_vessel_availabilities";
	public static final String EXTRA_VESSEL_EVENTS = "extra_vessel_events";
	public static final String EXTRA_LOAD_SLOTS = "extra_load_slots";
	public static final String EXTRA_DISCHARGE_SLOTS = "extra_discharge_slots";

	public ExtraDataProvider() {
		this.extraVesselAvailabilities = new LinkedList<>();
		this.extraCharterInMarkets = new LinkedList<>();
		this.extraCharterInMarketOverrides = new LinkedList<>();
		this.extraLoads = new LinkedList<>();
		this.extraDischarges = new LinkedList<>();
		this.extraVesselEvents = new LinkedList<>();
		this.extraSpotCargoMarkets = new LinkedList<>();
	}

	public ExtraDataProvider(List<VesselAvailability> newAvailabilities, List<CharterInMarket> newCharterInMarkets, List<CharterInMarketOverride> newCharterInMarketOverrides,
			List<LoadSlot> extraLoads, List<DischargeSlot> extraDischarges, List<VesselEvent> extraVesselEvents, List<SpotMarket> extraSpotCargoMarkets) {
		this.extraVesselAvailabilities = newAvailabilities;
		this.extraCharterInMarkets = newCharterInMarkets;
		this.extraCharterInMarketOverrides = newCharterInMarketOverrides;
		this.extraLoads = extraLoads;
		this.extraDischarges = extraDischarges;
		this.extraVesselEvents = extraVesselEvents;
		this.extraSpotCargoMarkets = extraSpotCargoMarkets;

	}

	public synchronized void merge(@Nullable AbstractSolutionSet solutionSet) {
		if (solutionSet != null) {
			solutionSet.getExtraSlots().stream().filter(s -> s instanceof LoadSlot).forEach(s -> extraLoads.add((LoadSlot) s));
			solutionSet.getExtraSlots().stream().filter(s -> s instanceof DischargeSlot).forEach(s -> extraDischarges.add((DischargeSlot) s));

			extraVesselAvailabilities.addAll(solutionSet.getExtraVesselAvailabilities());
			extraCharterInMarkets.addAll(solutionSet.getExtraCharterInMarkets());
			extraCharterInMarketOverrides.addAll(solutionSet.getCharterInMarketOverrides());
			extraVesselEvents.addAll(solutionSet.getExtraVesselEvents());
			// extraSpotCargoMarkets.addAll(solutionSet.getExtraVesselEvents());
		}
	}

	public synchronized void merge(ExtraDataProvider extraDataProvider) {

		// Null Check
		if (extraDataProvider.extraVesselAvailabilities != null) {
			extraVesselAvailabilities.addAll(extraDataProvider.extraVesselAvailabilities);
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
	}

	public com.google.inject.Module asModule() {
		return new AbstractModule() {

			@Override
			protected void configure() {
			}

			@Provides
			@Named(EXTRA_VESSEL_AVAILABILITIES)
			private List<VesselAvailability> provideExtraAvailabilities() {
				return extraVesselAvailabilities;
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
		};
	}

	public static com.google.inject.Module createDefaultModule() {
		return new ExtraDataProvider().asModule();
	}
}