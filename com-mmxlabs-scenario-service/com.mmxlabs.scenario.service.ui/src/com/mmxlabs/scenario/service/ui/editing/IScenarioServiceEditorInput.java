/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.ui.IEditorInput;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioServiceEditorInput extends IEditorInput {

	String getContentType();
	
	ScenarioInstance getScenarioInstance();
}
