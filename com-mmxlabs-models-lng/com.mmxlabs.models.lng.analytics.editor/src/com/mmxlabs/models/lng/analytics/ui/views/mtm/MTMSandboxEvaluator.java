/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class MTMSandboxEvaluator {

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();

	protected static void buildFullScenario(final LNGScenarioModel clone, final MTMModel clonedModel, final IMapperClass mapper) {
		final Set<String> usedIDs = getUsedSlotIDs(clone);
		final Set<YearMonth> loadDates = new LinkedHashSet<>();
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

		final Set<YearMonth> dischargeDates = new LinkedHashSet<>();

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
		
		for (final SpotMarket market : clonedModel.getMarkets()) {
			for (final YearMonth date : dischargeDates) {
				
				if (market instanceof FOBPurchasesMarket 
						|| market instanceof DESPurchaseMarket) {
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
			for (final YearMonth date : loadDates) {
				 if (market instanceof FOBSalesMarket
						|| market instanceof DESSalesMarket){
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
		}
	}

	public static void evaluate(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, //
			final MTMModel model, IProgressMonitor progressMonitor) {
		final long a = System.currentTimeMillis();
		{
			singleEval(scenarioDataProvider, scenarioInstance, model, progressMonitor);
		}

		final long b = System.currentTimeMillis();
		System.out.printf("Marked to market eval %d\n", b - a);
	}

	private static void singleEval(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, final MTMModel model, //
			IProgressMonitor progressMonitor) {

		final LNGScenarioModel optimiserScenario = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final IMapperClass mapper = new Mapper(optimiserScenario, true);
		buildFullScenario(optimiserScenario, model, mapper);

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class, evaluator -> {
			evaluator.evaluateMTMSandbox(scenarioDataProvider, scenarioInstance, userSettings, model, mapper, progressMonitor);
		});
	}
	
	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new LinkedHashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream()
				.map(LoadSlot::getName)
				.collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream()
				.map(DischargeSlot::getName)
				.collect(Collectors.toSet()));
		return usedIDs;
	}

}
