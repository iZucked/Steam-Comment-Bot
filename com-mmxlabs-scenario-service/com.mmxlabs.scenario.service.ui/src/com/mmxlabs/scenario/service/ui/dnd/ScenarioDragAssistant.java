/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

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
				final HashSet<EObject> containers = new HashSet<EObject>();
				EObject eTarget = (EObject) target;
				if (target instanceof ScenarioService) {
					return Status.CANCEL_STATUS;
				}
				if (target instanceof ScenarioInstance) {
					return Status.CANCEL_STATUS;
				}
				while (eTarget != null) {
					containers.add(eTarget);
					eTarget = eTarget.eContainer();
				}
				for (final Object o : ((IStructuredSelection) selection).toArray()) {
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
					if (!(filePath.endsWith(".sc2") || filePath.endsWith(".scn") || filePath.endsWith(".scenario") || filePath.endsWith(".lingo"))) {
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
					final List<Container> containers = new LinkedList<Container>();
					final Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
					while (iterator.hasNext()) {
						final Object obj = iterator.next();
						if (obj instanceof Container) {
							containers.add((Container) obj);
						} else {
							return Status.CANCEL_STATUS;
						}
					}

					// TODO: This should really invoke a shared move/copy etc command/action handler

					if (aDropTargetEvent.detail == DND.DROP_MOVE) {
						container.getElements().addAll(containers);
					} else if (aDropTargetEvent.detail == DND.DROP_COPY) {

						for (final Container c : containers) {
							if (c instanceof Folder) {
								try {
									copyFolder(container, (Folder) c);
								} catch (final IOException e) {
									log.error(e.getMessage(), e);
								}
							} else {
								try {
									copyScenario(container, (ScenarioInstance) c);
								} catch (final IOException e) {
									log.error(e.getMessage(), e);
								}
							}
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

					for (final String filePath : files) {
						final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromFile(filePath);
						if (instance != null) {
							try {
								String scenarioName = new File(filePath).getName();
								container.getScenarioService().duplicate(instance, container).setName(scenarioName);
							} catch (final IOException e) {
								log.error(e.getMessage(), e);
							}
						}
					}
					return Status.OK_STATUS;
				}

			}
		}

		return Status.CANCEL_STATUS;
	}

	private void copyScenario(final Container container, final ScenarioInstance scenario) throws IOException {

		final IScenarioService scenarioService = container.getScenarioService();
		final ScenarioInstance instance = scenarioService.duplicate(scenario, container);

		for (final Container c : scenario.getElements()) {
			if (c instanceof Folder) {
				copyFolder(instance, (Folder) c);
			} else {
				copyScenario(instance, (ScenarioInstance) c);
			}
		}
	}

	private void copyFolder(final Container container, final Folder folder) throws IOException {

		final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
		f.setName(folder.getName());
		f.setMetadata(EcoreUtil.copy(folder.getMetadata()));
		container.getElements().add(f);

		for (final Container c : folder.getElements()) {
			if (c instanceof Folder) {
				copyFolder(f, (Folder) c);
			} else {
				copyScenario(f, (ScenarioInstance) c);
			}
		}

	}

	@Override
	public boolean isSupportedType(final TransferData aTransferType) {
		return LocalSelectionTransfer.getTransfer().isSupportedType(aTransferType) || FileTransfer.getInstance().isSupportedType(aTransferType);
	}
}
