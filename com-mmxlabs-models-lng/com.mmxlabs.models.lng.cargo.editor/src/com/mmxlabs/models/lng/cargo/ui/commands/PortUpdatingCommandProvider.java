/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @author hinton
 * 
 */
public class PortUpdatingCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof Slot) {
				final Slot slot = (Slot) parameter.getEOwner();
				if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Contract()) {
					// update contract
					final EObject newValue = parameter.getEValue();
					if (newValue instanceof Contract) {
						final Contract contract = (Contract) newValue;
						if (contract.getAllowedPorts().isEmpty()) {
							return null;
						}
						final Port currentPort = slot.getPort();
						if (contract.getPreferredPort() != null) {
							if (!SetUtils.getObjects(contract.getAllowedPorts()).contains(currentPort)) {
								// figure out a new port - preferred port?
								return SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), contract.getPreferredPort());
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
