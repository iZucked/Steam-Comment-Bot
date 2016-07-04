/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;

/**
 */
public class IndexExposureData {
	public final String indexName;
	public final Map<YearMonth, Double> exposures;
	public final @NonNull Schedule schedule;

	public IndexExposureData(final @NonNull Schedule schedule, final String name, final Map<YearMonth, Double> exposuresByMonth) {
		this.schedule = schedule;
		this.indexName = name;
		this.exposures = exposuresByMonth;
	}
}
