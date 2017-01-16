/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface IScenarioComparisonServiceListener {
	void diffOptionChanged(EDiffOption d, Object oldValue, Object newValue);

	void compareDataUpdate(@NonNull ISelectedDataProvider selectedDataProvider, @NonNull ScenarioResult pin, @NonNull ScenarioResult other, @NonNull Table table,
			@NonNull List<LNGScenarioModel> rootObjects, @NonNull Map<EObject, Set<EObject>> equivalancesMap);

	void multiDataUpdate(@NonNull ISelectedDataProvider selectedDataProvider, @NonNull Collection<ScenarioResult> others, @NonNull Table table, @NonNull List<LNGScenarioModel> rootObjects);
}