/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
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
	public Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {

		if (parameter.getFeature() == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
			if (!(parameter.getEValue() instanceof CharterInMarket)) {
				// Clear value
				return SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE);
			}
		}

		return null;
	}

}
