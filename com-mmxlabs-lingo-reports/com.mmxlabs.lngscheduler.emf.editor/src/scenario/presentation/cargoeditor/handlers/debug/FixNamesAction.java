/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.debug;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.presentation.cargoeditor.handlers.ScenarioModifyingAction;

/**
 * A debug-mode action to jiggle cargoes around
 * 
 * @author hinton
 *
 */
public class FixNamesAction extends ScenarioModifyingAction {
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
		for (final Object o : ((IStructuredSelection) getLastSelection()).toArray()) {
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

	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			return ss.size() > 0;
		}
		return false;
	}

}
