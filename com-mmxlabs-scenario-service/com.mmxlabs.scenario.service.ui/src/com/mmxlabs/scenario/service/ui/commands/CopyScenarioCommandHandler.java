/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.internal.ScenarioStorageUtil;

/**
 * @author hinton
 * 
 */
public class CopyScenarioCommandHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final Clipboard clipboard = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			if (strucSelection.size() == 1) {
				final Object element = strucSelection.getFirstElement();
				if (element instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) element;
					final URLTransfer transfer = URLTransfer.getInstance();
					
					final IScenarioService service = instance.getScenarioService();
					URI scenarioURI;
					try {
						final String componentID = Activator.getDefault().getServiceComponentID(service);
						scenarioURI = new URI("scenario", componentID, "/"+instance.getUuid(), null);

						final String scenarioURL = scenarioURI.toString();
						
						String[] tempFilePath;
						try {
							tempFilePath = new String[]{ScenarioStorageUtil.storeToTemporaryFile(instance)};
							clipboard.setContents(new Object[]{scenarioURL, tempFilePath}, new Transfer[]{transfer, FileTransfer.getInstance()});
						} catch (IOException e) {
						}
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
				
			} else {
				final ArrayList<String> tempFiles = new ArrayList<String>();
				for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
					final Object element = iterator.next();
					if (element instanceof ScenarioInstance) {
						final ScenarioInstance instance = (ScenarioInstance) element;
						try {
							tempFiles.add(ScenarioStorageUtil.storeToTemporaryFile(instance));
						} catch (IOException e) {
						}
					}
				}
				final String[] tempFilesArray = tempFiles.toArray(new String[tempFiles.size()]);
				if (tempFilesArray.length > 0) {
					System.err.println(Arrays.toString(tempFilesArray));
					clipboard.setContents(new Object[]{tempFilesArray}, new Transfer[]{FileTransfer.getInstance()});
				}
			}
			clipboard.dispose();
		}
		
		return null;
	}
}
