/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.ui.IEditorInput;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * An {@link IEditorInput} similar to {@link IScenarioServiceEditorInput} but to trigger the difference mode. The "current" scenario instance is the {@link ScenarioInstance} to be edited, while the
 * "reference" scenario is the original.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceDiffingEditorInput extends IEditorInput {

	String getContentType();

	ScenarioInstance getReferenceScenarioInstance();

	ScenarioInstance getCurrentScenarioInstance();

	IDiffEditHandler getDiffEditHandler();
}
