/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.google.common.io.Files;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubColumnStyle;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubType;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 */
public class ExposuresReportModelV2 {
	@ColumnName("Deal")
	public String deal;
	
	@ColumnName("Index")
	public String marketIndex;
	
	@ColumnName("Contract Month")
	public String contractMonth;
	
	@ColumnName("Hedge Period Start")
	public String hedgeStart;
	
	@ColumnName("Hedge Period End")
	public String hedgeEnd;
	
	@ColumnName("Volume")
	public Double volume;
	
	@ColumnName("Volume Unit")
	public String volumeUnit;
	
	@ColumnName("Currency Price")
	public Double currencyPrice;
	
	@ColumnName("Currency Value")
	public Double currencyValue;
	
	@ColumnName("Currency Unit")
	public String currencyUnit;

	public ExposuresReportModelV2() {
		
	}

	public static List<ExposuresReportModelV2> doTransform(List<SlotAllocation> slotAllocations) {
		List<ExposuresReportModelV2> result = new ArrayList<>();
		
		for (SlotAllocation slotAllocation : slotAllocations) {
			String dealName = slotAllocation.getName();
			for (ExposureDetail exposure : slotAllocation.getExposures()) {
				result.add(createModelFromExposureDetail(dealName, exposure));
			}
		}

		return result;
	}

	public static Collection<? extends ExposuresReportModelV2> doTransformPaper(List<PaperDealAllocation> paperDealAllocations) {
		List<ExposuresReportModelV2> result = new ArrayList<>();
		
		for (PaperDealAllocation paperDealAllocation : paperDealAllocations) {	
			List<PaperDealAllocationEntry> entries = paperDealAllocation.getEntries();
			for (PaperDealAllocationEntry paperDealEntry : entries) {
				for (ExposureDetail exposure : paperDealEntry.getExposures()) {
					String deal = paperDealAllocation.getPaperDeal().getName() + " (Paper)";;		
					result.add(createModelFromExposureDetail(deal, exposure));
				}
			}	
		}
		
		return result;
	}
	
	// Utility
	private static ExposuresReportModelV2 createModelFromExposureDetail(String deal, ExposureDetail exposure) {
		ExposuresReportModelV2 row = new ExposuresReportModelV2();
		row.deal = deal;
		row.marketIndex = exposure.getIndexName();
		row.contractMonth = String.format("%02d/%02d", exposure.getDate().getMonthValue(), exposure.getDate().getYear());
		row.hedgeStart = String.format("%02d-%02d-%02d",
				exposure.getHedgingPeriodStart().getYear(),
				exposure.getHedgingPeriodStart().getMonthValue(),
				exposure.getHedgingPeriodStart().getDayOfMonth());
		row.hedgeEnd = String.format("%02d-%02d-%02d",
				exposure.getHedgingPeriodEnd().getYear(),
				exposure.getHedgingPeriodEnd().getMonthValue(),
				exposure.getHedgingPeriodEnd().getDayOfMonth());
		row.volume = exposure.getVolumeInNativeUnits();
		row.volumeUnit = exposure.getVolumeUnit();
		row.currencyPrice = exposure.getUnitPrice();
		row.currencyValue = exposure.getNativeValue();
		row.currencyUnit = exposure.getCurrencyUnit();
		return row;
	}
}
