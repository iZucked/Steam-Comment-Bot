package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IVesselImportCommandProvider {
	public void run(@NonNull final ScenarioInstance scenarioInstance);
}
