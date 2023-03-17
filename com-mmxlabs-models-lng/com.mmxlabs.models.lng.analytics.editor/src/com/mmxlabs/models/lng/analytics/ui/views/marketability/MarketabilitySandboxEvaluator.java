/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class MarketabilitySandboxEvaluator {

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();

	private MarketabilitySandboxEvaluator() {
	}
	protected static Map<ShippingOption, VesselAssignmentType> buildFullScenario(final LNGScenarioModel clone, final MarketabilityModel clonedModel, final IMapperClass mapper) {
		final Map<ShippingOption, VesselAssignmentType> shippingMap = createShipping(clone, clonedModel, mapper);
		final Set<String> usedIDs = getUsedSlotIDs(clone);
		final Set<YearMonth> loadDates = new HashSet<>();
		for (final BuyOption buy : clonedModel.getBuys()) {
			final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.ORIGINAL_SLOT, usedIDs);
			final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.BREAK_EVEN_VARIANT, usedIDs);
			final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);
			mapper.addMapping(buy, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);

			final LocalDate windowStart = loadSlot_original.getWindowStart();
			loadDates.add(YearMonth.from(windowStart));
			loadDates.add(YearMonth.from(windowStart.plusMonths(1)));
			loadDates.add(YearMonth.from(windowStart.plusMonths(2)));
			loadDates.add(YearMonth.from(windowStart.plusMonths(3)));

		}

		final Set<YearMonth> dischargeDates = new HashSet<>();

		for (final SellOption sell : clonedModel.getSells()) {
			final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.ORIGINAL_SLOT, usedIDs);
			final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.BREAK_EVEN_VARIANT, usedIDs);
			final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);

			mapper.addMapping(sell, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);

			final LocalDate windowStart = dischargeSlot_original.getWindowStart();
			dischargeDates.add(YearMonth.from(windowStart));
			dischargeDates.add(YearMonth.from(windowStart.minusMonths(1)));
			dischargeDates.add(YearMonth.from(windowStart.minusMonths(2)));
			dischargeDates.add(YearMonth.from(windowStart.minusMonths(3)));
		}

		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(clone);
		for (final SpotMarket market : spotMarketsModel.getFobPurchasesSpotMarket().getMarkets()) {
			for (final YearMonth date : dischargeDates) {
				final BuyMarket m = AnalyticsFactory.eINSTANCE.createBuyMarket();
				m.setMarket(market);
				final LoadSlot slot_original = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.ORIGINAL_SLOT, usedIDs);
				final LoadSlot slot_breakEven = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.BREAK_EVEN_VARIANT, usedIDs);
				final LoadSlot slot_changable = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);

				slot_original.setWindowStart(date.atDay(1));
				slot_original.setWindowSize(1);
				slot_original.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_breakEven.setWindowStart(date.atDay(1));
				slot_breakEven.setWindowSize(1);
				slot_breakEven.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_changable.setWindowStart(date.atDay(1));
				slot_changable.setWindowSize(1);
				slot_changable.setWindowSizeUnits(TimePeriod.MONTHS);

				mapper.addMapping(market, date, slot_original, slot_breakEven, slot_changable);
			}
		}
		for (final SpotMarket market : spotMarketsModel.getDesPurchaseSpotMarket().getMarkets()) {
			for (final YearMonth date : dischargeDates) {
				final BuyMarket m = AnalyticsFactory.eINSTANCE.createBuyMarket();
				m.setMarket(market);
				final LoadSlot slot_original = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.ORIGINAL_SLOT, usedIDs);
				final LoadSlot slot_breakEven = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.BREAK_EVEN_VARIANT, usedIDs);
				final LoadSlot slot_changable = AnalyticsBuilder.makeLoadSlot(m, clone, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);

				slot_original.setWindowStart(date.atDay(1));
				slot_original.setWindowSize(1);
				slot_original.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_breakEven.setWindowStart(date.atDay(1));
				slot_breakEven.setWindowSize(1);
				slot_breakEven.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_changable.setWindowStart(date.atDay(1));
				slot_changable.setWindowSize(1);
				slot_changable.setWindowSizeUnits(TimePeriod.MONTHS);

				mapper.addMapping(market, date, slot_original, slot_breakEven, slot_changable);
			}
		}
		for (final SpotMarket market : spotMarketsModel.getDesSalesSpotMarket().getMarkets()) {
			for (final YearMonth date : loadDates) {
				final SellMarket m = AnalyticsFactory.eINSTANCE.createSellMarket();
				m.setMarket(market);
				final DischargeSlot slot_original = AnalyticsBuilder.makeDischargeSlot(m, clone, SlotMode.ORIGINAL_SLOT, usedIDs);
				final DischargeSlot slot_breakEven = AnalyticsBuilder.makeDischargeSlot(m, clone, SlotMode.BREAK_EVEN_VARIANT, usedIDs);
				final DischargeSlot slot_changable = AnalyticsBuilder.makeDischargeSlot(m, clone, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);

				slot_original.setWindowStart(date.atDay(1));
				slot_original.setWindowSize(1);
				slot_original.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_breakEven.setWindowStart(date.atDay(1));
				slot_breakEven.setWindowSize(1);
				slot_breakEven.setWindowSizeUnits(TimePeriod.MONTHS);

				slot_changable.setWindowStart(date.atDay(1));
				slot_changable.setWindowSize(1);
				slot_changable.setWindowSizeUnits(TimePeriod.MONTHS);

				mapper.addMapping(market, date, slot_original, slot_breakEven, slot_changable);
			}
		}

		return shippingMap;
	}

	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new HashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		return usedIDs;
	}

	protected static Map<ShippingOption, VesselAssignmentType> createShipping(final LNGScenarioModel clone, final MarketabilityModel model, final IMapperClass mapper) {
		final Map<ShippingOption, VesselAssignmentType> availabilitiesMap = new HashMap<>();

		for (final ShippingOption shipping : model.getShippingTemplates()) {

			if (shipping instanceof OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
				vesselCharter.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
				final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
				vesselCharter.setVessel(vessel);
				vesselCharter.setEntity(optionalAvailabilityShippingOption.getEntity());

				vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
				vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

				if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
					vesselCharter.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
					vesselCharter.getStartHeel().setCvValue(22.8);
					// vesselCharter.getStartHeel().setPricePerMMBTU(0.1);

					vesselCharter.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}

				vesselCharter.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
				vesselCharter.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselCharter.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselCharter.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselCharter.setOptional(true);
				vesselCharter.setContainedCharterContract(AnalyticsBuilder.createCharterTerms(optionalAvailabilityShippingOption.getRepositioningFee(), //
						optionalAvailabilityShippingOption.getBallastBonus()));
				if (optionalAvailabilityShippingOption.getStartPort() != null) {
					vesselCharter.setStartAt(optionalAvailabilityShippingOption.getStartPort());
				}
				if (optionalAvailabilityShippingOption.getEndPort() != null) {
					final EList<APortSet<Port>> endAt = vesselCharter.getEndAt();
					endAt.clear();
					endAt.add(optionalAvailabilityShippingOption.getEndPort());
				}
				availabilitiesMap.put(optionalAvailabilityShippingOption, vesselCharter);

				mapper.addMapping(optionalAvailabilityShippingOption, vesselCharter);
			} else if (shipping instanceof SimpleVesselCharterOption fleetShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
				vesselCharter.setTimeCharterRate(fleetShippingOption.getHireCost());
				final Vessel vessel = fleetShippingOption.getVessel();
				vesselCharter.setVessel(vessel);
				vesselCharter.setEntity(fleetShippingOption.getEntity());

				vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
				vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

				if (fleetShippingOption.isUseSafetyHeel()) {
					vesselCharter.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
					vesselCharter.getStartHeel().setCvValue(22.8);

					vesselCharter.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
				vesselCharter.setOptional(false);
				clone.getCargoModel().getVesselCharters().add(vesselCharter);
				availabilitiesMap.put(fleetShippingOption, vesselCharter);
				mapper.addMapping(fleetShippingOption, vesselCharter);

			} else if (shipping instanceof RoundTripShippingOption roundTripShippingOption) {
				final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
				final String baseName = SHIPPING_OPTION_DESCRIPTION_FORMATTER.render(roundTripShippingOption);
				@NonNull
				final Set<String> usedIDStrings = clone.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().stream().map(c -> c.getName()).collect(Collectors.toSet());
				final String id = AnalyticsBuilder.getUniqueID(baseName, usedIDStrings);
				market.setName(id);
				market.setCharterInRate(roundTripShippingOption.getHireCost());
				market.setVessel(roundTripShippingOption.getVessel());
				market.setEntity(roundTripShippingOption.getEntity());

				availabilitiesMap.put(roundTripShippingOption, market);

				mapper.addMapping(roundTripShippingOption, market);
			} else if (shipping instanceof ExistingVesselCharterOption eva) {
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final VesselCharter va = eva.getVesselCharter();
				availabilitiesMap.put(eva, va);
				mapper.addMapping(eva, va);
			} else if (shipping instanceof ExistingCharterMarketOption ecmo) {
				final CharterInMarket charter = ecmo.getCharterInMarket();

				availabilitiesMap.put(ecmo, charter);
				mapper.addMapping(ecmo, charter);
			}
		}

		return availabilitiesMap;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean evaluate(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, //
			final MarketabilityModel model, final IProgressMonitor progressMonitor, boolean validateScenario) {
		final long timeBefore = System.currentTimeMillis();
		boolean successful = singleEval(scenarioDataProvider, scenarioInstance, model, progressMonitor, validateScenario);
		final long timeAfter = System.currentTimeMillis();

		System.out.printf("Eval %d\n", timeAfter - timeBefore);
		return successful;
	}

	private static boolean singleEval(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, //
			final MarketabilityModel model, final IProgressMonitor progressMonitor, boolean validateScenario) {

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		boolean[] successful = new boolean[1];
		ServiceHelper.<IAnalyticsScenarioEvaluator>withServiceConsumer(IAnalyticsScenarioEvaluator.class, evaluator -> {
			successful[0] = evaluator.evaluateMarketabilitySandbox(scenarioDataProvider, scenarioInstance, userSettings, model, progressMonitor, validateScenario );
		});
		return successful[0];
	}

	
}
