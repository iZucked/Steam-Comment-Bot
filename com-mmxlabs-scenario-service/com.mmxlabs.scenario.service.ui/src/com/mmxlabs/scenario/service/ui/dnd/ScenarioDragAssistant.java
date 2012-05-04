/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

/**
 * DND handler to allow element moving in the scenario navigator. This needs more support to handle copy actions and to allow scenarios to be dragged in from e.g. filesystem.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioDragAssistant extends CommonDropAdapterAssistant {

	public ScenarioDragAssistant() {
	}

	@Override
	public IStatus validateDrop(final Object target, final int operation, final TransferData transferType) {
		// Only support moving around currently.
		// TODO: Workspace service needs to handle this properly!
		if (operation != DND.DROP_MOVE) {
			return Status.CANCEL_STATUS;
		}

		// Only handle local "within eclipse" transfer
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType) && target instanceof Container) {
			return Status.OK_STATUS;
		}

		return Status.CANCEL_STATUS;
	}

	@Override
	public IStatus handleDrop(final CommonDropAdapter aDropAdapter, final DropTargetEvent aDropTargetEvent, final Object aTarget) {

		// Check operation is valid
		if (aDropTargetEvent.detail != DND.DROP_MOVE) {
			return Status.CANCEL_STATUS;
		}

		if (aTarget instanceof Container) {

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
								copyFolder(container, (Folder) c);
							} else {
								copyScenario(container, (ScenarioInstance) c);
							}
						}
					} else {
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
			}
		}

		return Status.CANCEL_STATUS;
	}

	private void copyScenario(final Container container, final ScenarioInstance scenario) {

		final ScenarioInstance instance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		instance.setName(scenario.getName());
		instance.setMetadata(EcoreUtil.copy(scenario.getMetadata()));
		instance.setUuid(UUID.randomUUID().toString());

		// TODO: Invoke scenario service copy rather than above stub
		// ScenarioInstance instance = scenario.getScenarioService().copy(scenario);
		container.getElements().add(instance);

		for (final Container c : scenario.getElements()) {
			if (c instanceof Folder) {
				copyFolder(instance, (Folder) c);
			} else {
				copyScenario(instance, (ScenarioInstance) c);
			}
		}
	}

	private void copyFolder(final Container container, final Folder folder) {

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
}
