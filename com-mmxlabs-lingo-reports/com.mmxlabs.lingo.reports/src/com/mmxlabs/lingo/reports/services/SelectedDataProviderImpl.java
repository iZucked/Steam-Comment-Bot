/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

class SelectedDataProviderImpl implements ISelectedDataProvider {

	private final List<ScenarioInstance> scenarioInstances = new LinkedList<>();
	private final List<LNGScenarioModel> scenarioModels = new LinkedList<>();
	private final List<LNGPortfolioModel> portfolioModels = new LinkedList<>();

	private final Map<EObject, ScenarioInstance> scenarioInstanceMap = new HashMap<>();
	private final Map<EObject, LNGScenarioModel> scenarioModelMap = new HashMap<>();
	private final Map<EObject, LNGPortfolioModel> portfolioModelMap = new HashMap<>();
	private final Map<EObject, Schedule> scheduleMap = new HashMap<>();
	private ScenarioInstance pinnedScenarioInstance;

	public void addScenario(@NonNull final ScenarioInstance scenarioInstance, @NonNull final LNGScenarioModel scenarioModel, @NonNull final LNGPortfolioModel portfolioModel,
			@Nullable final Schedule schedule, @NonNull final Collection<EObject> children) {
		scenarioInstances.add(scenarioInstance);
		scenarioModels.add(scenarioModel);
		portfolioModels.add(portfolioModel);

		for (final EObject e : children) {
			scenarioInstanceMap.put(e, scenarioInstance);
			scenarioModelMap.put(e, scenarioModel);
			portfolioModelMap.put(e, portfolioModel);
			scheduleMap.put(e, schedule);
		}
	}

	@Override
	public Collection<LNGScenarioModel> getScenarioModels() {
		return scenarioModels;
	}

	@Override
	public Collection<LNGPortfolioModel> getPortfolioModels() {
		return portfolioModels;
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
	public LNGPortfolioModel getPortfolioModel(final EObject eObject) {
		return portfolioModelMap.get(eObject);
	}

	@Override
	public Schedule getSchedule(final EObject eObject) {
		return scheduleMap.get(eObject);
	}

	@Override
	public ScenarioInstance getPinnedScenarioInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPinnedObject(EObject eObject) {

		return getScenarioInstance(eObject) == pinnedScenarioInstance;
	}

	public void setPinnedScenarioInstance(ScenarioInstance pinnedScenarioInstance) {
		this.pinnedScenarioInstance = pinnedScenarioInstance;
	}
}
