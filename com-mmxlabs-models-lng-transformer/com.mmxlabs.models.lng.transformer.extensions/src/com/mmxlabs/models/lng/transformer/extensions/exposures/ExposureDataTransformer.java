/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.exposures;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendarEntry;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProviderEditor;

/**
 */
public class ExposureDataTransformer implements ISlotTransformer {
	
	@Inject
	private IExposureDataProviderEditor exposureDataProviderEditor;
	
	@Inject
	private IExposuresCustomiser exposureCustomiser;
	
	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;
	
	@Inject
	@Named(SchedulerConstants.EXPOSURES_CUTOFF_AT_PROMPT_START)
	private boolean cutoffAtPromptStart;

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		if (exposuresEnabled) {
			final Slot<?> exposedSlot = exposureCustomiser.getExposed(modelSlot);
			final String priceExpression = exposureCustomiser.provideExposedPriceExpression(exposedSlot);
			if (priceExpression != null) {
				exposureDataProviderEditor.addPriceExpressionForPortSLot(optimiserSlot, priceExpression);
			}
		}
	}
	
	@Override
	public void startTransforming(LNGScenarioModel lngScenarioModel, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		if (exposuresEnabled) {
			if (lngScenarioModel != null && lngScenarioModel.getReferenceModel() != null) {
				final PricingModel pricingModel = lngScenarioModel.getReferenceModel().getPricingModel();
				final CargoModel cargoModel = lngScenarioModel.getCargoModel();
				if (pricingModel != null) {					
					final ExposuresLookupData lookupData = new ExposuresLookupData();
					if (lngScenarioModel.getPromptPeriodStart() != null && cutoffAtPromptStart) {
						lookupData.cutoffDate = lngScenarioModel.getPromptPeriodStart();
					}
					cargoModel.getCargoesForExposures().forEach(c -> {
						for (final var slot : c.getSlots()) {
							String prefix = "FP-";
							if (slot instanceof LoadSlot) {
								prefix = "FP-";
								if (((LoadSlot) slot).isDESPurchase()) {
									prefix = "DP-";
								}
							} else {
								prefix = "DS-";
								if (((DischargeSlot) slot).isFOBSale()) {
									prefix = "FS-";
								}
							}
							lookupData.slotsToInclude.add(prefix + slot.getName());
						}
					});
					pricingModel.getCommodityCurves().stream().filter(idx -> idx.getName() != null)//
					.forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), new BasicCommodityCurveData(//
									idx.getName().toLowerCase(), idx.getVolumeUnit(), idx.getCurrencyUnit(), idx.getExpression())));
					pricingModel.getCurrencyCurves().stream().filter(idx -> idx.getName() != null)//
					.forEach(idx -> lookupData.currencyList.add(idx.getName().toLowerCase()));
					pricingModel.getConversionFactors().forEach(f -> lookupData.conversionMap.put(//
							PriceIndexUtils.createConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));
					pricingModel.getConversionFactors().forEach(f -> lookupData.reverseConversionMap.put(//
							PriceIndexUtils.createReverseConversionFactorName(f).toLowerCase(), new BasicUnitConversionData(f.getFrom(), f.getTo(), f.getFactor())));
					
					for (final CommodityCurve curve : pricingModel.getCommodityCurves()) {
						final MarketIndex marketIndex = curve.getMarketIndex();
						final String curveName = curve.getName().toLowerCase();
						if (marketIndex == null) {
							continue;
						}
						if (marketIndex.getPricingCalendar() != null) {
							lookupData.pricingCalendars.putIfAbsent(curveName, transformPricingCalendar(marketIndex.getPricingCalendar()));
						}
						if (marketIndex.getSettleCalendar() != null) {
							lookupData.holidayCalendars.putIfAbsent(curveName, transformHolidayCalendar(marketIndex.getSettleCalendar()));
						}
					}
					exposureDataProviderEditor.addLookupData(lookupData);
				}
			}
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
