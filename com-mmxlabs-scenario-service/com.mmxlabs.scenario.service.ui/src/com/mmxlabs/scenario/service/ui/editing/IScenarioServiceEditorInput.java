/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.ui.IEditorInput;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * An {@link IEditorInput} for {@link ScenarioInstance}s.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceEditorInput extends IEditorInput {

	/**
	 * Returns the content type of this editor input. Used to find a compatible editor.
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * Returns the {@link ScenarioInstance} to be edited.
	 * 
	 * @return
	 */
	ScenarioInstance getScenarioInstance();
}
