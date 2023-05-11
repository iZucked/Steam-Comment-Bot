/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendarEntry;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PricingEventHelper;
import com.mmxlabs.scheduler.optimiser.exposures.calculators.ExposuresASTToCalculator;
import com.mmxlabs.scheduler.optimiser.exposures.calculators.ExposuresCalculatorUtils;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Utility class to calculate schedule exposure to market indices.
 * 
 * @author FM
 */
public class ExposuresCalculator {

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Currency)
	private SeriesParser currencyIndices;

	@Inject
	@Named(SchedulerConstants.INDIVIDUAL_EXPOSURES)
	private boolean individualExposures;

	@Inject
	private IExternalDateProvider externalDateProvider;

	@Inject
	private PricingEventHelper pricingEventHelper;

	@Inject
	private IExposureDataProvider exposureDataProvider;

	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;
	
	@Inject(optional = true)
	@Named(SchedulerConstants.IGNORE_EXPOSURES)
	private boolean exposuresIgnored = false;

	public List<BasicExposureRecord> calculateExposures(final ICargoValueAnnotation cargoValueAnnotation, final IPortSlot portSlot) {
		if (exposuresEnabled && !exposuresIgnored) {

			final ExposuresLookupData lookupData = exposureDataProvider.getExposuresLookupData();

			if (individualExposures && !lookupData.slotsToInclude.contains(portSlot.getId())) {
				return Collections.emptyList();
			}

			boolean isLong = false;
			if (portSlot.getPortType() == PortType.Load) {
				isLong = true;
			} else if (portSlot.getPortType() == PortType.Discharge) {
				isLong = false;
			} else {
				return Collections.emptyList();
			}

			final long volumeMMBTU = cargoValueAnnotation.getPhysicalSlotVolumeInMMBTu(portSlot);
			final int pricePerMMBTU = cargoValueAnnotation.getSlotPricePerMMBTu(portSlot);
			final String priceExpression = exposureDataProvider.getPriceExpression(portSlot);
			int pricingTime = 0;
			if (isLong) {

				// Pricing event assumes first discharge in the case of an LDD cargo
				IDischargeOption firstDischarge = null;
				for (final IPortSlot ps : cargoValueAnnotation.getSlots()) {
					if (ps instanceof final IDischargeOption dOpt) {
						firstDischarge = dOpt;
						break;
					}
				}
				assert firstDischarge != null;
				pricingTime = pricingEventHelper.getLoadPricingDate((ILoadOption) portSlot, firstDischarge, cargoValueAnnotation);
			} else {
				pricingTime = pricingEventHelper.getDischargePricingDate((IDischargeOption) portSlot, cargoValueAnnotation);
			}
			final LocalDate pricingDate = externalDateProvider.getDateFromHours(pricingTime, portSlot.getPort()).toLocalDate();

			if (pricingDate.isAfter(lookupData.cutoffDate)) {
				final int cargoCV = cargoValueAnnotation.getSlotCargoCV(portSlot);

				final InputRecord inputRecord = new InputRecord(pricingDate, volumeMMBTU, cargoCV, lookupData, externalDateProvider, commodityIndices, currencyIndices);

				return calculateExposures(portSlot.getId(), pricePerMMBTU, priceExpression, isLong, inputRecord);
			}

		}

		return Collections.emptyList();
	}

	private List<BasicExposureRecord> calculateExposures(final String name, final int pricePerMMBTU, final String priceExpression, final boolean isLong, InputRecord inputRecord) {

		final List<BasicExposureRecord> result = new ArrayList<>();
		result.add(calculatePhysicalExposure(pricePerMMBTU, isLong, inputRecord));
		result.addAll(calculateFinancialExposures(name, priceExpression, isLong, inputRecord));
		return result;
	}

	/**
	 * For unit test use only
	 * 
	 * @param volumeMMBTU
	 * @param priceExpression
	 * @param pricingDate
	 * @param isLong
	 * @param lookupData
	 * @return
	 */
	public List<BasicExposureRecord> calculateExposuresForITS(final long volumeMMBTU, final String priceExpression, final LocalDate pricingDate, final boolean isLong, final int cargoCV,
			final ExposuresLookupData lookupData) {

		final List<BasicExposureRecord> result = new ArrayList<>();
		final InputRecord inputRecord = new InputRecord(pricingDate, volumeMMBTU, cargoCV, lookupData, externalDateProvider, commodityIndices, currencyIndices);
		result.addAll(calculateFinancialExposures("", priceExpression, isLong, inputRecord));
		return result;
	}

	private BasicExposureRecord calculatePhysicalExposure(final int pricePerMMBTU, final boolean isLong, InputRecord inputRecord) {

		final BasicExposureRecord physical = new BasicExposureRecord();
		final long volumeValue = Calculator.costFromVolume(inputRecord.volumeInMMBTU(), pricePerMMBTU);

		physical.setVolumeMMBTU((isLong ? 1 : -1) * inputRecord.volumeInMMBTU());
		physical.setVolumeNative((isLong ? 1 : -1) * inputRecord.volumeInMMBTU());
		physical.setUnitPrice(pricePerMMBTU);
		physical.setVolumeValueNative((isLong ? -1 : 1) * volumeValue);
		physical.setVolumeUnit("mmBtu");
		physical.setIndexName("Physical");
		physical.setTime(inputRecord.date());
		physical.setCurrencyUnit("$");

		return physical;
	}

	private List<BasicExposureRecord> calculateFinancialExposures(final String name, final String priceExpression, final boolean isLong, InputRecord inputRecord) {
		final List<BasicExposureRecord> result = new ArrayList<>();
		if (inputRecord != null) {

			final ASTNode node = inputRecord.getExposureCoefficient(priceExpression);
			if (node != null) {
				final Collection<BasicExposureRecord> records = createOptimiserExposureRecord(name, node, inputRecord, isLong);
				if (!records.isEmpty()) {
					result.addAll(records);
				}
			}
		}
		return result;
	}

	private Collection<BasicExposureRecord> createOptimiserExposureRecord(final String name, final @NonNull ASTNode node, InputRecord inputRecord, final boolean isLong) {
		final List<BasicExposureRecord> results = new LinkedList<>();
		final IExposureNode enode = ExposuresASTToCalculator.getExposureNode(node, inputRecord).getSecond();
		if (enode instanceof final ExposureRecords exposureRecords) {
			for (final ExposureRecord record : exposureRecords.records) {

				final BasicExposureRecord exposure = new BasicExposureRecord();

				exposure.setPortSlotName(name);
				exposure.setIndexName(record.curveName());
				exposure.setCurrencyUnit(record.currencyUnit());
				exposure.setVolumeUnit(record.volumeUnit());
				exposure.setTime(record.date());
				exposure.setUnitPrice(record.unitPrice());
				exposure.setVolumeMMBTU(isLong ? -record.mmbtuVolume() / 10 : record.mmbtuVolume() / 10);
				exposure.setVolumeNative(isLong ? -record.nativeVolume() / 10 : record.nativeVolume() / 10);
				exposure.setVolumeValueNative(isLong ? -record.nativeValue() / 10 : record.nativeValue() / 10);
				if (inputRecord.lookupData().commodityMap.get(record.curveName()) != null){
					exposure.setAdjustment(inputRecord.lookupData().commodityMap.get(record.curveName()).getAdjustment());
				}

				final BasicHolidayCalendar holidays = inputRecord.lookupData().holidayCalendars.get(record.curveName());
				final BasicPricingCalendar pricingCalendar = inputRecord.lookupData().pricingCalendars.get(record.curveName());
				applyProRataCorrection(pricingCalendar, holidays, exposure);
				results.add(exposure);
			}
		}
		return results;
	}

	private void applyProRataCorrection(final BasicPricingCalendar pricingCalendar, final BasicHolidayCalendar holidays, final BasicExposureRecord exposure) {
		if (pricingCalendar == null && holidays == null) {
			return;
		}
		final YearMonth month = YearMonth.from(exposure.getTime());
		BasicPricingCalendarEntry pricingEntry = null;
		if (pricingCalendar != null) {
			for (final var entry : pricingCalendar.getEntries()) {
				if (month.equals(entry.getMonth())) {
					pricingEntry = entry;
				}
			}
		}
		{
			final double proRataCorrection = getProRataCoefficient(pricingEntry, holidays, LocalDate.of(2000, 1, 1));
			final long correction = OptimiserUnitConvertor.convertToInternalConversionFactor(proRataCorrection);
			exposure.setVolumeMMBTU(ExposuresCalculatorUtils.multiplyVolumeByConstant(exposure.getVolumeMMBTU(), correction));
			exposure.setVolumeNative(ExposuresCalculatorUtils.multiplyVolumeByConstant(exposure.getVolumeNative(), correction));
			exposure.setVolumeValueNative(ExposuresCalculatorUtils.multiplyVolumeByConstant(exposure.getVolumeValueNative(), correction));
		}
	}

	private double getProRataCoefficient(final BasicPricingCalendarEntry pricingCalendarEntry, final BasicHolidayCalendar holidays, final LocalDate cutoff) {
		double i = 1.0;
		double k = 1.0;
		// workdays
		if (pricingCalendarEntry != null) {
			final LocalDate co;
			if (cutoff.isBefore(pricingCalendarEntry.getStart())) {
				co = pricingCalendarEntry.getStart();
			} else {
				co = cutoff;
			}
			for (LocalDate c = co; c.isBefore(pricingCalendarEntry.getEnd().plusDays(1)); c = c.plusDays(1)) {
				if (c.getDayOfWeek() != DayOfWeek.SATURDAY && c.getDayOfWeek() != DayOfWeek.SUNDAY) {
					i += 1.0;
				}
			}
			for (LocalDate c = pricingCalendarEntry.getStart(); c.isBefore(pricingCalendarEntry.getEnd().plusDays(1)); c = c.plusDays(1)) {
				if (c.getDayOfWeek() != DayOfWeek.SATURDAY && c.getDayOfWeek() != DayOfWeek.SUNDAY) {
					k += 1.0;
				}
			}
			if (holidays != null && !holidays.getHolidays().isEmpty()) {
				// extra holidays
				for (final LocalDate hce : holidays.getHolidays()) {
					if (hce.isAfter(co) //
							&& hce.isBefore(pricingCalendarEntry.getEnd())) {
						i -= 1.0;
					}
					if (hce.isAfter(pricingCalendarEntry.getStart()) //
							&& hce.isBefore(pricingCalendarEntry.getEnd())) {
						k -= 1.0;
					}
				}
			}
		}
		return i / k;
	}
}
