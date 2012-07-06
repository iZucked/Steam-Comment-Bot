/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;

/**
 * 
 * A {@link IModelCommandProvider} implementation to map DES/FOB details between slots
 * 
 * @author Simon Goodall
 * 
 */
public class CargoTypeUpdatingCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Class<? extends Command> commandClass,
			final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			InputModel inputModel = rootObject.getSubModel(InputModel.class);
			if (overrides.containsKey(inputModel)) {
				inputModel = (InputModel) overrides.get(inputModel);
			}
			if (parameter.getEOwner() instanceof LoadSlot) {
				final LoadSlot slot = (LoadSlot) parameter.getEOwner();

				if (slot.getCargo() != null) {
					final Cargo cargo = slot.getCargo();
					final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
					if (dischargeSlot != null) {

						if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
							final boolean desPurchase = (Boolean) parameter.getValue();
							if (desPurchase) {

								final CompoundCommand cmd = new CompoundCommand("Convert to DES Purchase");
								cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));

								return cmd;
							}
						} else if (dischargeSlot.isFOBSale()) {
							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
								final CompoundCommand cmd = new CompoundCommand("FOB Sale update");
								cmd.append(SetCommand.create(editingDomain, dischargeSlot, parameter.getEStructuralFeature(), parameter.getValue()));
								return cmd;
							}
						}
					}
				}
			}
			if (parameter.getEOwner() instanceof DischargeSlot) {
				final DischargeSlot slot = (DischargeSlot) parameter.getEOwner();
				if (slot.getCargo() != null) {
					final Cargo cargo = slot.getCargo();
					final LoadSlot loadSlot = cargo.getLoadSlot();
					if (loadSlot != null) {

						if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
							final boolean fobSale = (Boolean) parameter.getValue();
							if (fobSale) {

								final CompoundCommand cmd = new CompoundCommand("Convert to FOB Sale");
								cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));

								return cmd;
							}
						} else if (loadSlot.isDESPurchase()) {
							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
								final CompoundCommand cmd = new CompoundCommand("DES Purchase update");
								cmd.append(SetCommand.create(editingDomain, loadSlot, parameter.getEStructuralFeature(), parameter.getValue()));
								return cmd;
							}
						}

					}
				}
			}
		}
		return null;
	}

	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
}
