/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.ui.commands.PasteScenarioCommandHandler;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

/**
 * DND handler to allow element moving in the scenario navigator. This needs more support to handle copy actions and to allow scenarios to be dragged in from e.g. filesystem.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioDragAssistant extends CommonDropAdapterAssistant {

	private static final Logger log = LoggerFactory.getLogger(ScenarioDragAssistant.class);

	public ScenarioDragAssistant() {
	}

	@Override
	public IStatus validateDrop(final Object target, final int operation, final TransferData transferType) {
		if (!(operation == DND.DROP_MOVE || operation == DND.DROP_COPY)) {
			return Status.CANCEL_STATUS;
		}

		// Handle local "within eclipse" transfer
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType) && (target instanceof Container || target == null)) {
			final ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				final HashSet<EObject> containers = new HashSet<EObject>();
				EObject eTarget = (EObject) target;
				if (target instanceof ScenarioService) {
					return Status.CANCEL_STATUS;
				}
				if (target instanceof ScenarioInstance) {
					// Could open up to multipe?
					if (!(ss.size() == 1 && ss.getFirstElement() instanceof ScenarioFragment)) {
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
						// since containers contains the full hierarchy above the drop target, if o is in that hierarchy then we are dragging
						// something into something which it contains, so cancel the drop
						if (containers.contains(o))
							return Status.CANCEL_STATUS;
					}
				}
			}
			return Status.OK_STATUS;
		}
		// Handle e.g. desktop to application DND
		else if (FileTransfer.getInstance().isSupportedType(transferType) && (target instanceof Container || target == null)) {
			final Object obj = FileTransfer.getInstance().nativeToJava(transferType);

			if (!(target instanceof Folder || target instanceof ScenarioService)) {
				return Status.CANCEL_STATUS;
			}

			if (obj instanceof String[]) {
				final String[] files = (String[]) obj;

				for (final String filePath : files) {
					// Expect this type of extension
					final String lowerFilePath = filePath.toLowerCase();
					if (!(lowerFilePath.endsWith(".sc2") || lowerFilePath.endsWith(".scn") || lowerFilePath.endsWith(".scenario") || lowerFilePath.endsWith(".lingo"))) {
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
				if (selection instanceof IStructuredSelection) {

					int detail = aDropTargetEvent.detail;

					final List<Container> containers = new LinkedList<Container>();
					final Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
					while (iterator.hasNext()) {
						final Object obj = iterator.next();
						if (obj instanceof Container) {
							final Container c = (Container) obj;
							// Moving between scenario services? Force a copy
							if (c.getScenarioService() != container.getScenarioService()) {
								detail = DND.DROP_COPY;
							}
							containers.add(c);
						} else {
							return Status.CANCEL_STATUS;
						}
					}

					// TODO: This should really invoke a shared move/copy etc command/action handler
					if (detail == DND.DROP_MOVE) {

						BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

							@Override
							public void run() {
								final IScenarioService scenarioService = container.getScenarioService();
								scenarioService.moveInto(containers, container);
							}
						});
					} else if (detail == DND.DROP_COPY) {
						final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

						try {
							dialog.run(true, true, new IRunnableWithProgress() {

								@Override
								public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

									monitor.beginTask("Copying", 100 * containers.size());
									try {
										for (final Container c : containers) {
											if (monitor.isCanceled()) {
												return;
											}
											monitor.setTaskName("Copying " + c.getName());
											if (c instanceof Folder) {
												try {
													copyFolder(container, (Folder) c, new SubProgressMonitor(monitor, 100));
												} catch (final IOException e) {
													log.error(e.getMessage(), e);
												}
											} else {
												try {
													copyScenario(container, (ScenarioInstance) c, new SubProgressMonitor(monitor, 100));
												} catch (final IOException e) {
													log.error(e.getMessage(), e);
												}
											}
											monitor.worked(1);
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

				if (obj instanceof String[]) {

					final String[] files = (String[]) obj;
					final IScenarioCipherProvider scenarioCipherProvider = getScenarioCipherProvider();

					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());

					try {
						dialog.run(true, true, new IRunnableWithProgress() {

							@Override
							public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

								monitor.beginTask("Copying", files.length);
								try {

									for (final String filePath : files) {
										if (monitor.isCanceled()) {
											return;
										}
										monitor.setTaskName("Copying " + filePath);
										final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromFile(filePath, scenarioCipherProvider);
										if (instance != null) {
											try {
												// Get basic name
												String scenarioName = new File(filePath).getName();
												scenarioName = ScenarioServiceModelUtils.stripFileExtension(scenarioName);

												final Set<String> existingNames = ScenarioServiceModelUtils.getExistingNames(container);
												ScenarioServiceModelUtils.copyScenario(instance, container, scenarioName, existingNames);
											} catch (final IOException e) {
												log.error(e.getMessage(), e);
											}
										}
										monitor.worked(1);
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

					return Status.OK_STATUS;
				}

			}
		} else if (aTarget instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) aTarget;
			final TransferData currentTransfer = aDropAdapter.getCurrentTransfer();
			if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {

				final ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection) selection;
					Object e = ss.getFirstElement();
					if (e instanceof ScenarioFragment) {
						ScenarioFragment scenarioFragment = (ScenarioFragment) e;
						ServiceHelper.withAllServices(IScenarioFragmentCopyHandler.class, null, handler -> {
							boolean handled = handler.copy(scenarioFragment, scenarioInstance);
							return !handled;
						});
					}

				}
			}

			// if (!(ss.size() == 1 && ss.getFirstElement() instanceof ScenarioFragment)) {
			// return Status.CANCEL_STATUS;
			// }
		}

		return Status.CANCEL_STATUS;
	}

	private void copyScenario(@NonNull final Container container, @NonNull final ScenarioInstance scenario, @NonNull final IProgressMonitor monitor) throws IOException {
		monitor.beginTask("Copying " + scenario.getName(), 10);
		try {
			final ScenarioInstance instance = ScenarioServiceModelUtils.copyScenario(scenario, container, ScenarioServiceModelUtils.getExistingNames(container));

			if (instance != null) {
				for (final Container c : scenario.getElements()) {
					if (monitor.isCanceled()) {
						return;
					}
					monitor.subTask("Copying " + c.getName());
					if (c instanceof Folder) {
						copyFolder(instance, (Folder) c, new SubProgressMonitor(monitor, 10));
					} else {
						copyScenario(instance, (ScenarioInstance) c, new SubProgressMonitor(monitor, 10));
					}
				}
			}
		} finally {
			monitor.done();
		}
	}

	private void copyFolder(@NonNull final Container container, @NonNull final Folder folder, @NonNull final IProgressMonitor monitor) throws IOException {

		// move all of thing into ScenarioServiceModelUtisl
		monitor.beginTask("Copying " + folder.getName(), 10 * folder.getElements().size());
		try {
			// Ensure name is unique in the destination container
			String name = folder.getName();
			final Folder f = ScenarioServiceModelUtils.copyFolder(folder, container, name, ScenarioServiceModelUtils.getExistingNames(container));

			for (final Container c : folder.getElements()) {
				if (monitor.isCanceled()) {
					return;
				}
				monitor.subTask("Copying " + c.getName());
				if (c instanceof Folder) {
					copyFolder(f, (Folder) c, new SubProgressMonitor(monitor, 10));
				} else {
					copyScenario(f, (ScenarioInstance) c, new SubProgressMonitor(monitor, 10));
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

	@Nullable
	private IScenarioCipherProvider getScenarioCipherProvider() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(PasteScenarioCommandHandler.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		if (serviceReference != null) {
			return bundleContext.getService(serviceReference);
		}
		return null;
	}
}
