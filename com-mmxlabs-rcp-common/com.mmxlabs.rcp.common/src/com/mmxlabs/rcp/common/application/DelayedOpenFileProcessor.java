/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.rcp.common.internal.Activator;


/**
 * Based on org.eclipse.ui.internal.ide.application.DelayedEventsProcessor
 * 
 * @author Simon Goodall
 * 
 */
public class DelayedOpenFileProcessor implements Listener {

	private final Queue<String> filesToOpen = new ConcurrentLinkedQueue<String>();

	public DelayedOpenFileProcessor(final Display display) {
		display.addListener(SWT.OpenDocument, this);
	}

	@Override
	public void handleEvent(final Event event) {
		final String path = event.text;
		if (path == null) {
			return;
		}

		// Record file to open
		filesToOpen.add(path);
	}

	public void processEvents(final Display display) {
		if (filesToOpen.isEmpty()) {
			return;
		}

		while (!filesToOpen.isEmpty()) {
			final String path = filesToOpen.poll();
			if (path != null) {
				openFile(display, path);
			}
		}
	}

	private void openFile(final Display display, final String path) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window == null) {
					return;
				}
				final IWorkbenchPage page = window.getActivePage();
				if (page == null) {
					return;
				}

				final IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(path));
				final IFileInfo fetchInfo = fileStore.fetchInfo();
				if (!fetchInfo.isDirectory() && fetchInfo.exists()) {

					// From eclipse IDE - should be a better match to app
//					final FileStoreEditorInput editorInput = new FileStoreEditorInput(fileStore);
//
//					final IEditorRegistry editorReg = PlatformUI.getWorkbench().getEditorRegistry();
//					final IEditorDescriptor defaultEditor = editorReg.getDefaultEditor(fileStore.getName());
//					if (defaultEditor != null) {
//						try {
//							page.openEditor(editorInput, defaultEditor.getId());
//						} catch (final PartInitException e) {
//							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
//						}
//					}
				} else {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "File not found: " + path));
				}
			}
		});
	}

}
