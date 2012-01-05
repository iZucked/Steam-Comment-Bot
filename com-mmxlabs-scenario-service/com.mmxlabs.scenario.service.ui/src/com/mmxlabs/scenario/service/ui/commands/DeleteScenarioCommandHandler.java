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

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DeleteScenarioCommandHandler extends AbstractHandler {

	private final IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof ScenarioInstance) {
					final ScenarioInstance model = (ScenarioInstance) element;

					model.setLocked(!model.isLocked());
				}
			}
		}

		return null;
	}

	public void openEditor(final IEditorInput editorInput) throws PartInitException {

		final IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(editorInput);
		if (editorPart != null) {
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, null, true);
		} else {
			// String contentTypeString = editorInput.getContentType();
			final IContentType contentType = null;// contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			final IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, descriptor.getId());
			}
		}
	}
}
