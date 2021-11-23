/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.viability;

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
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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

public class ViabilitySandboxEvaluator {

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();

	protected static Map<ShippingOption, VesselAssignmentType> buildFullScenario(final LNGScenarioModel clone, final ViabilityModel clonedModel, final IMapperClass mapper) {
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
		for (final SpotMarket market : spotMarketsModel.getFobSalesSpotMarket().getMarkets()) {
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
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream()
				.map(LoadSlot::getName)
				.collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream()
				.map(DischargeSlot::getName)
				.collect(Collectors.toSet()));
		return usedIDs;
	}

	protected static Map<ShippingOption, VesselAssignmentType> createShipping(final LNGScenarioModel clone, final ViabilityModel model, final IMapperClass mapper) {
		final Map<ShippingOption, VesselAssignmentType> availabilitiesMap = new HashMap<>();

		for (final ShippingOption shipping : model.getShippingTemplates()) {

			if (shipping instanceof OptionalSimpleVesselCharterOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption = (OptionalSimpleVesselCharterOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
				final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(optionalAvailabilityShippingOption.getEntity());

				vesselAvailability.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

				if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);
					// vesselAvailability.getStartHeel().setPricePerMMBTU(0.1);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}

				vesselAvailability.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
				vesselAvailability.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setOptional(true);
				vesselAvailability.setContainedCharterContract(AnalyticsBuilder.createCharterTerms(optionalAvailabilityShippingOption.getRepositioningFee(),//
						optionalAvailabilityShippingOption.getBallastBonus()));
				if (optionalAvailabilityShippingOption.getStartPort() != null) {
					vesselAvailability.setStartAt(optionalAvailabilityShippingOption.getStartPort());
				}
				if (optionalAvailabilityShippingOption.getEndPort() != null) {
					final EList<APortSet<Port>> endAt = vesselAvailability.getEndAt();
					endAt.clear();
					endAt.add(optionalAvailabilityShippingOption.getEndPort());
				}
				availabilitiesMap.put(optionalAvailabilityShippingOption, vesselAvailability);

				mapper.addMapping(optionalAvailabilityShippingOption, vesselAvailability);
			} else if (shipping instanceof SimpleVesselCharterOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final SimpleVesselCharterOption fleetShippingOption = (SimpleVesselCharterOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(fleetShippingOption.getHireCost());
				final Vessel vessel = fleetShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(fleetShippingOption.getEntity());

				vesselAvailability.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

				if (fleetShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
				vesselAvailability.setOptional(false);
				clone.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
				availabilitiesMap.put(fleetShippingOption, vesselAvailability);
				mapper.addMapping(fleetShippingOption, vesselAvailability);

			} else if (shipping instanceof RoundTripShippingOption) {
				final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;
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
			} else if (shipping instanceof ExistingVesselCharterOption) {
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final ExistingVesselCharterOption eva = (ExistingVesselCharterOption) shipping;
				final VesselAvailability va = eva.getVesselCharter();
				availabilitiesMap.put(eva, va);
				mapper.addMapping(eva, va);
			} else if (shipping instanceof ExistingCharterMarketOption) {
				final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) shipping;
				final CharterInMarket charter = ecmo.getCharterInMarket();
				
				availabilitiesMap.put(ecmo, charter);
				mapper.addMapping(ecmo, charter);
			}
		}

		return availabilitiesMap;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public static void evaluate(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, //
			final ViabilityModel model, final IProgressMonitor progressMonitor) {
		final long a = System.currentTimeMillis();
		{
			singleEval(scenarioDataProvider, scenarioInstance, model, progressMonitor);
		}

		final long b = System.currentTimeMillis();
		System.out.printf("Eval %d\n", b - a);

	}
	
	private static void singleEval(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, //
			final ViabilityModel model, final IProgressMonitor progressMonitor) {

		final LNGScenarioModel optimiserScenario = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final IMapperClass mapper = new Mapper(optimiserScenario, true);
		final Map<ShippingOption, VesselAssignmentType> shippingMap = buildFullScenario(optimiserScenario, model, mapper);

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class, evaluator -> {
			evaluator.evaluateViabilitySandbox(scenarioDataProvider, scenarioInstance, userSettings, model, mapper, shippingMap, progressMonitor);
		});
	}

}
