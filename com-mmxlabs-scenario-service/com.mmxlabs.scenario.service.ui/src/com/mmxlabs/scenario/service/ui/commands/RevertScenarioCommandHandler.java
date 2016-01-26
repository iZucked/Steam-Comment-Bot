/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;

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

							final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.EDITORS);
							if (lock.awaitClaim()) {
								try {

									// Deselect from view
									Activator.getDefault().getScenarioServiceSelectionProvider().deselect(scenarioInstance, true);

									final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
									final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);

									if (editorReferences != null && editorReferences.length > 0) {
										activePage.closeEditors(editorReferences, false);
									}

									// Set to false
									scenarioInstance.setDirty(false);
									// Force unload.
									scenarioInstance.unload();

									if (editorReferences != null && editorReferences.length > 0) {
										// scenarioInstance.getScenarioService().load(scenarioInstance);
//										OpenScenarioUtils.openScenarioInstance(activePage, scenarioInstance);
									}
//								} catch (final PartInitException e) {
//									log.error(e.getMessage(), e);
								} finally {
									lock.release();
								}
							}
						}
					}
				}
			}
		});

		return null;
	}
}
