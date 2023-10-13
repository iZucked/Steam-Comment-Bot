/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.paperdeals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.paperdeals.BasicInstrumentData;
import com.mmxlabs.common.paperdeals.BasicPaperDealAllocationEntry;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.common.paperdeals.PaperDealsLookupData;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.exposures.OptimiserExposureRecords;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IInternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPaperDealDataProviderEditor;

public class PaperDealsCalculator {

	@Inject
	IPaperDealDataProviderEditor paperDealDataProvider;

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	private IExposureDataProvider exposureDataProvider;

	@Inject
	@NonNull
	private IInternalDateProvider dateProvider;

	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;

	@Inject
	@Named(SchedulerConstants.COMPUTE_PAPER_PNL)
	private boolean paperPnLEnabled;
	
	@Inject
	@Named(SchedulerConstants.INDIVIDUAL_EXPOSURES)
	private boolean individualExposures;

	@Inject
	@Named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)
	private boolean reHedgeWithPapers;
	
	@Inject
	@Named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)
	private boolean generatedPapersInPNL;

	/**
	 * Get the collection of paper deals allocation and create a map Map<Pair<Market index name, Month>, Volume> hedges
	 * 
	 * @param paperDealAllocations
	 * @return Map<Pair<String, YearMonth>, Long>
	 */
	private Map<Pair<String, YearMonth>, Long> getHedges(final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealAllocations) {
		final Map<Pair<String, YearMonth>, Long> hedges = new HashMap<>();

		if (reHedgeWithPapers) {
			final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();
			for (final BasicPaperDealData basicPaperDealData : paperDealAllocations.keySet()) {
				for (final BasicPaperDealAllocationEntry entry : paperDealAllocations.get(basicPaperDealData)) {
					final YearMonth month = YearMonth.from(entry.getDate());
					final String marketIndex = lookupData.marketIndices.get(basicPaperDealData.getIndexName().toLowerCase());
					final Pair<String, YearMonth> pair = new Pair<>(marketIndex, month);
					for (BasicExposureRecord record : entry.getExposures()) {
						hedges.merge(pair, record.getVolumeMMBTU(), Long::sum);
					}
				}
			}
		}
		return hedges;
	}

	/**
	 * Get the list of exposures and create a map of Map<Pair<Market index name, Month>, Volume> exposures
	 * 
	 * @param List<OptimiserExposureRecords>
	 *                                           exposureRecords
	 * @return Map<Pair<String, YearMonth>, Long>
	 */
	private Map<Pair<String, YearMonth>, Long> getExposures(final List<OptimiserExposureRecords> exposureRecords) {
		final Map<Pair<String, YearMonth>, Long> exposures = new HashMap<>();

		if (exposuresEnabled) {
			final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();
			for (final OptimiserExposureRecords records : exposureRecords) {
				for (final BasicExposureRecord record : records.records) {
					
					if (checkValid(lookupData, record)){
						final YearMonth month = YearMonth.from(record.getTime());
						final String marketIndex = lookupData.marketIndices.get(record.getIndexName().toLowerCase());
						final Pair<String, YearMonth> pair = new Pair<>(marketIndex, month);
						exposures.merge(pair, record.getVolumeMMBTU(), Long::sum);
					}
				}
			}
		}
		return exposures;
	}
	
	private boolean checkValid(final PaperDealsLookupData lookupData, final BasicExposureRecord record) {
		boolean result = true;
		if (individualExposures) {
			result = lookupData.slotsToInclude.contains(record.getPortSlotName());
		}
		return result && !record.getIndexName().equalsIgnoreCase("Physical");
	}

	/**
	 * Get the map of exposures per IPortSlot and create a map of Map<Pair<Market index name, Month>, Volume> exposures
	 * 
	 * @param Map<IPortSlot,
	 *                           OptimiserExposureRecords> exposureRecords
	 * @return Map<Pair<String, YearMonth>, Long>
	 */
	private Map<Pair<String, YearMonth>, Long> getExposures(Map<IPortSlot, OptimiserExposureRecords> exposureRecords) {
		final List<OptimiserExposureRecords> exposureRecordsList = new LinkedList<>();
		for (final IPortSlot portSlot : exposureRecords.keySet()) {
			exposureRecordsList.add(exposureRecords.get(portSlot));
		}
		return getExposures(exposureRecordsList);
	}

	/**
	 * Create a delta map of hedges and exposures Map<Pair<Market Index Name, Month>, Volume>
	 * 
	 * @return Map<Pair<String, YearMonth>, Long>
	 */
	private Map<Pair<String, YearMonth>, Long> getDeltaExposuresMap(final Map<Pair<String, YearMonth>, Long> exposures, final Map<Pair<String, YearMonth>, Long> hedges) {
		final Map<Pair<String, YearMonth>, Long> delta = new HashMap<>(exposures);
		if (reHedgeWithPapers && generatedPapersInPNL) {
			final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();
			for (final Pair<String, YearMonth> hedgePair : hedges.keySet()) {
				delta.merge(hedgePair, hedges.get(hedgePair), Long::sum);
			}
		}
		return delta;
	}

	// 50 mmBtu
	private final long threshold = 50_000;

	/**
	 * Generate paper deals for delta map
	 * 
	 * @return
	 */
	private List<BasicPaperDealData> generateHedges(final Map<Pair<String, YearMonth>, Long> delta) {
		final List<BasicPaperDealData> results = new LinkedList<>();
		if (reHedgeWithPapers && generatedPapersInPNL) {
			final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();
			final ExposuresLookupData exposuresLookupData = exposureDataProvider.getExposuresLookupData();
			for (final Pair<String, YearMonth> deltaPair : delta.keySet()) {
				final long exposure = delta.get(deltaPair);
				if (exposure != 0 && (exposure > threshold || exposure < -threshold)) {
					final YearMonth month = deltaPair.getSecond();
					final String indexName = deltaPair.getFirst();
					// No hedging for physical positions
					if (month == null || month.isBefore(lookupData.cutoffMonth))
						continue;
					if (indexName == null || indexName.equalsIgnoreCase("Physical") || !lookupData.indicesToHedge.contains(indexName))
						continue;

					final Map<String, String> hedgeCurves = lookupData.hedgeCurves.get(indexName.toLowerCase());
					// No hedging curves
					if (hedgeCurves == null || hedgeCurves.isEmpty())
						throw new IllegalStateException(String.format("No hedging curves for %s found. Check your market index!", indexName));
					String curveName = indexName;
					boolean isBuy = false;
					String name = String.format("%s_%s", indexName, month.toString());
					if (exposure > 0) {
						name = String.format("%s_%s", name, "sell");
						isBuy = false;
						curveName = hedgeCurves.get("bid");
					} else {
						name = String.format("%s_%s", name, "buy");
						isBuy = true;
						curveName = hedgeCurves.get("offer");
					}
					if (curveName == null) {
						throw new IllegalStateException(String.format("No hedging curve found to hedge %s. Check your market index!", curveName));
					}
					final String flatCurve = hedgeCurves.get("flat");
					if (flatCurve == null) {
						throw new IllegalStateException(String.format("No flat curve found to hedge %s. Check your market index!", flatCurve));
					}

					final LocalDate start = LocalDate.of(month.getYear(), month.getMonthValue(), 1);
					final LocalDate end = LocalDate.of(month.getYear(), month.getMonthValue(), month.lengthOfMonth());
					final int startTime = dateProvider.convertTime(start);
					final ISeries series = commodityIndices.getSeries(flatCurve).get();
					double price = series.evaluate(startTime, Collections.emptyMap()).doubleValue();
					final int paperUnitPrice = OptimiserUnitConvertor.convertToInternalPrice(price);

					final BasicInstrumentData instrument = null;
					final String type = "PERIOD_AVG";
					final String entity = "";

					final int year = month.getYear();
					final String note = "Auto generated paper deal";

					final BasicCommodityCurveData curveData = exposuresLookupData.commodityMap.get(flatCurve.toLowerCase());
					long quantity = (isBuy ? -1 : 1) * exposure;
					if (curveData.getVolumeUnit() != null || !curveData.getVolumeUnit().isEmpty() || !("mmbtu".equalsIgnoreCase(curveData.getVolumeUnit()))) {
						// Not mmBtu? then the quantity field needs conversion to native units
						for (final BasicUnitConversionData factor : exposuresLookupData.conversionMap.values()) {
							if (factor.getTo().equalsIgnoreCase("mmbtu") && factor.getFrom().equalsIgnoreCase(curveData.getVolumeUnit())) {
								quantity *= factor.getFactor();
								break;
							}
						}
					}
					final BasicPaperDealData basicPaperDealData = new BasicPaperDealData(name, isBuy, quantity, paperUnitPrice, type,//
							instrument, curveName, entity, year, note, true, month.atDay(1), start, end, start, end);
					results.add(basicPaperDealData);
				}
			}
		}

		return results;
	}

	public Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> processPaperDeals(final Map<IPortSlot, OptimiserExposureRecords> exposuresMap) {
		if (paperPnLEnabled) {
			final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealsMap = processOriginalPaperDeals();
			if (exposuresEnabled) {
				final Map<Pair<String, YearMonth>, Long> hedges = getHedges(paperDealsMap);
				final Map<Pair<String, YearMonth>, Long> exposures = getExposures(exposuresMap);
				final Map<Pair<String, YearMonth>, Long> delta = getDeltaExposuresMap(exposures, hedges);
				final List<BasicPaperDealData> generatedHedges = generateHedges(delta);
				final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> generatedPaperDealsMap = processPaperDeals(generatedHedges);
				paperDealsMap.putAll(generatedPaperDealsMap);
			}
			return paperDealsMap;
		}
		return new HashMap<>();
	}

	private Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> processPaperDeals(final List<BasicPaperDealData> paperDeals) {
		final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords = new HashMap<>();

		final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();

		for (final BasicPaperDealData basicPaperDealData : paperDeals) {
			final Object object = paperDealRecords.put(basicPaperDealData, calculateAllocations(basicPaperDealData, lookupData, true));
			assert object == null;
		}
		return paperDealRecords;
	}

	private Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> processOriginalPaperDeals() {
		final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords = new HashMap<>();

		final PaperDealsLookupData lookupData = paperDealDataProvider.getPaperDealsLookupData();

		for (final BasicPaperDealData basicPaperDealData : lookupData.paperDeals) {
			final Object object = paperDealRecords.put(basicPaperDealData, calculateAllocations(basicPaperDealData, lookupData, false));
			assert object == null;
		}
		return paperDealRecords;
	}

	private List<BasicPaperDealAllocationEntry> calculateAllocations(final BasicPaperDealData basicPaperDealData, final PaperDealsLookupData lookupData, final boolean generated) {
		final List<BasicPaperDealAllocationEntry> result = new LinkedList<>();
		final String curveName = basicPaperDealData.getIndexName();
		final Map<LocalDate, Double> settledPrices = lookupData.settledPrices.get(curveName);
		final ISeries series = commodityIndices.getSeries(curveName).get();
		final ExposuresLookupData exposuresLookupData = exposureDataProvider.getExposuresLookupData();
		final BasicCommodityCurveData curveData = exposuresLookupData.commodityMap.get(curveName);
		return settlePaper(basicPaperDealData, settledPrices, series, curveData, exposuresLookupData, generated);
	}
	
	private List<BasicPaperDealAllocationEntry> settlePaper(final BasicPaperDealData basicPaperDealData, final Map<LocalDate, Double> settledPrices, //
			final ISeries series, final BasicCommodityCurveData curveData, final ExposuresLookupData exposuresLookupData, final boolean generated) {
		final List<BasicPaperDealAllocationEntry> result = new LinkedList<>();
		final boolean isBuy = basicPaperDealData.isBuy();
		LocalDate start = basicPaperDealData.getPricingStart();
		LocalDate hedgingStart = basicPaperDealData.getHedgingStart();
		LocalDate hedgingEnd = basicPaperDealData.getHedgingEnd();
		int days = 0;
		// Count trading days
		while (!start.isAfter(basicPaperDealData.getPricingEnd())) {
			final boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;

			start = start.plusDays(1);
			if (isWeekend) {
				continue;
			}
			// If holiday continue
			++days;
		}
		start = basicPaperDealData.getPricingStart();
		// MTM Part
		// Count trading days
		while (!start.isAfter(basicPaperDealData.getPricingEnd())) {
			final boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;
			if (!isWeekend) {
				double price = series.evaluate(dateProvider.convertTime(start), Collections.emptyMap()).doubleValue();
				boolean settled = false;
				if (settledPrices != null) {
					final Double settledPrice = settledPrices.get(start);

					if (settledPrice != null) {
						price = settledPrice;
						settled = true;
					}
				}

				final int internalPrice = OptimiserUnitConvertor.convertToInternalPrice(price);
				final long quantity = days == 0 ? 0 : ((isBuy ? 1 : -1) * basicPaperDealData.getPaperVolume() / days);
				final int intSpread;
				if (generated) {
					intSpread = basicPaperDealData.getPaperUnitPrice() - internalPrice;
				} else {
					intSpread = (internalPrice - basicPaperDealData.getPaperUnitPrice());
				}
				long value = (quantity * intSpread) / 1_000_000;

				final BasicPaperDealAllocationEntry entry = 
						new BasicPaperDealAllocationEntry(start, hedgingStart, hedgingEnd, quantity, internalPrice, value, settled);

				if (!settled && exposuresEnabled) {
					final BasicExposureRecord exposure = new BasicExposureRecord();

					exposure.setIndexName(curveData.getName());
					exposure.setCurrencyUnit(curveData.getCurrencyUnit());
					exposure.setVolumeUnit(curveData.getVolumeUnit());
					exposure.setTime(start);
					exposure.setHedgingStart(hedgingStart);
					exposure.setHedgingEnd(hedgingEnd);
					exposure.setUnitPrice(intSpread);
					exposure.setVolumeNative(quantity);

					// Is the record unit in mmBtu? Then either it always was mmBtu OR we have
					// converted the native units to mmBtu
					if (curveData.getVolumeUnit() == null || curveData.getVolumeUnit().isEmpty() || "mmbtu".equalsIgnoreCase(curveData.getVolumeUnit())) {
						exposure.setVolumeMMBTU(quantity);
					} else {
						// Not mmBtu? then the mmBtu field is still really native units
						// Perform units conversion - compute mmBtu equivalent of exposed native volume
						long mmbtuVolume = quantity;
						for (final BasicUnitConversionData factor : exposuresLookupData.conversionMap.values()) {
							if (factor.getTo().equalsIgnoreCase("mmbtu")) {
								if (factor.getFrom().equalsIgnoreCase(curveData.getVolumeUnit())) {
									mmbtuVolume /= factor.getFactor();
									break;
								}
							}
						}
						exposure.setVolumeMMBTU(mmbtuVolume);
					}

					final long nativeValue = (exposure.getVolumeNative() * exposure.getUnitPrice()) / 1_000_000;
					exposure.setVolumeValueNative(nativeValue);
					entry.getExposures().add(exposure);
				}

				result.add(entry);
			}
			start = start.plusDays(1);
		}
		return result;
	}

}
