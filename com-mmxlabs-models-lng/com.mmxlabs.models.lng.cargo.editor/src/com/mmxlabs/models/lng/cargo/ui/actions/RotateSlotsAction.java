/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;

/**
 * @author hinton
 *
 */
public class RotateSlotsAction extends ScenarioModifyingAction {
	private IEditingDomainProvider editingDomainProvider;

	public RotateSlotsAction(final IEditingDomainProvider edProvider) {
		super("Swap Discharge Slots");
		this.editingDomainProvider = edProvider;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final List<Cargo> cargos = (List<Cargo>) ((IStructuredSelection) getLastSelection()).toList();
		final EditingDomain domain = editingDomainProvider.getEditingDomain();
		
		final CompoundCommand cc = new CompoundCommand();
		
		DischargeSlot evictedSlot = cargos.get(cargos.size()-1).getDischargeSlot();
		
		for (final Cargo cargo : cargos) {
			cc.append(SetCommand.create(domain, cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), evictedSlot));
			evictedSlot = cargo.getDischargeSlot();
		}
		
		domain.getCommandStack().execute(cc);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction#isApplicableToSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected boolean isApplicableToSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).size() == 2) {
				for (final Object o : ((IStructuredSelection) selection).toArray()) {
					if (!(o instanceof Cargo)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
