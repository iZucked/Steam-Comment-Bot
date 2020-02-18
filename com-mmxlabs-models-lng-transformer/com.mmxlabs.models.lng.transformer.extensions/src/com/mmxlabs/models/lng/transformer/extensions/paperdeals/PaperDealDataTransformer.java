/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendarEntry;
import com.mmxlabs.common.paperdeals.BasicInstrumentData;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.common.paperdeals.PaperDealsLookupData;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPaperDealDataProviderEditor;

/**
 */
public class PaperDealDataTransformer implements ISlotTransformer {
	
	@Inject
	private IPaperDealDataProviderEditor paperDealDataProviderEditor;
	
	@Inject
	private IScenarioDataProvider scenarioDataProvider;
	
	@Inject
	@Named(SchedulerConstants.COMPUTE_PAPER_PNL)
	private boolean paperPnLEnabled;
	
	@Override
	public void startTransforming(LNGScenarioModel lngScenarioModel, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		if (paperPnLEnabled) {
			if (lngScenarioModel != null && lngScenarioModel.getReferenceModel() != null) {
				final CargoModel cargoModel= lngScenarioModel.getCargoModel();
				final PricingModel pricingModel = lngScenarioModel.getReferenceModel().getPricingModel();
				final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
				
				if (cargoModel != null && pricingModel != null) {
					final DataHandler dataHandler = new DataHandler(pricingModel, provider);
					final List<BasicPaperDealData> paperDeals = new ArrayList<>(cargoModel.getPaperDeals().size());
					
					for (final PaperDeal entry : cargoModel.getPaperDeals()) {
						String name = entry.getName();
						boolean isBuy = false;
						if (entry instanceof BuyPaperDeal) {
							isBuy = true;
						}
						final long paperVolume = OptimiserUnitConvertor.convertToInternalVolume(entry.getQuantity());
						final int paperUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(entry.getPrice());
						LocalDate start = entry.getStartDate();
						LocalDate end = entry.getEndDate();
						BasicInstrumentData instrument = null;
						final String type = entry.getPricingType().getLiteral();
						if (type.equals("INSTRUMENT") && entry.getInstrument() != null) {
							instrument = dataHandler.instruments.get(entry.getInstrument().getName().toLowerCase());
						} 
						if ((type.equals("INSTRUMENT") || type.equals("CALENDAR")) && entry.getPricingMonth() != null) {
							final YearMonth month = entry.getPricingMonth();
							start = LocalDate.of(month.getYear(), month.getMonth(), 1);
							end = LocalDate.of(month.getYear(), month.getMonth(), month.lengthOfMonth());
						}
						final String curveName = entry.getIndex().toLowerCase();
						final String entity = entry.getEntity().getName();
						
						final int year = entry.getYear();
						final String note = entry.getComment();
						
						final BasicPaperDealData basicPaperDealData = new BasicPaperDealData(name, isBuy, paperVolume, paperUnitPrice, start, end, type, instrument, curveName, entity, year, note, false);
						paperDeals.add(basicPaperDealData);
						modelEntityMap.addModelObject(entry, basicPaperDealData);
					}
					final PaperDealsLookupData lookupData = new PaperDealsLookupData(dataHandler.pricingCalendars, dataHandler.holidayCalendars, dataHandler.settledPrices, paperDeals, dataHandler.hedgeCurves);
					paperDealDataProviderEditor.addLookupData(lookupData);
				}
			}
		}
	}

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {

	}
	
	private class DataHandler {

		private Map<String, BasicPricingCalendar> pricingCalendars = new HashMap<>();
		private Map<String, BasicHolidayCalendar> holidayCalendars = new HashMap<>();
		private Map<String, Map<LocalDate, Double>> settledPrices = new HashMap<>();
		private Map<String, BasicInstrumentData> instruments = new HashMap<>();
		private Map<String, Map<String, String>> hedgeCurves = new HashMap<>();
		
		public DataHandler(final @NonNull PricingModel pricingModel, final ModelMarketCurveProvider provider) {
	
			for (final CommodityCurve curve : pricingModel.getCommodityCurves()) {
				final MarketIndex marketIndex = curve.getMarketIndex();
				final String curveName = curve.getName().toLowerCase();
				if (marketIndex == null) {
					continue;
				}
				if (marketIndex.getPricingCalendar() != null) {
					pricingCalendars.putIfAbsent(curveName, transformPricingCalendar(marketIndex.getPricingCalendar()));
				}
				if (marketIndex.getSettleCalendar() != null) {
					holidayCalendars.putIfAbsent(curveName, transformHolidayCalendar(marketIndex.getSettleCalendar()));
				}
				final Map<String, String> buySellCurves = new HashMap<>();
				if (marketIndex.getBidCurve() != null) {
					buySellCurves.put("bid", marketIndex.getBidCurve().getName().toLowerCase());
				}
				if (marketIndex.getOfferCurve() != null) {
					buySellCurves.put("offer", marketIndex.getOfferCurve().getName().toLowerCase());
				}
				hedgeCurves.put(curveName, buySellCurves);
				if (provider != null) {
					final Map<LocalDate, Double> settlePrices = provider.getSettledPrices(curveName);
					if (settlePrices != null && !settlePrices.isEmpty()) {
						settledPrices.putIfAbsent(curveName, settlePrices);
					}
				}
			}
			
			for (final SettleStrategy settleStrategy : pricingModel.getSettleStrategies()) {
				final BasicInstrumentData basicInstrumentData = new BasicInstrumentData(settleStrategy.getName().toLowerCase(), settleStrategy.getDayOfTheMonth(), settleStrategy.isLastDayOfTheMonth(), 
						settleStrategy.getSettlePeriod(), settleStrategy.getSettlePeriodUnit().getLiteral(), settleStrategy.getSettleStartMonthsPrior());
				instruments.putIfAbsent(settleStrategy.getName().toLowerCase(), basicInstrumentData);
			}
		}
		
		private BasicPricingCalendar transformPricingCalendar(final PricingCalendar pricingCalendar) {
			final BasicPricingCalendar basicPricingCalendar = new BasicPricingCalendar(pricingCalendar.getName().toLowerCase());
			pricingCalendar.getEntries().forEach(e -> basicPricingCalendar.getEntries().add(new BasicPricingCalendarEntry(e.getMonth(), e.getStart(), e.getEnd())));
			return basicPricingCalendar;
		}
		
		private BasicHolidayCalendar transformHolidayCalendar(final HolidayCalendar holidayCalendar) {
			final BasicHolidayCalendar basicHolidayCalendar = new BasicHolidayCalendar(holidayCalendar.getName().toLowerCase());
			holidayCalendar.getEntries().forEach(e -> basicHolidayCalendar.getHolidays().add(e.getDate()));
			return basicHolidayCalendar;
		}
	}
}
