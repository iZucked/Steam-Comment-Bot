/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
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
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
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
								"Do you really want to delete the selection and all and its contents (" + totalChildCount + " scenarios)? Note: this action cannot be undone", MessageDialog.CONFIRM,
								new String[] { "Don't Delete", "Delete" }, 1);
						if (dialog.open() != 1) {
							return;
						}
					}
					final List<ScenarioInstance> scenarios = new LinkedList<>();

					// Find scenarios to save/close editor
					final List<EObject> search = new LinkedList<>(filtered);
					while (!search.isEmpty()) {
						final Container container = (Container) search.remove(0);
						if (container instanceof ScenarioInstance) {
							final ScenarioInstance scenarioInstance = (ScenarioInstance) container;
							// Insert at start of list so that children get deleted before parents.
							scenarios.add(0, scenarioInstance);
						} else {
							// Only search non-scenario instances as delete scenario will also delete children
						}
						search.addAll(container.getElements());
					}
					for (final ScenarioInstance scenarioInstance : scenarios) {
						if (scenarioInstance.getInstance() != null) {

							// Deselect from view
							Activator.getDefault().getScenarioServiceSelectionProvider().deselect(scenarioInstance);

							final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
							final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
							// TODO: Prompt to save?
							activePage.closeEditors(editorReferences, false);
						}
					}

					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

					try {
						dialog.run(true, true, new IRunnableWithProgress() {

							@Override
							public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

								monitor.beginTask("Deleting", scenarios.size() + 1);
								try {

									// Delete scenario instances first
									for (final ScenarioInstance scenarioInstance : scenarios) {
										monitor.subTask("Deleting " + scenarioInstance.getName());

										final IScenarioService service = scenarioInstance.getScenarioService();
										service.delete(scenarioInstance);
										filtered.remove(scenarioInstance);
										monitor.worked(1);
									}
									// Finally delete remainder (should be folders)
									for (final EObject object : filtered) {
										final Container container = (Container) object;
										monitor.subTask("Deleting " + container.getName());

										final IScenarioService service = container.getScenarioService();
										service.delete(container);
									}
									monitor.worked(1);
								} finally {
									monitor.done();
								}
							}
						});
					} catch (final InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		return null;
	}
}
