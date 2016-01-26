/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * @author hinton
 * 
 */
public class CopyScenarioCommandHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Clipboard clipboard = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;

					final ArrayList<String> tempFiles = new ArrayList<String>();
					final ArrayList<ScenarioInstance> instances = new ArrayList<ScenarioInstance>();
					for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
						final Object element = iterator.next();
						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;
							instances.add(instance);
							try {
								tempFiles.add(ScenarioStorageUtil.storeToTemporaryFile(instance));
							} catch (final IOException e) {
								e.printStackTrace();
							}
						}
					}
					final String[] tempFilesArray = tempFiles.toArray(new String[tempFiles.size()]);
					if (tempFilesArray.length > 0) {
						clipboard.setContents(new Object[] { tempFilesArray, instances }, new Transfer[] { FileTransfer.getInstance(), LocalTransfer.getInstance() });
					}
					clipboard.dispose();
				}
			}
		});
		return null;
	}
}
