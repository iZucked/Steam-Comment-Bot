/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.ui.editing.internal.ScenarioServiceDiffingEditorInput;

public class DiffEditScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(DiffEditScenarioCommandHandler.class);

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

							ScenarioInstance instance = (ScenarioInstance) element;

							// Check for existing sandbox scenario
							for (Container c : new ArrayList<Container>(instance.getElements())) {
								if (c instanceof ScenarioInstance && c.isHidden() && c.getName().startsWith("[Sandbox]")) {

									ScenarioInstance subInstance = (ScenarioInstance) c;
									EObject instance2 = subInstance.getInstance();
									if (instance2 == null) {
										// Assume not cleaned up!
										String name = "[Sandbox-Recovered] " + subInstance.getName();
										subInstance.setName(name);
										subInstance.setHidden(false);
										// subInstance.getScenarioService().delete(subInstance);
									} else {
										// Most likely already in sandbox, so ignore
										return;
									}
								}
							}

							final IScenarioService scenarioService = instance.getScenarioService();

							try {

								final Set<String> existingNames = ScenarioServiceModelUtils.getExistingNames(instance);

								final String namePrefix = "[S] " + instance.getName();
								String newName = ScenarioServiceModelUtils.getNextName(namePrefix, existingNames);

								final ScenarioInstance fork = scenarioService.duplicate(instance, instance);
								fork.setName(newName);
								fork.setHidden(true);

								openScenarioInstance(activePage, fork, instance);
							} catch (final Exception e) {

								MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());

								log.error(e.getMessage(), e);
							}
						}
					}
				}
			}
		});

		return null;
	}

	public static void openScenarioInstance(final IWorkbenchPage activePage, final ScenarioInstance current, ScenarioInstance ref) throws PartInitException {

		final ScenarioServiceDiffingEditorInput editorInput = new ScenarioServiceDiffingEditorInput(current, ref);

		openEditor(editorInput);
	}

	public static void openEditor(final ScenarioServiceDiffingEditorInput editorInput) throws PartInitException {

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		{
			final IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			final String contentTypeString = editorInput.getContentType();
			final IContentType contentType = contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			final IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {

				final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				try {
					dialog.run(false, false, new IRunnableWithProgress() {
						@Override
						public void run(final IProgressMonitor monitor) {
							monitor.beginTask("Opening editor", IProgressMonitor.UNKNOWN);
							try {
								activePage.openEditor(editorInput, descriptor.getId());
								monitor.worked(1);
							} catch (final PartInitException e) {
								log.error(e.getMessage(), e);
							} finally {
								monitor.done();
							}
						}
					});
				} catch (final InvocationTargetException e) {
					log.error(e.getMessage(), e);
				} catch (final InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
}
