/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * Generic content provider for simple table views.
 * 
 * @author Simon McGregor
 * 
 */
@NonNullByDefault
public interface AbstractSimpleTabularReportTransformer<T> {

	List<ColumnManager<T>> getColumnManagers(ISelectedDataProvider selectedDataProvider);

	List<T> createData(@Nullable Pair<Schedule, ScenarioResult> pinnedPair, List<Pair<Schedule, ScenarioResult>> otherPairs);
}
