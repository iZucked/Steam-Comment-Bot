/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.ui.IEditorActionDelegate;

import com.mmxlabs.models.lng.transformer.ui.jobmanagers.LocalJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimiserTask;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * A {@link IEditorActionDelegate} implementation to start or resume an optimisation.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationEditorActionDelegate extends AbstractStartOptimisationEditorActionDelegate {

	@Override
	protected void doRun(ScenarioInstance instance, ScenarioModelRecord modelRecord) {
		OptimiserTask.submit(instance, LocalJobManager.INSTANCE);
	}
}
