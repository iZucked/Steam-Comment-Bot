/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface ISelectedDataProvider {
	// @NonNull
	// Collection<@NonNull LNGScenarioModel> getScenarioModels();
	//
	// @NonNull
	// Collection<@NonNull ScenarioInstance> getScenarioInstances();
	//
	// @Nullable
	// ScenarioInstance getPinnedScenarioInstance();
	//
	// // Lookup methods
	@Nullable
	ScenarioResult getScenarioResult(EObject eObject);
	//
	// @Nullable
	// LNGScenarioModel getScenarioModel(EObject eObject);
	//
	// @Nullable
	// LNGScenarioModel getScenarioModel(@NonNull ScenarioInstance scenarioInstance);

	@Nullable
	Schedule getSchedule(EObject eObject);

	boolean isPinnedObject(EObject eObject);

	@NonNull
	List<ScenarioResult> getScenarioResults();
}