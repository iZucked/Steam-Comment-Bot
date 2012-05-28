/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * @author hinton
 * 
 */
public class PasteScenarioCommandHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();

		final Container container = getContainer(selection);

		if (container == null) {
			return null;
		}

		final Clipboard clipboard = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
		try {
			if (!pasteFromURI(clipboard, container)) {
				pasteFromFiles(clipboard, container);
			}
		} catch (final IOException e) {
			throw new ExecutionException(e.getMessage(), e);
		} finally {
			clipboard.dispose();
		}

		return null;
	}

	/**
	 * @param clipboard
	 * @param container
	 * @throws IOException
	 */
	private boolean pasteFromFiles(final Clipboard clipboard, final Container container) throws IOException {
		final Object fileData = clipboard.getContents(FileTransfer.getInstance());
		final IScenarioService service = container.getScenarioService();
		if (fileData instanceof String[]) {
			final String[] files = (String[]) fileData;
			for (final String filePath : files) {
				final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromFile(filePath);
				if (instance != null) {
					service.duplicate(instance, container).setName(new File(filePath).getName());
				}
			}
		}
		return false;
	}

	/**
	 * @param selection
	 * @return
	 */
	private Container getContainer(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof Container) {
					return (Container) element;
				}
			}
		}
		return null;
	}

	/**
	 * @param clipboard
	 * @param container
	 * @throws IOException
	 */
	private boolean pasteFromURI(final Clipboard clipboard, final Container container) throws IOException {
		final String url = (String) clipboard.getContents(URLTransfer.getInstance());
		if (url == null)
			return false;
		try {
			final URI uri = new URI(url);

			if (uri.getScheme().equals("scenario")) {
				final String serviceName = uri.getHost();
				final String scenarioUUID = uri.getPath().substring(1);

				final IScenarioService service = Activator.getDefault().getServiceForComponentID(serviceName);
				if (service != null) {
					final ScenarioInstance instance = service.getScenarioInstance(scenarioUUID);
					if (instance != null) {
						// duplicate insert into selected container

						final IScenarioService targetService = container.getScenarioService();
						targetService.duplicate(instance, container).setName("Copy of " + instance.getName());
						return true;
					}
				}
			}
		} catch (final URISyntaxException e) {
		}
		return false;
	}
}
