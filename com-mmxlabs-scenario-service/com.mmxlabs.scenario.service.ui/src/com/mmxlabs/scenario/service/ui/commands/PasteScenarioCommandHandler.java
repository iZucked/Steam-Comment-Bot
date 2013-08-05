/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.File;
import java.io.IOException;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
		final Exception exceptions[] = new Exception[1];
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();

				final Container container = getContainer(selection);

				if (container == null) {
					return;
				}

				final Clipboard clipboard = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
				try {
					if (!pasteLocal(clipboard, container)) {
						pasteFromFiles(clipboard, container);
					}
				} catch (final IOException e) {
					exceptions[0] = e;
				} finally {
					clipboard.dispose();
				}
			}
		});

		if (exceptions[0] != null) {
			throw new ExecutionException(exceptions[0].getMessage(), exceptions[0]);
		}

		return null;
	}

	private boolean pasteLocal(final Clipboard clipboard, final Container container) throws IOException {
		final Object localData = clipboard.getContents(LocalTransfer.getInstance());
		final IScenarioService service = container.getScenarioService();
		if (localData instanceof Iterable) {
			for (final Object o : (Iterable<?>) localData) {
				if (o instanceof ScenarioInstance) {
					System.err.println("Local paste " + ((ScenarioInstance) o).getName());
					service.duplicate((ScenarioInstance) o, container).setName("Copy of " + ((ScenarioInstance) o).getName());
				}
			}
			return true;
		}
		return false;
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
}
