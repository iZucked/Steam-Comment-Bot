/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData;
import com.mmxlabs.models.lng.pricing.CommodityCurve;

/**
 */
public class ExposuresReportModel {
	String dateCargo;
	List<String> indicesList;

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

	public ExposuresReportModel() {
		if (this.indicesList == null) {
			this.indicesList = new LinkedList<String>();
		}
	}

	static List<ExposuresReportModel> doTransform(final List<IndexExposureData> exposures, final List<CommodityCurve> indices) {
		final List<ExposuresReportModel> rmlist = new LinkedList<>();
		// Header
		{
			final ExposuresReportModel rm = new ExposuresReportModel();
			rm.setDateCargo("Date");
			for (final CommodityCurve ci : indices) {
				if (ci.getMarketIndex() != null) {
					rm.getIndicesList().add(ci.getMarketIndex().getName());
				}
			}
			rm.getIndicesList().add("Physical");
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

	private static ExposuresReportModel addRow(final List<CommodityCurve> indices, final IndexExposureData ied, final String rowHeader) {
		final ExposuresReportModel rm = new ExposuresReportModel();
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

}
