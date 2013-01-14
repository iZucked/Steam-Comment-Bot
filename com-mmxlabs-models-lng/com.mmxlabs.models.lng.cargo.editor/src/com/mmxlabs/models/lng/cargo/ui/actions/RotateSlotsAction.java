/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 * @author hinton
 * 
 */
public class RotateSlotsAction extends ScenarioModifyingAction {
	private final IScenarioEditingLocation location;

	public RotateSlotsAction(final IScenarioEditingLocation iScenarioEditingLocation) {
		super("Swap Discharge Slots");
		setImageDescriptor(CargoEditorPlugin.getPlugin().getImageDescriptor(CargoEditorPlugin.IMAGE_CARGO_SWAP));
		setDisabledImageDescriptor(CargoEditorPlugin.getPlugin().getImageDescriptor(CargoEditorPlugin.IMAGE_CARGO_SWAP_DISABLED));
		this.location = iScenarioEditingLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final List<Cargo> cargoes = (List<Cargo>) ((IStructuredSelection) getLastSelection()).toList();
		final EditingDomain domain = location.getEditingDomain();

		final MMXRootObject rootObject = location.getRootObject();
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);

		final CompoundCommand cc = new CompoundCommand("Swap discharge slots");

		DischargeSlot evictedSlot = cargoes.get(cargoes.size() - 1).getDischargeSlot();

		for (final Cargo cargo : cargoes) {
			cc.append(SetCommand.create(domain, cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), evictedSlot));
			evictedSlot = cargo.getDischargeSlot();

			final LoadSlot loadSlot = cargo.getLoadSlot();
			if (evictedSlot.isFOBSale()) {
				if (loadSlot.isDESPurchase()) {
					throw new IllegalArgumentException("Cannot link FOB Sales to DES Purchases");
				}

				appendFOBDESCommands(cc, domain, inputModel, cargo, loadSlot, evictedSlot);

			}
			if (loadSlot.isDESPurchase()) {
				appendFOBDESCommands(cc, domain, inputModel, cargo, loadSlot, evictedSlot);
			}
		}

		domain.getCommandStack().execute(cc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction#isApplicableToSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
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

	private void appendFOBDESCommands(final CompoundCommand cmd, final EditingDomain editingDomain, final InputModel inputModel, final Cargo cargo, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {

		if (loadSlot.isDESPurchase()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));

			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
			if (loadSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, loadSlot, dischargeSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
			}
		} else if (dischargeSlot.isFOBSale()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
			if (dischargeSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, dischargeSlot, loadSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
			}
		}
	}
}
