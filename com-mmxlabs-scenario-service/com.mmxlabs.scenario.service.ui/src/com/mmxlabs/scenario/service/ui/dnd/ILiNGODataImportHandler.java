/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface ILiNGODataImportHandler {

	boolean importLiNGOData(String filename, @NonNull ScenarioInstance scenarioInstance);

}
