/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class MergeScenarioCommandHandler extends AbstractHandler {

	public MergeScenarioCommandHandler() {
		//Feature for this is called "merge-tool"...
	}
	
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}
		
		ScenarioInstance targetScenario = this.getSelectedScenarioInstance(event);
		
		MergeScenarioWizard wizard = new MergeScenarioWizard(targetScenario);
		IStructuredSelection selectionToPass = getSelectionToUse(event);
		wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);
		wizard.setForcePreviousAndNextButtons(true);

		Shell parent = activeWorkbenchWindow.getShell();
		WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();

		//Must be null.
		return null;
	}

	protected ScenarioInstance getSelectedScenarioInstance(final ExecutionEvent event) {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object element = itr.next();

				if (element instanceof ScenarioInstance) {
					return (ScenarioInstance) element;
				}
			}
		}

		return null;
	}
	
	protected IStructuredSelection getSelectionToUse(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		}
		return selectionToPass;
	}
}
