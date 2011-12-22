/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.presentation.cargoeditor.LockableAction;

/**
 * A debug-mode action to jiggle cargoes around
 * 
 * @author hinton
 *
 */
public class FixNamesAction extends LockableAction implements ISelectionChangedListener {
	public FixNamesAction() {
		setToolTipText("Fix cargo and slot names");
		setText("Fix Names");
		setEnabled(false);
	}

	@Override
	public void run() {
		final CompoundCommand command = new CompoundCommand();
		final EditingDomain editingDomain = getEditingDomain(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		
		assert editingDomain != null;
		int index=1;
		for (final Object o : theSelection.toArray()) {
			if (o instanceof Cargo) {
				final Cargo c = (Cargo) o;
				final LoadSlot l = c.getLoadSlot();
				final Slot d = c.getDischargeSlot();
				
				command.append(SetCommand.create(editingDomain, l, CargoPackage.eINSTANCE.getSlot_Id(), "load-cargo-" + index));
				command.append(SetCommand.create(editingDomain, d, CargoPackage.eINSTANCE.getSlot_Id(), "discharge-cargo-"+index));
				command.append(SetCommand.create(editingDomain, c, CargoPackage.eINSTANCE.getCargo_Id(), "cargo-"+index));
				index++;
			}
		}

		editingDomain.getCommandStack().execute(command);
	}

	private IStructuredSelection theSelection = null;
	
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			setEnabled(ss.size() > 0);
			theSelection = ss;
		} else {
			setEnabled(false);
		}
	}

	private EditingDomain getEditingDomain(final IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? ((IEditingDomainProvider) part)
				.getEditingDomain() : null);
	}
}
