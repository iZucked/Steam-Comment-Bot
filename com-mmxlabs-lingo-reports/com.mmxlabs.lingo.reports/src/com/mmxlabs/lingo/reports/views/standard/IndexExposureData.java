/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.Map;

import org.joda.time.YearMonth;

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
