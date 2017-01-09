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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

class SelectedDataProviderImpl implements ISelectedDataProvider {

	private final List<ScenarioInstance> scenarioInstances = new LinkedList<>();
	private final List<LNGScenarioModel> scenarioModels = new LinkedList<>();

	private final Map<ScenarioInstance, LNGScenarioModel> instanceToModelMap = new HashMap<>();
	private final Map<EObject, ScenarioInstance> scenarioInstanceMap = new HashMap<>();
	private final Map<EObject, LNGScenarioModel> scenarioModelMap = new HashMap<>();
	private final Map<EObject, Schedule> scheduleMap = new HashMap<>();
	private ScenarioInstance pinnedScenarioInstance;

	public void addScenario(@NonNull final ScenarioInstance scenarioInstance, @NonNull final LNGScenarioModel scenarioModel, @Nullable final Schedule schedule,
			@NonNull final Collection<EObject> children) {
		scenarioInstances.add(scenarioInstance);
		scenarioModels.add(scenarioModel);

		for (final EObject e : children) {
			scenarioInstanceMap.put(e, scenarioInstance);
			scenarioModelMap.put(e, scenarioModel);
			scheduleMap.put(e, schedule);
		}
		instanceToModelMap.put(scenarioInstance, scenarioModel);
	}

	@Override
	public Collection<LNGScenarioModel> getScenarioModels() {
		return scenarioModels;
	}

	@Override
	public Collection<ScenarioInstance> getScenarioInstances() {
		return scenarioInstances;
	}

	@Override
	public ScenarioInstance getScenarioInstance(final EObject eObject) {
		return scenarioInstanceMap.get(eObject);
	}

	@Override
	public LNGScenarioModel getScenarioModel(final EObject eObject) {
		return scenarioModelMap.get(eObject);
	}

	@Override
	public @NonNull LNGScenarioModel getScenarioModel(@NonNull ScenarioInstance scenarioInstance) {
		return instanceToModelMap.get(scenarioInstance);
	}

	@Override
	public Schedule getSchedule(final EObject eObject) {
		return scheduleMap.get(eObject);
	}

	@Override
	public ScenarioInstance getPinnedScenarioInstance() {
		return pinnedScenarioInstance;
	}

	@Override
	public boolean isPinnedObject(EObject eObject) {

		return getScenarioInstance(eObject) == pinnedScenarioInstance;
	}

	public void setPinnedScenarioInstance(ScenarioInstance pinnedScenarioInstance) {
		this.pinnedScenarioInstance = pinnedScenarioInstance;
	}
}
