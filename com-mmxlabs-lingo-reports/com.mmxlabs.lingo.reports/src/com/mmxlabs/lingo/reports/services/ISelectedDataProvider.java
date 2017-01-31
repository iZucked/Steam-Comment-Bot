/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

	// Lookup methods
	@Nullable
	ScenarioResult getScenarioResult(EObject eObject);

	@Nullable
	Schedule getSchedule(EObject eObject);

	boolean isPinnedObject(EObject eObject);

	@NonNull
	List<ScenarioResult> getScenarioResults();
}