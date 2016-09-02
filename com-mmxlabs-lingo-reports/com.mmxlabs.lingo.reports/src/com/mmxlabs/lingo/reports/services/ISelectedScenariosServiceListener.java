/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface ISelectedScenariosServiceListener {
	void selectionChanged(@NonNull ISelectedDataProvider selectedDataProvider, @Nullable ScenarioInstance pinned, @NonNull Collection<@NonNull ScenarioInstance> others, boolean block);

}
