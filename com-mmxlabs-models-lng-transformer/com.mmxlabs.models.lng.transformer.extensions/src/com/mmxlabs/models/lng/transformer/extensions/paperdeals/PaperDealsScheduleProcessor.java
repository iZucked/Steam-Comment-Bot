/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class PaperDealsScheduleProcessor implements IOutputScheduleProcessor {

	@Inject
	private LNGScenarioModel scenarioModel;

	@Inject
	private IScenarioDataProvider scenarioDataProvider;

	@Override
	public void process(final Schedule schedule) {
		if (LicenseFeatures.isPermitted("features:paperdeals")) {
			boolean calculateExposures = LicenseFeatures.isPermitted("features:exposures");
			//add calendar support
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);

			final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
			if (provider == null) {
				return;
			}
			
			final CalendarHandler calendarHandler = new CalendarHandler(scenarioDataProvider);

			for (final PaperDeal paperDeal : cargoModel.getPaperDeals()) {

				final boolean isBuy = paperDeal instanceof BuyPaperDeal;

				final PaperDealAllocation allocation = ScheduleFactory.eINSTANCE.createPaperDealAllocation();
				allocation.setPaperDeal(paperDeal);

				final Map<LocalDate, Double> settledPrices = provider.getSettledPrices(paperDeal.getIndex());
				final ISeries series = provider.getSeriesParser(PriceIndexType.COMMODITY).getSeries(paperDeal.getIndex());
				final AbstractYearMonthCurve curveData = provider.getCurve(PriceIndexType.COMMODITY, paperDeal.getIndex());
				
				switch (paperDeal.getPricingType()) {
				case PERIOD_AVG:
					calculateDailyAveragePaperExposures(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData);
					break;
				case CALENDAR:
					calculateCalendarPaperExposures(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData, calendarHandler);
					break;
				case INSTRUMENT:
					calculateInstrumentPaperExposures(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData);
					break;
				default:
					throw new IllegalArgumentException("Paper pricing type has a wrong state");
				}
			}
		}
	}

	private void calculateDailyAveragePaperExposures(final Schedule schedule, boolean calculateExposures, final PricingModel pricingModel, final PaperDeal paperDeal, final boolean isBuy,
			final PaperDealAllocation allocation, final Map<LocalDate, Double> settledPrices, final ISeries series, final AbstractYearMonthCurve curveData) {
		final LocalDate extStart = paperDeal.getStartDate();
		final LocalDate extEnd = paperDeal.getEndDate();
		
		settlePaper(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData, extStart, extEnd);
	}
	
	private void calculateCalendarPaperExposures(final Schedule schedule, boolean calculateExposures, final PricingModel pricingModel, final PaperDeal paperDeal, final boolean isBuy,//
			final PaperDealAllocation allocation, final Map<LocalDate, Double> settledPrices, final ISeries series, final AbstractYearMonthCurve curveData, //
			final CalendarHandler calendarHandler) {
		
		final List<PricingCalendar> pcs = new ArrayList<>();
		final List<HolidayCalendar> hcs = new ArrayList<>();

		if (curveData instanceof CommodityCurve) {
			final CommodityCurve cc = (CommodityCurve) curveData;
			if (calendarHandler.getPricingCalendar(cc) != null) {
				pcs.add(calendarHandler.getPricingCalendar(cc));
			}
			if (calendarHandler.getHolidayCalendar(cc) != null) {
				hcs.add(calendarHandler.getHolidayCalendar(cc));
			}
		}
		
		if (pcs.isEmpty() || paperDeal.getPricingMonth() == null) {
			return;
		}
		final PricingCalendar pc = pcs.get(0);
		final List<PricingCalendarEntry> lpce = pc.getEntries().stream().filter(a -> paperDeal.getPricingMonth().equals(a.getMonth())).collect(Collectors.toList());
		if (lpce.isEmpty()) {
			return;
		}
		final PricingCalendarEntry pce = lpce.get(0);
		
		final LocalDate extStart = pce.getStart();
		final LocalDate extEnd = pce.getEnd();
		
		settlePaper(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData, extStart, extEnd);
	}
	
	private void calculateInstrumentPaperExposures(final Schedule schedule, boolean calculateExposures, final PricingModel pricingModel, final PaperDeal paperDeal, final boolean isBuy,
			final PaperDealAllocation allocation, final Map<LocalDate, Double> settledPrices, final ISeries series, final AbstractYearMonthCurve curveData) {
		
		if (paperDeal.getInstrument() == null || paperDeal.getPricingMonth() == null) {
			return;
		}
		
		final SettleStrategy ss = paperDeal.getInstrument();
		final YearMonth originalMonth = paperDeal.getPricingMonth();
		final YearMonth startMonth = originalMonth.plusMonths(-1*ss.getSettleStartMonthsPrior());
		
		int settlingPeriodDurationInDays = 0;

		switch (ss.getSettlePeriodUnit()) {
		case HOURS:
			settlingPeriodDurationInDays = ss.getSettlePeriod() / 24;
			break;
		case DAYS:
			settlingPeriodDurationInDays = ss.getSettlePeriod();
			break;
		case MONTHS:
			settlingPeriodDurationInDays = ss.getSettlePeriod() * 30;
		default:
			break;
		}

		int startDay = ss.isLastDayOfTheMonth() ? startMonth.lengthOfMonth() : ss.getDayOfTheMonth();
		if (startDay == 0) {
			startDay = 1;
		}
		
		final LocalDate extStart = getNonWeekendStart(startMonth, startDay);
		final LocalDate extEnd = extStart.plusDays(settlingPeriodDurationInDays - 1);
		
		settlePaper(schedule, calculateExposures, pricingModel, paperDeal, isBuy, allocation, settledPrices, series, curveData, extStart, extEnd);
	}

	private @NonNull LocalDate getNonWeekendStart(final YearMonth startMonth, int startDay) {

		LocalDate result = LocalDate.of(startMonth.getYear(), startMonth.getMonthValue(), startDay);
		boolean isWeekend = result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY;
		while(isWeekend) {
			result = result.plusDays(1);
			isWeekend = result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY;
		}
		return result;
	}
	
	private int getNonWeekendStartDay(final YearMonth startMonth, int startDay) {

		LocalDate result = LocalDate.of(startMonth.getYear(), startMonth.getMonthValue(), startDay);
		boolean isWeekend = result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY;
		while(isWeekend) {
			result = result.plusDays(1);
			isWeekend = result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY;
			++startDay;
		}
		return startDay;
	}
	
	private void settlePaper(final Schedule schedule, boolean calculateExposures, final PricingModel pricingModel, final PaperDeal paperDeal, final boolean isBuy, final PaperDealAllocation allocation,
			final Map<LocalDate, Double> settledPrices, final ISeries series, final AbstractYearMonthCurve curveData, final LocalDate extStart, final LocalDate extEnd) {
		LocalDate start = extStart;
		int days = 0;
		// Count trading days
		while (!start.isAfter(extEnd)) {
			final boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;

			start = start.plusDays(1);
			if (isWeekend) {
				continue;
			}
			// If holiday continue
			++days;
		}

		start = extStart;
		// Fixed price portion
		final PaperDealAllocationEntry fixedPortion;
		{
			PaperDealAllocationEntry entry = ScheduleFactory.eINSTANCE.createPaperDealAllocationEntry();
			entry.setDate(start);
			double price = paperDeal.getPrice();
			boolean settled = true;
			entry.setPrice(price);
			entry.setSettled(settled);
			// Buy, gain volume, sell loose volume
			entry.setQuantity((isBuy ? -1.0 : 1.0) * paperDeal.getQuantity());
			// Buy, pay money , sell gain money
			entry.setValue(entry.getQuantity() * entry.getPrice());

			// excluding the fixed portion for now
			//allocation.getEntries().add(entry); 

			fixedPortion = entry;
		}
		// MTM Part
		// Count trading days
		while (!start.isAfter(extEnd)) {
			final boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;

			if (!isWeekend) {
				final PaperDealAllocationEntry entry = ScheduleFactory.eINSTANCE.createPaperDealAllocationEntry();

				entry.setDate(start);
				double price = series.evaluate(PriceIndexUtils.convertTime(PriceIndexUtils.dateZero, start)).doubleValue();
				boolean settled = false;
				if (settledPrices != null) {
					final Double settledPrice = settledPrices.get(start);

					if (settledPrice != null) {
						price = settledPrice;
						settled = true;
					}
				}

				entry.setPrice(price);
				//
				entry.setSettled(settled);
				entry.setQuantity((isBuy ? 1.0 : -1.0) * paperDeal.getQuantity() / (double) days);
				entry.setValue(entry.getQuantity() * (entry.getPrice() - paperDeal.getPrice()));

				if (!settled && calculateExposures) {
					final ExposureDetail detail = ScheduleFactory.eINSTANCE.createExposureDetail();
					detail.setDealType(DealType.FINANCIAL);
					detail.setCurrencyUnit(curveData.getCurrencyUnit());
					detail.setVolumeUnit(curveData.getVolumeUnit());

					detail.setDate(YearMonth.from(entry.getDate()));

					detail.setIndexName(paperDeal.getIndex());

					detail.setVolumeInNativeUnits(entry.getQuantity());
					if (curveData.getVolumeUnit() == null || curveData.getVolumeUnit().isEmpty() || "mmbtu".equalsIgnoreCase(curveData.getVolumeUnit())) {
						// Native matches mmbtu
						detail.setVolumeInMMBTU(entry.getQuantity());
					} else {

					}
					// Not mmBtu? then the mmBtu field is still really native units
					// Perform units conversion - compute mmBtu equivalent of exposed native volume
					double mmbtuVolume = entry.getQuantity();
					for (final UnitConversion factor : pricingModel.getConversionFactors()) {
						if (factor.getTo().equalsIgnoreCase("mmbtu")) {
							if (factor.getFrom().equalsIgnoreCase(curveData.getVolumeUnit())) {
								mmbtuVolume /= factor.getFactor();
								break;
							}
						}
					}
					detail.setVolumeInMMBTU(mmbtuVolume);

					detail.setNativeValue(entry.getQuantity() * entry.getPrice());
					detail.setUnitPrice(entry.getPrice());

					entry.getExposures().add(detail);

				}
				allocation.getEntries().add(entry);
			}
			start = start.plusDays(1);
		}

		schedule.getPaperDealAllocations().add(allocation);
	}
	
	private static class CalendarHandler {

		private Map<CommodityCurve, PricingCalendar> pricingCalendars = new HashMap<>();
		private Map<CommodityCurve, HolidayCalendar> holidayCalendars = new HashMap<>();

		public PricingCalendar getPricingCalendar(final CommodityCurve cc) {
			return pricingCalendars.get(cc);
		}

		public HolidayCalendar getHolidayCalendar(final CommodityCurve cc) {
			return holidayCalendars.get(cc);
		}

		public CalendarHandler(final @NonNull IScenarioDataProvider scenarioDataProvider) {

			PricingModel pm = ScenarioModelUtil.getPricingModel(scenarioDataProvider);

			for (final AbstractYearMonthCurve curve : pm.getCommodityCurves()) {
				if (curve instanceof CommodityCurve) {
					final CommodityCurve cc = (CommodityCurve) curve;
					final MarketIndex mi = cc.getMarketIndex();
					if (mi == null) {
						continue;
					}
					if (mi.getPricingCalendar() != null) {
						pricingCalendars.putIfAbsent(cc, mi.getPricingCalendar());
					}
					if (mi.getSettleCalendar() != null) {
						holidayCalendars.putIfAbsent(cc, mi.getSettleCalendar());
					}
				}
			}
		}
	}

}
