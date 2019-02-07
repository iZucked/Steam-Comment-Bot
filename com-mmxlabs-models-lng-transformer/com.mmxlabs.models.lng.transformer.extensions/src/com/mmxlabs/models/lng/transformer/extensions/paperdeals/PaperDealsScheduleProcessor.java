/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
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
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);

			final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
			if (provider == null) {
				return;
			}

			for (final PaperDeal paperDeal : cargoModel.getPaperDeals()) {

				final boolean isBuy = paperDeal instanceof BuyPaperDeal;

				final PaperDealAllocation allocation = ScheduleFactory.eINSTANCE.createPaperDealAllocation();
				allocation.setPaperDeal(paperDeal);

				LocalDate start = paperDeal.getStartDate();
				int days = 0;
				// Count trading days
				while (!start.isAfter(paperDeal.getEndDate())) {
					final boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;

					start = start.plusDays(1);
					if (isWeekend) {
						continue;
					}
					// If holiday continue
					++days;
				}

				final Map<LocalDate, Double> settledPrices = provider.getSettledPrices(paperDeal.getIndex());
				final ISeries series = provider.getSeriesParser(PriceIndexType.COMMODITY).getSeries(paperDeal.getIndex());
				final AbstractYearMonthCurve curveData = provider.getCurve(PriceIndexType.COMMODITY, paperDeal.getIndex());

				start = paperDeal.getStartDate();
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
					entry.setQuantity((isBuy ? 1.0 : -1.0) * paperDeal.getQuantity());
					// Buy, pay money , sell gain money
					entry.setValue(-entry.getQuantity() * entry.getPrice());

					allocation.getEntries().add(entry);

					fixedPortion = entry;
				}
				// MTM Part
				// Count trading days
				while (!start.isAfter(paperDeal.getEndDate())) {
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
						entry.setValue(-(entry.getQuantity() * entry.getPrice()));

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

							fixedPortion.getExposures().add(detail);

						}
						allocation.getEntries().add(entry);
					}
					start = start.plusDays(1);
				}

				schedule.getPaperDealAllocations().add(allocation);
			}
		}
	}

}
