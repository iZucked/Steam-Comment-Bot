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
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
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

public class OpenScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(OpenScenarioCommandHandler.class);

	private IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		ISelection selection = activePage.getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				if (element instanceof ScenarioInstance) {
					ScenarioInstance model = (ScenarioInstance) element;

					// ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(model);
					URIEditorInput editorInput = new URIEditorInput(URI.createURI(model.getUri()));

					try {
						openEditor(editorInput);
					} catch (PartInitException e) {

						MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());

						log.error(e.getMessage(), e);
					}
				}
			}
		}

		return null;
	}

	public void openEditor(IEditorInput editorInput) throws PartInitException {

		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(editorInput);
		if (editorPart != null) {
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, null, true);
		} else {
			// String contentTypeString = editorInput.getContentType();
			IContentType contentType = null;// contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, descriptor.getId());
			}
		}
	}
}
