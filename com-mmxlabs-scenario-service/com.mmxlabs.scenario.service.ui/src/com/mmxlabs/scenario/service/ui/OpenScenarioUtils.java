/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * @since 2.0
 * 
 */
public class OpenScenarioUtils {
	
	private static final Logger log = LoggerFactory.getLogger(OpenScenarioUtils.class);


	public static void openScenarioInstance(final IWorkbenchPage activePage, final ScenarioInstance model) throws PartInitException {

		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(model);

		openEditor(editorInput);
	}

	public static void openEditor(final IScenarioServiceEditorInput editorInput) throws PartInitException {

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IEditorPart editorPart = activePage.findEditor(editorInput);
		if (editorPart != null) {
			activePage.activate(editorPart);
		} else {
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
