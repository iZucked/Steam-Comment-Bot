/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class BreakEvenSandboxEvaluator {

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();

	protected static Map<ShippingOption, VesselAssignmentType> buildFullScenario(final LNGScenarioModel clone, final BreakEvenAnalysisModel clonedModel, final IMapperClass mapper) {
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

	protected static Map<ShippingOption, VesselAssignmentType> createShipping(final LNGScenarioModel clone, final BreakEvenAnalysisModel model, final IMapperClass mapper) {
		final Map<ShippingOption, VesselAssignmentType> availabilitiesMap = new HashMap<>();

		for (final ShippingOption shipping : model.getShippingTemplates()) {

			if (shipping instanceof OptionalSimpleVesselCharterOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption = (OptionalSimpleVesselCharterOption) shipping;
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
				vesselCharter.setContainedCharterContract(AnalyticsBuilder.createCharterTerms(optionalAvailabilityShippingOption.getRepositioningFee(),//
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
			} else if (shipping instanceof SimpleVesselCharterOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final SimpleVesselCharterOption fleetShippingOption = (SimpleVesselCharterOption) shipping;
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
					// vesselCharter.getStartHeel().setPricePerMMBTU(0.1);

					vesselCharter.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
					vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
				vesselCharter.setOptional(false);
				clone.getCargoModel().getVesselCharters().add(vesselCharter);
				availabilitiesMap.put(fleetShippingOption, vesselCharter);
				mapper.addMapping(fleetShippingOption, vesselCharter);

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
			}
		}

		return availabilitiesMap;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final BreakEvenAnalysisModel model) {

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();

		final CompoundCommand cmd = new CompoundCommand("Generate results");

		final long a = System.currentTimeMillis();

		{

			singleEval(scenarioEditingLocation, model, cmd);
		}

		final long b = System.currentTimeMillis();
		System.out.printf("Eval %d\n", b - a);

		if (!cmd.isEmpty()) {
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, model, null);
		}
	}

	private static void filterTasks(final List<BaseCase> tasks) {
		final Set<BaseCase> duplicates = new HashSet<>();
		for (final BaseCase baseCase1 : tasks) {
			DUPLICATE_TEST: for (final BaseCase baseCase2 : tasks) {
				if (duplicates.contains(baseCase1) || duplicates.contains(baseCase2) || baseCase1 == baseCase2 || baseCase1.getBaseCase().size() != baseCase2.getBaseCase().size()) {
					continue;
				}
				for (int i = 0; i < baseCase1.getBaseCase().size(); i++) {
					final BaseCaseRow baseCase1Row = baseCase1.getBaseCase().get(i);
					final BaseCaseRow baseCase2Row = baseCase2.getBaseCase().get(i);
					if (baseCase1Row.getBuyOption() != baseCase2Row.getBuyOption() || baseCase1Row.getSellOption() != baseCase2Row.getSellOption()
							|| baseCase1Row.getShipping() != baseCase2Row.getShipping()) {
						continue DUPLICATE_TEST;
					}
				}
				duplicates.add(baseCase2);
			}
		}
		tasks.removeAll(duplicates);
	}

	private static void singleEval(final IScenarioEditingLocation scenarioEditingLocation, final BreakEvenAnalysisModel model, CompoundCommand cmd) {

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final IMapperClass mapper = new Mapper(lngScenarioModel, true);
			final Map<ShippingOption, VesselAssignmentType> shippingMap = buildFullScenario(lngScenarioModel, model, mapper);

			final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
			userSettings.setGenerateCharterOuts(false);
			userSettings.setShippingOnly(false);
			userSettings.setWithSpotCargoMarkets(true);
			userSettings.setSimilarityMode(SimilarityMode.OFF);

			ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class, evaluator -> {
				evaluator.evaluateBreakEvenSandbox(scenarioEditingLocation.getScenarioDataProvider(), scenarioEditingLocation.getScenarioInstance(), userSettings, model, mapper, shippingMap, cmd);
			});

			// TODO: Save generated things
		}
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
}
