/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface ISelectedDataProvider {
	@NonNull
	Collection<LNGScenarioModel> getScenarioModels();

	@NonNull
	Collection<ScenarioInstance> getScenarioInstances();

	@Nullable
	ScenarioInstance getPinnedScenarioInstance();

	// Lookup methods
	@Nullable
	ScenarioInstance getScenarioInstance(EObject eObject);

	@Nullable
	LNGScenarioModel getScenarioModel(EObject eObject);

	@Nullable
	Schedule getSchedule(EObject eObject);

	boolean isPinnedObject(EObject eObject);
}