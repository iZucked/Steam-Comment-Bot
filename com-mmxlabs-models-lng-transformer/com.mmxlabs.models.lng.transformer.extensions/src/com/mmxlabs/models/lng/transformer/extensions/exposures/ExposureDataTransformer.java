/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProviderEditor;

/**
 */
public class ExposureDataTransformer implements ISlotTransformer {

	private PricingModel pricingModel;
	
	@Inject
	private IExposureDataProviderEditor exposureDataProviderEditor;

	@Inject
	private IExposuresCustomiser exposureCustomiser;

	@Inject
	@Named(SchedulerConstants.INDIVIDUAL_EXPOSURES)
	private boolean individualExposures;

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
			String priceExpression = null;
			// Check for volume tier contract - convert it to an expression
			if (exposedSlot.getContract() != null && exposedSlot.getContract().getPriceInfo() instanceof VolumeTierPriceParameters vtContract) {

				VolumeTierSlotParams p = SlotContractParamsHelper.findSlotContractParams(exposedSlot, VolumeTierSlotParams.class, VolumeTierPriceParameters.class);
				if (p != null && p.isOverrideContract()) {
					if (p.getVolumeLimitsUnit() == VolumeUnits.M3) {
						priceExpression = String.format("VOLUMETIERM3(%s,%f,%s)", p.getLowExpression(), p.getThreshold(), p.getHighExpression());
					} else {
						priceExpression = String.format("VOLUMETIERMMBTU(%s,%f,%s)", p.getLowExpression(), p.getThreshold(), p.getHighExpression());
					}
				} else {
					if (vtContract.getVolumeLimitsUnit() == VolumeUnits.M3) {
						priceExpression = String.format("VOLUMETIERM3(%s,%f,%s)", generateSafeExpression(vtContract.getLowExpression()), vtContract.getThreshold(),
								generateSafeExpression(vtContract.getHighExpression()));
					} else {
						priceExpression = String.format("VOLUMETIERMMBTU(%s,%f,%s)", generateSafeExpression(vtContract.getLowExpression()), vtContract.getThreshold(),
								generateSafeExpression(vtContract.getHighExpression()));
					}
				}
			}
			
			if (priceExpression == null) {
				priceExpression = exposureCustomiser.provideExposedPriceExpression(exposedSlot);
				if (priceExpression == null || priceExpression.isBlank()) {
					priceExpression = exposureCustomiser.provideExposedPriceExpression(exposedSlot, ModelMarketCurveProvider.getOrCreate(pricingModel));
				}
			}
			if (priceExpression != null) {
				exposureDataProviderEditor.addPriceExpressionForPortSlot(optimiserSlot, priceExpression);
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
					this.pricingModel = pricingModel;
					final ExposuresLookupData lookupData = new ExposuresLookupData();
					if (lngScenarioModel.getPromptPeriodStart() != null && cutoffAtPromptStart) {
						lookupData.cutoffDate = lngScenarioModel.getPromptPeriodStart();
					}
					if (individualExposures) {
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
					}
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

	/**
	 * Returns a simple constant if the expression string is not set
	 * 
	 * @param expression
	 * @return
	 */
	private String generateSafeExpression(String expression) {
		if (expression == null || expression.isBlank()) {
			return "0";
		}
		return expression;
	}
}
