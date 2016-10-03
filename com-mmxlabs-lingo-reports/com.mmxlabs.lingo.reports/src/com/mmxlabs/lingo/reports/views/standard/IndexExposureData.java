/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 */
public class IndexExposureData {
	public final String indexName;
	public final NamedIndexContainer<?> index;
	public final Map<YearMonth, Double> exposures;
	public final @NonNull Schedule schedule;

	public final String currencyUnit;
	public final String volumeUnit;

	public IndexExposureData(final @NonNull Schedule schedule, final String name, final NamedIndexContainer<?> index, final Map<YearMonth, Double> exposuresByMonth, final String currencyUnit,
			final String volumeUnit
	// final Map<YearMonth, Long> valueByMonth
	) {
		this.schedule = schedule;
		this.indexName = name;
		this.index = index;
		this.exposures = exposuresByMonth;
		// this.value = valueByMonth;
		this.currencyUnit = currencyUnit;
		this.volumeUnit = volumeUnit;
	}
}
