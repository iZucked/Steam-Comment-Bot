/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class SelectedDataProviderImpl implements ISelectedDataProvider {

	private final @NonNull List<ScenarioResult> scenarioResults = new LinkedList<>();
	private final @NonNull Map<EObject, ScenarioResult> scenarioResultMap = new HashMap<>();
	private final @NonNull Map<EObject, Schedule> scheduleMap = new HashMap<>();
	private ScenarioResult pinnedScenarioResult;

	public void addScenario(@NonNull final ScenarioResult scenarioResult, @Nullable final Schedule schedule, @NonNull final Collection<EObject> children) {

		scenarioResults.add(scenarioResult);
		for (final EObject e : children) {
			scenarioResultMap.put(e, scenarioResult);
			scheduleMap.put(e, schedule);
		}
	}

	@Override
	public List<ScenarioResult> getScenarioResults() {
		return scenarioResults;
	}

	@Override
	public ScenarioResult getScenarioResult(final EObject eObject) {
		return scenarioResultMap.get(eObject);
	}

	@Override
	public Schedule getSchedule(final EObject eObject) {
		return scheduleMap.get(eObject);
	}

	@Override
	public boolean isPinnedObject(final EObject eObject) {
		return Objects.equals(getScenarioResult(eObject), pinnedScenarioResult);
	}

	@Override
	public ScenarioResult getPinnedScenarioResult() {
		return pinnedScenarioResult;
	}

	public void setPinnedScenarioInstance(final @Nullable ScenarioResult pinnedScenarioResult) {
		this.pinnedScenarioResult = pinnedScenarioResult;
	}
}
