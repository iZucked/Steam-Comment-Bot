/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.resources.projectvariables.ParentVariableResolver;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Command provider to automatically clear the Spot Index field for non-vessel class assignments.
 * 
 * @author Simon Goodall
 * 
 */
public class AssignableElementCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		if (parameter.getFeature() == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
			final CompoundCommand cmd = new CompoundCommand();
			if (!(parameter.getEValue() instanceof CharterInMarket)) {
				// Clear value
				cmd.append(SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
			}
			// Reset the sequence hint to avoid sort issues. See BugzId: 2290
			cmd.append(SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, SetCommand.UNSET_VALUE));
			return cmd;
		}
		if (parameter.getFeature() == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX) {
			// Reset the sequence hint to avoid sort issues. See BugzId: 2290
			return SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, SetCommand.UNSET_VALUE);
		}

		// Various fields which can change the relative ordering of AssignableElements
		if ((parameter.getFeature() == CargoPackage.Literals.SLOT__PORT) //
				|| (parameter.getFeature() == CargoPackage.Literals.SLOT__WINDOW_SIZE) //
				|| (parameter.getFeature() == CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS) //
				|| (parameter.getFeature() == CargoPackage.Literals.SLOT__WINDOW_START) //
				|| (parameter.getFeature() == CargoPackage.Literals.SLOT__WINDOW_START_TIME) //
				|| (parameter.getFeature() == CargoPackage.Literals.SLOT__DURATION)) {
			final Slot slot = (Slot) parameter.getOwner();
			if (slot.getCargo() != null) {
				return SetCommand.create(editingDomain, slot.getCargo(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, SetCommand.UNSET_VALUE);
			}

		}

		return null;
	}

}
