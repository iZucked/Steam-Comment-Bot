/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * @author hinton
 * 
 */
public class PasteScenarioCommandHandler extends AbstractHandler {
	private static final Logger log = LoggerFactory.getLogger(PasteScenarioCommandHandler.class);

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

		final Set<String> existingNames = new HashSet<String>();
		for (final Container c : container.getElements()) {
			if (c instanceof Folder) {
				existingNames.add(((Folder) c).getName());
			} else if (c instanceof ScenarioInstance) {
				existingNames.add(((ScenarioInstance) c).getName());
			}
		}

		if (localData instanceof Iterable) {
			final Iterable<?> iterable = (Iterable<?>) localData;
			int count = IProgressMonitor.UNKNOWN;
			if (iterable instanceof Collection<?>) {
				count = ((Collection<?>) iterable).size();
			}
			final int numTasks = count;
			final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

			try {
				dialog.run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						monitor.beginTask("Copying", numTasks);
						try {
							for (final Object o : iterable) {
								if (o instanceof ScenarioInstance) {
									final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
									monitor.subTask("Copying " + scenarioInstance.getName());
									log.debug("Local paste " + scenarioInstance.getName());
									try {
										final ScenarioInstance duplicate = service.duplicate(scenarioInstance, container);
										if (duplicate != null) {

											final String namePrefix = "Copy of " + scenarioInstance.getName();
											String newName = namePrefix;
											int counter = 1;
											while (existingNames.contains(newName)) {
												newName = namePrefix + " (" + counter++ + ")";
											}

											duplicate.setName(newName);
											existingNames.add(newName);
										} else {
											log.error("Unable to paste scenario: " + scenarioInstance.getName(), new RuntimeException());
										}
									} catch (final Exception e) {
										log.error("Unable to paste scenario: " + scenarioInstance.getName(), new RuntimeException());
									}
								}
								monitor.worked(1);
							}
						} finally {
							monitor.done();
						}
					}
				});
			} catch (final InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					Container container = (Container) element;
					// Step up to a non-scenario item
					while (container != null && container instanceof ScenarioInstance) {
						container = container.getParent();
					}
					return container;
				}
			}
		}
		return null;
	}
}
