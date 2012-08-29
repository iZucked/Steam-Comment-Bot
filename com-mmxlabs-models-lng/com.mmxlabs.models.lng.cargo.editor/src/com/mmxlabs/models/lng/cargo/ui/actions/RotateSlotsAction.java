/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
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
		try {
			setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.cargo.editor/icons/swap.gif")));
		} catch (final MalformedURLException e) {
		}
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
				cc.append(AssignmentEditorHelper.unassignElement(domain, inputModel, cargo));
				cc.append(SetCommand.create(domain, evictedSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
				cc.append(SetCommand.create(domain, evictedSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
				cc.append(SetCommand.create(domain, evictedSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
				cc.append(SetCommand.create(domain, evictedSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));

				if (loadSlot.isDESPurchase()) {
					throw new IllegalArgumentException("Cannot link FOB Sales to DES Purchases");
				}
			}
			if (loadSlot.isDESPurchase()) {
				cc.append(SetCommand.create(domain, loadSlot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
				cc.append(SetCommand.create(domain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
				cc.append(SetCommand.create(domain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), evictedSlot.getPort()));
				cc.append(SetCommand.create(domain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), evictedSlot.getWindowStart()));
				cc.append(SetCommand.create(domain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), evictedSlot.getWindowStartTime()));
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

}
