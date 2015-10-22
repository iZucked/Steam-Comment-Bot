/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.Map;

/**
 */
public class IndexExposureData {
	public final String indexName;
	public final Map<YearMonth, Double> exposures; 

	public IndexExposureData(String name, Map<YearMonth, Double> exposuresByMonth) {
		super();
		this.indexName = name;
		this.exposures = exposuresByMonth;
	}
}
