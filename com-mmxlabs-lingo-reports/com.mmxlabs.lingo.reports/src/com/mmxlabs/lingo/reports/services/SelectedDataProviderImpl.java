/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

	private final List<ScenarioResult> scenarioResults = new LinkedList<>();
	private final Map<EObject, ScenarioResult> scenarioInstanceMap = new HashMap<>();
	private final Map<EObject, Schedule> scheduleMap = new HashMap<>();
	private ScenarioResult pinnedScenarioInstance;

	public void addScenario(@NonNull final ScenarioResult scenarioResult, @Nullable final Schedule schedule, @NonNull final Collection<EObject> children) {

		scenarioResults.add(scenarioResult);

		for (final EObject e : children) {
			scenarioInstanceMap.put(e, scenarioResult);
			scheduleMap.put(e, schedule);
		}
	}

	@Override
	public List<ScenarioResult> getScenarioResults() {
		return scenarioResults;
	}

	@Override
	public ScenarioResult getScenarioResult(final EObject eObject) {
		return scenarioInstanceMap.get(eObject);
	}

	@Override
	public Schedule getSchedule(final EObject eObject) {
		return scheduleMap.get(eObject);
	}

	@Override
	public boolean isPinnedObject(EObject eObject) {
		return Objects.equals(getScenarioResult(eObject), pinnedScenarioInstance);
	}

	public void setPinnedScenarioInstance(ScenarioResult pinnedScenarioInstance) {
		this.pinnedScenarioInstance = pinnedScenarioInstance;
	}
}
