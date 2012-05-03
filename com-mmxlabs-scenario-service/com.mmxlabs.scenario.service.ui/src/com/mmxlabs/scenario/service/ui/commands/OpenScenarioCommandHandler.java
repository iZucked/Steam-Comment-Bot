/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public class OpenScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(OpenScenarioCommandHandler.class);

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

					try {
						openScenarioInstance(activePage, (ScenarioInstance) element);
					} catch (final PartInitException e) {

						MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());

						log.error(e.getMessage(), e);
					}
				}
			}
		}

		return null;
	}

	public static void openScenarioInstance(final IWorkbenchPage activePage, final ScenarioInstance model) throws PartInitException {

		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(model);

		openEditor(editorInput);
	}

	public static void openEditor(final IEditorInput editorInput) throws PartInitException {

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IEditorPart editorPart = activePage.findEditor(editorInput);
		if (editorPart != null) {
			// FIXME: This doesn't quite work through navigator double click. Editor is activated, selection provider is linked to editor. However another double click in navigator does not transfer
			// focus back - it still seems to think it has focus so does not request it. This results in the execute() obtaining the wrong selection.
			activePage.activate(editorPart);
		} else {
			final IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			// String contentTypeString = editorInput.getContentType();
			final IContentType contentType = null;// contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			final IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {
				activePage.openEditor(editorInput, descriptor.getId());
			}
		}
	}
}
