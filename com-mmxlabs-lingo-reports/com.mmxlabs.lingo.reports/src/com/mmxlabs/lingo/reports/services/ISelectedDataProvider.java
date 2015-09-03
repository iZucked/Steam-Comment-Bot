package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface ISelectedDataProvider {

	Collection<LNGScenarioModel> getScenarioModels();

	Collection<LNGPortfolioModel> getPortfolioModels();

	Collection<ScenarioInstance> getScenarioInstances();

	// Lookup methods
	ScenarioInstance getScenarioInstance(EObject eObject);

	LNGScenarioModel getScenarioModel(EObject eObject);

	LNGPortfolioModel getPortfolioModel(EObject eObject);

	Schedule getSchedule(EObject eObject);
}