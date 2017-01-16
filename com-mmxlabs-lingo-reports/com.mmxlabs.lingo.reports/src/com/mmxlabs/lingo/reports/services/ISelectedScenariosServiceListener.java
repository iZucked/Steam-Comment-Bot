/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface ISelectedScenariosServiceListener {
	void selectionChanged(@NonNull ISelectedDataProvider selectedDataProvider, @Nullable ScenarioResult pinned, @NonNull Collection<@NonNull ScenarioResult> others, boolean block);

}
