/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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

import com.google.common.io.Files;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 * @author hinton
 * 
 */
public class PasteScenarioCommandHandler extends AbstractHandler {
	private static final Logger log = LoggerFactory.getLogger(PasteScenarioCommandHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		final Exception[] exceptions = new Exception[1];
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), () -> {
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
		});

		if (exceptions[0] != null) {
			throw new ExecutionException(exceptions[0].getMessage(), exceptions[0]);
		}

		return null;
	}

	private boolean pasteLocal(final Clipboard clipboard, final @NonNull Container container) throws IOException {
		final Object localData = clipboard.getContents(LocalTransfer.getInstance());

		final Set<String> existingNames = ScenarioServiceUtils.getExistingNames(container);

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
					public void run(final IProgressMonitor parentMonitor) throws InvocationTargetException, InterruptedException {

						SubMonitor monitor = SubMonitor.convert(parentMonitor);
						monitor.beginTask("Copying", 3 * numTasks);
						try {
							for (final Object o : iterable) {
								if (o instanceof ScenarioInstance) {
									final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
									monitor.subTask("Copying " + scenarioInstance.getName());
									log.debug("Local paste " + scenarioInstance.getName());
									try {
										final ScenarioInstance duplicate = ScenarioServiceUtils.copyScenario(scenarioInstance, container, existingNames, monitor.split(2));
										if (duplicate != null) {
											existingNames.add(duplicate.getName());
										}
									} catch (final Exception e) {
										log.error("Unable to paste scenario: " + scenarioInstance.getName(), e);
									}
								}
								monitor.worked(1);
							}
						} finally {
							monitor.done();
						}
					}
				});
			} catch (final InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	private void scanTree(final File root, final java.util.List<File> scenarioFiles, final Container container, final Map<File, Container> scenarioContainerMap) {

		@Nullable
		final File[] listFiles = root.listFiles();
		if (listFiles != null) {
			for (final File f : listFiles) {
				if (f == null) {
					continue;
				}
				if (f.isFile() && Files.getFileExtension(f.getName()).equalsIgnoreCase("lingo")) {
					scenarioFiles.add(f);
					scenarioContainerMap.put(f, container);
				} else if (f.isDirectory()) {
					// Check for existing dir in contents and reuse!
					Folder folder = null;
					for (final Container c : container.getElements()) {
						if (c instanceof Folder && c.getName().equals(f.getName())) {
							folder = (Folder) c;
							break;
						}
					}
					if (folder == null) {
						folder = ScenarioServiceFactory.eINSTANCE.createFolder();
						folder.setName(f.getName());
						container.getElements().add(folder);
					}
					scanTree(f, scenarioFiles, folder, scenarioContainerMap);
				}
			}
		}
	}

	/**
	 * @param clipboard
	 * @param container
	 * @throws IOException
	 */
	private boolean pasteFromFiles(final Clipboard clipboard, final Container container) throws IOException {
		final Object fileData = clipboard.getContents(FileTransfer.getInstance());
		if (fileData instanceof String[]) {
			return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				final String[] files = (String[]) fileData;

				// Scan tree creating folder structure and gathering scenarios.
				final List<File> scenarioFiles = new LinkedList<>();
				final Map<File, Container> scenarioContainerMap = new HashMap<>();
				for (final String filePath : files) {
					final File f = new File(filePath);
					if (f.isFile() && Files.getFileExtension(f.getName()).equalsIgnoreCase("lingo")) {
						scenarioFiles.add(f);
						scenarioContainerMap.put(f, container);
					} else if (f.isDirectory()) {
						// Check for existing dir in contents and reuse!
						Folder folder = null;
						for (final Container c : container.getElements()) {
							if (c instanceof Folder && c.getName().equals(f.getName())) {
								folder = (Folder) c;
								break;
							}
						}
						if (folder == null) {
							folder = ScenarioServiceFactory.eINSTANCE.createFolder();
							folder.setName(f.getName());
							container.getElements().add(folder);
						}
						scanTree(f, scenarioFiles, folder, scenarioContainerMap);
					}
				}

				try {
					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
					dialog.run(true, true, new IRunnableWithProgress() {

						@Override
						public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

							SubMonitor m = SubMonitor.convert(monitor, "Copying", 10 * scenarioFiles.size());
							try {

								for (final File f : scenarioFiles) {

									if (m.isCanceled()) {
										break;
									}

									m.subTask("Copying " + f.getName());
									final Container destinationContainer = scenarioContainerMap.get(f);
									assert destinationContainer != null;
									// Get basic name
									final String scenarioName = ScenarioServiceUtils.stripFileExtension(f.getName());
									final Set<String> existingNames = ScenarioServiceUtils.getExistingNames(destinationContainer);

									ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(f.toURL(), (modelRecord, modelReference) -> {
										ScenarioServiceUtils.copyScenario(modelRecord, destinationContainer, scenarioName, existingNames, m.split(4));
									}, m.split(5));

									m.worked(1);

								}
							} catch (final Exception e) {
								log.error(e.getMessage(), e);
							} finally {
								m.done();
							}
						}
					});
				} catch (final InvocationTargetException | InterruptedException e) {
					log.error(e.getMessage(), e);
				}
				return true;
			});
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
