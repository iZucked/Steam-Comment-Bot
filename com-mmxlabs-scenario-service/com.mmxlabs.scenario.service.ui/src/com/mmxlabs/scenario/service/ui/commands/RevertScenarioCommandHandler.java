/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * Command Handler to revert {@link ScenarioInstance} to last saved state.
 * 
 * @author Simon Goodall
 * 
 */
public class RevertScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(RevertScenarioCommandHandler.class);

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
						final Object element = iterator.next();
						if (element instanceof ScenarioInstance) {
							final ScenarioInstance scenarioInstance = (ScenarioInstance) element;
							final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
							if (modelRecord != null) {
								ScenarioServiceModelUtils.closeReferences(scenarioInstance);
								// Run revert in BG to free up UI thread
								ForkJoinPool.commonPool().submit(() -> modelRecord.revert());
							}
						}
					}
				}
			}
		});

		return null;
	}
}
