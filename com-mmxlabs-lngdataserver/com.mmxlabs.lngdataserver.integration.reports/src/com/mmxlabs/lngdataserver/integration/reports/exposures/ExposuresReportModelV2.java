/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import com.google.common.io.Files;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubColumnStyle;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubType;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData;
import com.mmxlabs.models.lng.pricing.CommodityCurve;

/**
 */
public class ExposuresReportModelV2 {
	@ColumnName("Date")
	@HubColumnStyle("""
			{
				"row": [ {
					"condition":"matchesRegex",
					"value":"^[0-9][0-9][0-9][0-9]-[0-9][0-9]$",
					"styling": [ {
						"property":"background-color",
						"value":"rgb(224, 255, 255)"
					} ]
				} ]
			}""")
	public String dateCargo;
	
	@ColumnName("Market Index")
	@HubType("number")
	public List<String> indicesList;
	
	@ColumnName("Load Slot")
	public String loadSlot;
	
	@ColumnName("Discharge Slot")
	public String dischargeSlot;
	
	@ColumnName("Volume")
	@HubType("number")
	public int volume;
	
	@ColumnName("Unit")
	public String volumeUnit;
	
	@ColumnName("Price")
	@HubType("number")
	public int price;
	
	@ColumnName("Value")
	@HubType("number")
	public int value;
	
	@ColumnName("Unit")
	public String currencyUnit;
	
	public String getLoadSlot() {
		return loadSlot;
	}

	public void setLoadSlot(String loadSlot) {
		this.loadSlot = loadSlot;
	}

	public String getDischargeSlot() {
		return dischargeSlot;
	}

	public void setDischargeSlot(String dischargeSlot) {
		this.dischargeSlot = dischargeSlot;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public String getDateCargo() {
		return this.dateCargo;
	}

	public void setDateCargo(String dateCargo) {
		this.dateCargo = dateCargo;
	}


	public List<String> getIndicesList() {
		return this.indicesList;
	}

	public void setIndicesList(List<String> list) {
		this.indicesList = list;
	}

	public ExposuresReportModelV2() {
		if (this.indicesList == null) {
			this.indicesList = new LinkedList<>();
		}
	}

	static List<ExposuresReportModelV2> doTransform(final List<IndexExposureData> exposures, final List<CommodityCurve> indices) {
		final List<ExposuresReportModelV2> rmlist = new LinkedList<>();
		// Header
		{
			final ExposuresReportModelV2 rm = new ExposuresReportModelV2();
			rm.setDateCargo("Date");
			for (final CommodityCurve ci : indices) {
				if (ci.getMarketIndex() != null) {
					rm.getIndicesList().add(ci.getMarketIndex().getName());
				}
			}
			rmlist.add(rm);
		}
		// data
		for (final IndexExposureData ied : exposures) {
			final String date = String.format("%04d-%02d", ied.date.getYear(), ied.date.getMonthValue());
			rmlist.add(addRow(indices, ied, date));

			if (ied.children != null) {
				for (final IndexExposureData iedChild : ied.children) {
					rmlist.add(addRow(indices, iedChild, iedChild.childName));
				}
			}
		}
		return rmlist;
	}

	private static ExposuresReportModelV2 addRow(final List<CommodityCurve> indices, final IndexExposureData ied, final String rowHeader) {
		final ExposuresReportModelV2 rm = new ExposuresReportModelV2();
		rm.setDateCargo(rowHeader);
		for (final CommodityCurve ci : indices) {
			if (ci.getMarketIndex() != null) {
				final String indexName = ci.getMarketIndex().getName();
				if (ied.exposures.containsKey(indexName)) {
					String result = String.format("%,.01f", ied.exposures.get(indexName));
					rm.getIndicesList().add(result);
				} else {
					rm.getIndicesList().add("");
				}
			}
		}
		{
			final String indexName = "Physical";
			if (ied.exposures.containsKey(indexName)) {
				String result = String.format("%,.01f", ied.exposures.get(indexName));
				rm.getIndicesList().add(result);
			} else {
				rm.getIndicesList().add("");
			}
		}
		return rm;
	}

	public static void main(String[] args) throws Exception {
		String schema = new SchemaGenerator().generateHubSchema(ExposuresReportModelV2.class, Mode.SUMMARY);
		System.out.println(schema);
		Files.write(schema, new File("target/expo.json"), StandardCharsets.UTF_8);
	}
}
