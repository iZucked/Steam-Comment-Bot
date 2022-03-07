/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * DND handler to allow element moving in the scenario navigator. This needs
 * more support to handle copy actions and to allow scenarios to be dragged in
 * from e.g. filesystem.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioDragAssistant extends CommonDropAdapterAssistant {

	private static final Logger log = LoggerFactory.getLogger(ScenarioDragAssistant.class);

	@Override
	public IStatus validateDrop(final Object target, final int operation, final TransferData transferType) {
		if (!(operation == DND.DROP_MOVE || operation == DND.DROP_COPY)) {
			return Status.CANCEL_STATUS;
		}

		// Handle local "within eclipse" transfer
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType) && (target instanceof Container || target == null)) {
			final ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
			if (selection instanceof IStructuredSelection ss) {
				final HashSet<EObject> containers = new HashSet<>();
				EObject eTarget = (EObject) target;
				if (target instanceof ScenarioService) {
					return Status.CANCEL_STATUS;
				}
				if (target instanceof ScenarioInstance) {
					// Could open up to multiple?
					if (!(ss.size() == 1 && (ss.getFirstElement() instanceof ScenarioFragment || ss.getFirstElement() instanceof IChangeSource))) {
						return Status.CANCEL_STATUS;
					}
				}
				while (eTarget != null) {
					containers.add(eTarget);
					eTarget = eTarget.eContainer();
				}
				for (final Object o : ((IStructuredSelection) selection).toArray()) {
					if (o instanceof ScenarioFragment) {
						if (!(target instanceof ScenarioInstance)) {
							return Status.CANCEL_STATUS;

						}
					}
					if (o instanceof EObject) {
						// since containers contains the full hierarchy above the drop target, if o is
						// in that hierarchy then we are dragging
						// something into something which it contains, so cancel the drop
						if (containers.contains(o))
							return Status.CANCEL_STATUS;
					}
				}
			}
			return Status.OK_STATUS;
		}
		// Handle e.g. desktop to application DND
		else if (FileTransfer.getInstance().isSupportedType(transferType) && ((target instanceof ScenarioService || target instanceof Folder) || target == null)) {
			final Object obj = FileTransfer.getInstance().nativeToJava(transferType);

			if (!(target instanceof Folder || target instanceof ScenarioService)) {
				return Status.CANCEL_STATUS;
			}

			if (obj instanceof String[] files) {

				for (final String filePath : files) {
					// Expect this type of extension
					final String lowerFilePath = filePath.toLowerCase();
					if (!lowerFilePath.endsWith(".lingo")) {
						return Status.CANCEL_STATUS;

					}
				}

			}

			return Status.OK_STATUS;
		} else if (FileTransfer.getInstance().isSupportedType(transferType) && (target instanceof ScenarioInstance || target == null)) {
			final Object obj = FileTransfer.getInstance().nativeToJava(transferType);

			if (obj instanceof String[] files) {
				if (files.length != 1) {
					return Status.CANCEL_STATUS;

				}
				for (final String filePath : files) {
					// Expect this type of extension
					final String lowerFilePath = filePath.toLowerCase();
					if (!( //
					lowerFilePath.endsWith(".lingodata") //
							|| lowerFilePath.endsWith(".lingoupdate") //
					)) {
						return Status.CANCEL_STATUS;

					}
				}

			}

			return Status.OK_STATUS;
		}

		return Status.CANCEL_STATUS;
	}

	@Override
	public IStatus handleDrop(final CommonDropAdapter aDropAdapter, final DropTargetEvent aDropTargetEvent, final Object aTarget) {

		// Check operation is valid
		if (!(aDropTargetEvent.detail == DND.DROP_MOVE || aDropTargetEvent.detail == DND.DROP_COPY)) {
			return Status.CANCEL_STATUS;
		}

		if (aTarget instanceof Folder) {

			final Container container = (Container) aTarget;

			final TransferData currentTransfer = aDropAdapter.getCurrentTransfer();
			if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {

				final ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
				if (selection instanceof IStructuredSelection ss) {

					int detail = aDropTargetEvent.detail;

					final List<Container> containers = new LinkedList<>();
					final Iterator<?> iterator = ss.iterator();
					while (iterator.hasNext()) {
						final Object obj = iterator.next();
						if (obj instanceof Container c) {
							// Moving between scenario services? Force a copy
							final IScenarioService a = SSDataManager.Instance.findScenarioService(c);
							final IScenarioService b = SSDataManager.Instance.findScenarioService(container);
							if (a != b) {
								detail = DND.DROP_COPY;
							}
							containers.add(c);
						} else {
							return Status.CANCEL_STATUS;
						}
					}

					// TODO: This should really invoke a shared move/copy etc command/action handler
					if (detail == DND.DROP_MOVE) {

						BusyIndicator.showWhile(Display.getCurrent(), () -> {
							final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(container);
							scenarioService.moveInto(containers, container);
						});
					} else if (detail == DND.DROP_COPY) {
						final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

						try {
							dialog.run(true, true, new IRunnableWithProgress() {

								@SuppressWarnings("deprecation")
								@Override
								public void run(final IProgressMonitor parentMonitor) throws InvocationTargetException, InterruptedException {

									final SubMonitor monitor = SubMonitor.convert(parentMonitor);
									monitor.beginTask("Copying", 100 * containers.size());
									try {
										for (final Container c : containers) {
											if (monitor.isCanceled()) {
												return;
											}
											monitor.setTaskName("Copying " + c.getName());
											if (c instanceof Folder folder) {
												try {
													copyFolder(container, folder, monitor.split(100));
												} catch (final Exception e) {
													log.error(e.getMessage(), e);
													RunnerHelper.asyncExec(display -> MessageDialog.openError(display.getActiveShell(), "Error copying scenarios",
															"There was an error copying scenarios. Please ensure they can all be opened."));
												}
											} else {
												try {
													copyScenario(container, (ScenarioInstance) c, monitor.split(100));
													Thread.sleep(1000);
												} catch (final Exception e) {
													log.error(e.getMessage(), e);
												}
											}
											// monitor.worked(1);
										}
									} finally {
										monitor.done();
									}
								}
							});
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
							return Status.CANCEL_STATUS;
						}
					} else {
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
			} else if (FileTransfer.getInstance().isSupportedType(currentTransfer)) {
				final Object obj = FileTransfer.getInstance().nativeToJava(currentTransfer);

				if (obj instanceof String[] files) {

					return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

						final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

						try {
							dialog.run(true, true, new IRunnableWithProgress() {

								@Override
								public void run(final IProgressMonitor parentMonitor) throws InvocationTargetException, InterruptedException {

									final SubMonitor monitor = SubMonitor.convert(parentMonitor);
									monitor.beginTask("Copying", 10 * files.length);
									try {

										for (final String filePath : files) {
											if (monitor.isCanceled()) {
												return;
											}
											monitor.setTaskName("Copying " + filePath);
											final Set<String> existingNames = ScenarioServiceUtils.getExistingNames(container);
											final File file = new File(filePath);
											final String scenarioName = ScenarioServiceUtils.stripFileExtension(file.getName());
											// final EObject rootObjectCopy;
											final URL scenarioURL = file.toURI().toURL();
											// SubMonitor - remember to be careful, only one #split monitor will work at a
											// time.
											ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioURL, (modelRecord, modelReference) -> {
												try {
													// This is the *second* progress monitor. This is expected to be called once the
													// other #split monitor has completed.
													ScenarioServiceUtils.copyScenario(modelRecord, container, scenarioName, existingNames, monitor.split(4));
												} catch (final Exception e) {
													log.error(e.getMessage(), e);
												}
											}, monitor.split(5));

											monitor.worked(1);
										}
									} catch (final Exception e) {
										log.error(e.getMessage(), e);
									} finally {
										monitor.done();
									}
								}
							});
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
							return Status.CANCEL_STATUS;
						}
						return Status.OK_STATUS;
					});
				}
			}
		} else if (aTarget instanceof ScenarioInstance scenarioInstance) {
			final TransferData currentTransfer = aDropAdapter.getCurrentTransfer();
			if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {

				final ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
				if (selection instanceof IStructuredSelection ss) {
					final Object e = ss.getFirstElement();
					if (e instanceof ScenarioFragment scenarioFragment) {
						ServiceHelper.withAllServices(IScenarioFragmentCopyHandler.class, null, handler -> {
							final boolean handled = handler.copy(scenarioFragment, scenarioInstance);
							return !handled;
						});
					} else if (e instanceof IChangeSource iChangeSource) {
						ServiceHelper.withAllServices(IScenarioInstanceChangeHandler.class, null, handler -> {
							final boolean handled = handler.applyChange(scenarioInstance, iChangeSource);
							return !handled;
						});
					}
				}
			} else if (FileTransfer.getInstance().isSupportedType(currentTransfer)) {
				final Object obj = FileTransfer.getInstance().nativeToJava(currentTransfer);
				if (obj instanceof String[] files) {
					return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
						final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
						try {
							dialog.run(true, true, new IRunnableWithProgress() {
								@Override
								public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

									monitor.beginTask("Import data", 10 * files.length);
									try {

										for (final String filePath : files) {
											if (monitor.isCanceled()) {
												return;
											}
											monitor.setTaskName("Copying " + filePath);
											ServiceHelper.withAllServices(ILiNGODataImportHandler.class, null, handler -> {
												final boolean handled = handler.importLiNGOData(filePath, scenarioInstance);
												return !handled;
											});
											monitor.worked(10);
										}
									} catch (final Exception e) {
										log.error(e.getMessage(), e);
									} finally {
										monitor.done();
									}
								}
							});
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
							return Status.CANCEL_STATUS;
						}
						return Status.OK_STATUS;
					});
				}
			}

			// if (!(ss.size() == 1 && ss.getFirstElement() instanceof ScenarioFragment)) {
			// return Status.CANCEL_STATUS;
			// }
		}

		return Status.CANCEL_STATUS;
	}

	private void copyScenario(@NonNull final Container container, @NonNull final ScenarioInstance scenario, @NonNull final IProgressMonitor parentMonitor) throws Exception {
		final SubMonitor monitor = SubMonitor.convert(parentMonitor);
		monitor.beginTask("Copying " + scenario.getName(), 20);
		try {
			final ScenarioInstance instance = ScenarioServiceModelUtils.copyScenario(scenario, container, ScenarioServiceUtils.getExistingNames(container), monitor.split(10));

			if (instance != null) {
				for (final Container c : scenario.getElements()) {
					if (monitor.isCanceled()) {
						return;
					}
					monitor.subTask("Copying " + c.getName());
					if (c instanceof Folder folder) {
						copyFolder(instance, folder, monitor.split(10));
					} else {
						copyScenario(instance, (ScenarioInstance) c, monitor.split(10));
					}
				}
			}
		} finally {
			monitor.done();
		}
	}

	private void copyFolder(@NonNull final Container container, @NonNull final Folder folder, @NonNull final IProgressMonitor parentMonitor) throws Exception {
		final SubMonitor monitor = SubMonitor.convert(parentMonitor);

		// move all of thing into ScenarioServiceModelUtisl
		monitor.beginTask("Copying " + folder.getName(), 10 * folder.getElements().size());
		try {
			// Ensure name is unique in the destination container
			final String name = folder.getName();
			final Folder f = ScenarioServiceUtils.copyFolder(folder, container, name, ScenarioServiceUtils.getExistingNames(container));

			for (final Container c : folder.getElements()) {
				if (monitor.isCanceled()) {
					return;
				}
				monitor.subTask("Copying " + c.getName());
				if (c instanceof Folder subFolder) {
					copyFolder(f, subFolder, monitor.split(10));
				} else {
					copyScenario(f, (ScenarioInstance) c, monitor.split(10));
				}
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}
	}

	@Override
	public boolean isSupportedType(final TransferData aTransferType) {
		return LocalSelectionTransfer.getTransfer().isSupportedType(aTransferType) || FileTransfer.getInstance().isSupportedType(aTransferType);
	}
}
