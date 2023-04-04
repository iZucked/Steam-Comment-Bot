/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.commands;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class SlotUpdatingCommandProvider extends AbstractCommandProvider {

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
  		if (rootObject instanceof final LNGScenarioModel sm) {
			final CompoundCommand cc = new CompoundCommand();
			if (commandClass == SetCommand.class) {
				//Check if slot has been renamed.
				if (parameter.getEOwner() instanceof Slot) {
					final Slot<?> slot = (Slot<?>)parameter.getEOwner();
					//If it's a LoadSlot => rename Buy nomination's nomineeId.
					//Else it must be a DischargeSlot => rename Sell nomination's nomineeId.
					final Side side = (slot instanceof LoadSlot) ? Side.BUY : Side.SELL;
		
					if (parameter.getEStructuralFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
						final String oldName = slot.getName();
						final String newName = (String)parameter.getValue();

						if (!Objects.equals(oldName, newName)) {
							//update all nominations with nomineeId = slot name.
							final NominationsModel nm = sm.getNominationsModel();
							nm.getNominations().forEach(sn -> {
								if (sn.getSide() == side && Objects.equals(sn.getNomineeId(), oldName)) {
									cc.append(SetCommand.create(editingDomain, sn, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), newName));
								}
							});
						}
					}
				}
			} else if (commandClass == DeleteCommand.class) {
				//Check if slot has been deleted.
				for (final Object object : getChangedValues(parameter)) {
					if (object instanceof Slot) {
						final Slot<?> slot = (Slot<?>)object;
						final String name = slot.getName();
						//If it's a LoadSlot => rename Buy nomination's nomineeId.
						//Else it must be a DischargeSlot => rename Sell nomination's nomineeId.
						final Side side = (slot instanceof LoadSlot) ? Side.BUY : Side.SELL;
						
						//delete all nominations with nomineeId = slot name.
						final NominationsModel nm = sm.getNominationsModel();
						nm.getNominations().forEach(sn -> {
							if (sn.getSide() == side && Objects.equals(sn.getNomineeId(), name)) {
								cc.append(DeleteCommand.create(editingDomain, sn));
							}
						});
					}
				}
			}
			if (!cc.isEmpty()) {
				return cc;
			}
		}
		return null;
	}

}
