package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Date;
import java.util.Map;

/**
 * @since 2.0
 */
public class IndexExposureData {
	public final String indexName;
	public final Map<Date, Double> exposures; 

	public IndexExposureData(String name, Map<Date, Double> exposuresByMonth) {
		super();
		this.indexName = name;
		this.exposures = exposuresByMonth;
	}
}
