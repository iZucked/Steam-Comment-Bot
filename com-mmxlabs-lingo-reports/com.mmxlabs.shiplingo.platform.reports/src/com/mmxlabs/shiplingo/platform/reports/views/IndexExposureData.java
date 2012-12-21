package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Map;

import com.mmxlabs.shiplingo.platform.reports.Exposures.MonthYear;

/**
 * @since 2.0
 */
public class IndexExposureData {
	public final String indexName;
	public final Map<MonthYear, Double> exposures; 

	public IndexExposureData(String name, Map<MonthYear, Double> exposuresByMonth) {
		super();
		this.indexName = name;
		this.exposures = exposuresByMonth;
	}
}
