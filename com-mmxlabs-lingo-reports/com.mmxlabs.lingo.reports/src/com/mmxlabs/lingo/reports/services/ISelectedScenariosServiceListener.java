package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface ISelectedScenariosServiceListener {
	void selectionChanged(@NonNull ISelectedDataProvider selectedDataProvider, @Nullable ScenarioInstance pinned, @NonNull Collection<ScenarioInstance> others, boolean block);

}
