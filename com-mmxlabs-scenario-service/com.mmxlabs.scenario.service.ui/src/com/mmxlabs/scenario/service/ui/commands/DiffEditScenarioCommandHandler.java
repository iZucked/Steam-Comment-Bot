/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceDiffingEditorInput;

public class DiffEditScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(DiffEditScenarioCommandHandler.class);

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof ScenarioInstance) {

					ScenarioInstance instance = (ScenarioInstance) element;
					final IScenarioService scenarioService = instance.getScenarioService();

					try {
						final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

						final Set<String> existingNames = new HashSet<String>();
						for (final Container c : instance.getElements()) {
							if (c instanceof Folder) {
								existingNames.add(((Folder) c).getName());
							} else if (c instanceof ScenarioInstance) {
								existingNames.add(((ScenarioInstance) c).getName());
							}
						}

						final String namePrefix = "DiffEdit Fork " + df.format(new Date()) + " - " + instance.getName();
						String newName = namePrefix;
						int counter = 1;
						while (existingNames.contains(newName)) {
							newName = namePrefix + " (" + counter++ + ")";
						}
						final ScenarioInstance fork = scenarioService.duplicate(instance, instance);
						fork.setName(newName);
						// fork.setHidden(true);

						openScenarioInstance(activePage, fork, instance);
					} catch (final Exception e) {

						MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());

						log.error(e.getMessage(), e);
					}
				}
			}
		}

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
