/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class TransformedSelectedDataProvider implements ISelectedDataProvider {

	private ISelectedDataProvider selectedDataProvider;

	private final Map<Object, ScenarioResult> scenarioResultMap = new HashMap<>();
	private final Map<Object, Schedule> scheduleMap = new HashMap<>();

	public TransformedSelectedDataProvider(ISelectedDataProvider selectedDataProvider) {
		this.selectedDataProvider = selectedDataProvider;

	}

	@Override
	public @Nullable ScenarioResult getScenarioResult(EObject eObject) {
		if (selectedDataProvider != null) {
			return selectedDataProvider.getScenarioResult(eObject);
		}
		return null;
	}

	@Override
	public @Nullable Schedule getSchedule(EObject eObject) {
		if (selectedDataProvider != null) {
			return selectedDataProvider.getSchedule(eObject);
		}
		return null;
	}

	@Override
	public boolean isPinnedObject(EObject eObject) {
		if (selectedDataProvider != null) {
			return selectedDataProvider.isPinnedObject(eObject);
		}
		return false;
	}

	@Override
	public ScenarioResult getPinnedScenarioResult() {
		return selectedDataProvider.getPinnedScenarioResult();
	}

	public @Nullable ScenarioResult getScenarioResult(Object obj) {
		if (scenarioResultMap.containsKey(obj)) {
			return scenarioResultMap.get(obj);
		}
		if (obj instanceof EObject) {
			return getScenarioResult((EObject) obj);
		}
		return null;
	}

	public @Nullable Schedule getSchedule(Object obj) {
		if (scheduleMap.containsKey(obj)) {
			return scheduleMap.get(obj);
		}
		if (obj instanceof EObject) {
			return getSchedule((EObject) obj);
		}
		return null;
	}

	public boolean isPinnedObject(Object obj) {
		return Objects.equals(getScenarioResult(obj), getPinnedScenarioResult());

	}

	@Override
	public @NonNull List<ScenarioResult> getScenarioResults() {
		if (selectedDataProvider != null) {
			return selectedDataProvider.getScenarioResults();
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	public void addExtraData(@NonNull Object obj, @NonNull final ScenarioResult scenarioResult, @Nullable final Schedule schedule) {
		scenarioResultMap.put(obj, scenarioResult);
		scheduleMap.put(obj, schedule);
	}
}
