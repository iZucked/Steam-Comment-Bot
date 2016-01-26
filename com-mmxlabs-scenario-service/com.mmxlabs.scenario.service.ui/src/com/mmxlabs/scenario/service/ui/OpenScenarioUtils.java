/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class OpenScenarioUtils {

	private static final Logger log = LoggerFactory.getLogger(OpenScenarioUtils.class);

	public static void openScenarioInstance(final ScenarioInstance model) throws PartInitException {

		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(model);

		openEditor(editorInput);
	}

	public static void openEditor(final IScenarioServiceEditorInput editorInput) throws PartInitException {
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		openAndReturnEditorPart(activePage, editorInput);
	}

	/**
	 */
	public static IEditorPart openAndReturnEditorPart(final IWorkbenchPage activePage, final ScenarioInstance model) throws PartInitException {
		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(model);
		return openAndReturnEditorPart(activePage, editorInput);
	}

	/**
	 */
	public static IEditorPart openAndReturnEditorPart(final IWorkbenchPage activePage, final IScenarioServiceEditorInput editorInput) throws PartInitException {

		final IEditorPart editorPart = activePage.findEditor(editorInput);
		if (editorPart != null) {
			activePage.activate(editorPart);
			return editorPart;
		} else {
			final IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			final String contentTypeString = editorInput.getContentType();
			final IContentType contentType = contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			final IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {

				final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				try {
					final IEditorPart editorRef[] = new IEditorPart[1];
					dialog.run(false, false, new IRunnableWithProgress() {
						@Override
						public void run(final IProgressMonitor monitor) {
							monitor.beginTask("Opening editor", IProgressMonitor.UNKNOWN);
							try {
								final IEditorPart openEditorPart = activePage.openEditor(editorInput, descriptor.getId());
								editorRef[0] = openEditorPart;
								monitor.worked(1);
							} catch (final PartInitException e) {
								log.error(e.getMessage(), e);
							} finally {
								monitor.done();
							}
						}
					});
					return editorRef[0];
				} catch (final InvocationTargetException e) {
					log.error(e.getMessage(), e);
				} catch (final InterruptedException e) {
					log.error(e.getMessage(), e);
				}

			}
			return null;
		}
	}
}
