/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.models.lng.nominations.ui.editor.LocalDateSerializer;
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
	@HubFormat("MM/YYYY")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
	@JsonSerialize(using = YearMonthSerializer.class)
	public YearMonth contractMonth;

	@ColumnName("Hedge Period Start")
	@HubFormat("DD/MM/YYYY")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate hedgeStart;
	
	@ColumnName("Hedge Period End")
	@HubFormat("DD/MM/YYYY")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate hedgeEnd;

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

	public static @NonNull List<@NonNull ExposuresReportModelV2> doTransformSlot(final @NonNull List<@NonNull SlotAllocation> slotAllocations) {
		final List<ExposuresReportModelV2> result = new ArrayList<>();
		
		for (SlotAllocation slotAllocation : slotAllocations) {
			final String dealName = slotAllocation.getName();
			for (ExposureDetail exposure : slotAllocation.getExposures()) {
				result.add(createModelFromExposureDetail(dealName, exposure));
			}
		}

		return result;
	}

	public static @NonNull List<@NonNull ExposuresReportModelV2> doTransformPaper(final @NonNull List<@NonNull PaperDealAllocation> paperDealAllocations) {
		final List<ExposuresReportModelV2> result = new ArrayList<>();
		
		for (PaperDealAllocation paperDealAllocation : paperDealAllocations) {	
			List<PaperDealAllocationEntry> entries = paperDealAllocation.getEntries();
			for (PaperDealAllocationEntry paperDealEntry : entries) {
				for (ExposureDetail exposure : paperDealEntry.getExposures()) {
					final String deal = paperDealAllocation.getPaperDeal().getName() + " (Paper)";;		
					result.add(createModelFromExposureDetail(deal, exposure));
				}
			}	
		}
		
		return result;
	}

	// Utility
	private static @NonNull ExposuresReportModelV2 createModelFromExposureDetail(final String deal, final @NonNull ExposureDetail exposure) {
		ExposuresReportModelV2 row = new ExposuresReportModelV2();
		row.deal = deal;
		row.marketIndex = exposure.getIndexName();
		row.contractMonth = exposure.getDate();
		row.hedgeStart = exposure.getHedgingPeriodStart(); 
		row.hedgeEnd = exposure.getHedgingPeriodEnd(); 
		row.volume = exposure.getVolumeInNativeUnits();
		row.volumeUnit = exposure.getVolumeUnit();
		row.currencyPrice = exposure.getUnitPrice();
		row.currencyValue = exposure.getNativeValue();
		row.currencyUnit = exposure.getCurrencyUnit();
		return row;
	}
}
