/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;

/**
 * Adds a set command to the setter for cargo names
 * 
 * @author hinton
 *
 */
public class SlotNameUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(EditingDomain editingDomain, MMXRootObject rootObject, Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof Cargo) {
				final Cargo cargo = (Cargo) parameter.getEOwner();
				if (parameter.getEStructuralFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
					final Slot load = cargo.getLoadSlot();
					final Slot discharge = cargo.getDischargeSlot();
					final CompoundCommand fixer = new CompoundCommand();
					fixer.append(IdentityCommand.INSTANCE);
					if (load != null) {
						fixer.append(SetCommand.create(editingDomain, load, MMXCorePackage.eINSTANCE.getNamedObject_Name(), "load-" + parameter.getValue()));
					}
					
					if (discharge != null) {
						fixer.append(SetCommand.create(editingDomain, discharge, MMXCorePackage.eINSTANCE.getNamedObject_Name(), "discharge-" + parameter.getValue()));
					}
					
					return fixer;
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
