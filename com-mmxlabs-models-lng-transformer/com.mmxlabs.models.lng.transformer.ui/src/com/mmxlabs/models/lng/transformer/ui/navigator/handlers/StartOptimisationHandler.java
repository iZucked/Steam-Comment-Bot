/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.transformer.ui.jobmanagers.LocalJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimiserTask;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class StartOptimisationHandler extends AbstractHandler {

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if (selection != null && selection instanceof IStructuredSelection strucSelection) {
			BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), () -> {
				final Iterator<?> itr = strucSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ScenarioInstance instance) {

						ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
						if (modelRecord == null) {
							return;
						}

						if (instance.isReadonly()) {
							return;
						}

						if (modelRecord.isLoadFailure()) {
							return;
						}

						OptimiserTask.submit(instance, LocalJobManager.INSTANCE);
					}
				}
			});
		}

		return null;
	}

}
