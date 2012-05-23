/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class DeleteScenarioCommandHandler extends AbstractHandler {
	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final ArrayList<Container> itemsToDelete = new ArrayList<Container>();
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof Container) {
					final Container container = (Container) element;

					itemsToDelete.add(container);
				}
			}

			final List<EObject> filtered = EcoreUtil.filterDescendants(itemsToDelete);
			int totalChildCount = 0;
			for (final EObject object : filtered) {
				totalChildCount += ((Container) object).getContainedInstanceCount();
			}

			if (totalChildCount > 0) {
				final MessageDialog dialog = new MessageDialog(HandlerUtil.getActiveShell(event), "Delete selection and contents?", null,
						"Do you really want to delete the selection and all and its contents (" + totalChildCount + " scenarios)", MessageDialog.CONFIRM, new String[] { "Don't Delete", "Delete" }, 0);
				if (dialog.open() != 1) {
					return null;
				}
			}
			for (final EObject object : filtered) {
				final Container container = (Container) object;

				if (container instanceof ScenarioInstance) {
					final ScenarioInstance scenarioInstance = (ScenarioInstance) container;
					if (scenarioInstance.getInstance() != null) {

						// Deselect from view
						Activator.getDefault().getScenarioServiceSelectionProvider().deselect(scenarioInstance);

						final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
						final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
						// TODO: Prompt to save?
						activePage.closeEditors(editorReferences, false);
					}
				}

				final IScenarioService service = container.getScenarioService();
				service.delete(container);
			}
		}

		return null;
	}

	@Override
	public void setEnabled(final Object evaluationContext) {
		boolean enabled = false;
		if (evaluationContext instanceof IEvaluationContext) {
			final IEvaluationContext context = (IEvaluationContext) evaluationContext;
			final Object defaultVariable = context.getDefaultVariable();

			if (defaultVariable instanceof List<?>) {
				final List<?> variables = (List<?>) defaultVariable;

				for (final Object var : variables) {
					if (var instanceof ScenarioInstance || var instanceof Folder) {
						enabled = true;
					} else {
						super.setBaseEnabled(false);
						return;
					}
				}
			}
		}

		super.setBaseEnabled(enabled);
	}
}
