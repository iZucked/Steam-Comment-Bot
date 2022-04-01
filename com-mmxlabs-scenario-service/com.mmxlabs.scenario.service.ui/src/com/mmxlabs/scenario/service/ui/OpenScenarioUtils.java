/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;
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

	/**
	 * Close any open editors. Return true if an editor was found. Does not try to save contents.
	 * 
	 * @param editorInput
	 * @return
	 */
	public static boolean closeEditors(final IScenarioServiceEditorInput editorInput) {
		boolean closedEditor = false;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		for (IWorkbenchWindow window : workbench.getWorkbenchWindows()) {
			for (final IWorkbenchPage page : window.getPages()) {
				final IEditorReference[] editorReferences = page.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
				if (editorReferences != null && editorReferences.length > 0) {
					closedEditor = true;
					RunnerHelper.syncExec(() -> page.closeEditors(editorReferences, false));
				}
			}
		}
		return closedEditor;
	}

	public static void openEditor(final IScenarioServiceEditorInput editorInput) throws PartInitException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				openAndReturnEditorPart(activePage, editorInput);
			}
		}
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
