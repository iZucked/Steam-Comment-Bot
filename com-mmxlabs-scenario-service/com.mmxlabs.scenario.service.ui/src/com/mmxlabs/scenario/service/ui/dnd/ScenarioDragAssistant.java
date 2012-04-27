package com.mmxlabs.scenario.service.ui.dnd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

import com.mmxlabs.scenario.service.model.Container;

public class ScenarioDragAssistant extends CommonDropAdapterAssistant {

	public ScenarioDragAssistant() {
		// TODO Auto-generated constructor stub
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

					container.getElements().addAll(containers);
					return Status.OK_STATUS;
				}
			}
		}

		return Status.CANCEL_STATUS;
	}

}
