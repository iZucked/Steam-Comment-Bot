/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Command provider to automatically clear the Spot Index field for non-vessel class assignments.
 * 
 * @author Simon Goodall
 * 
 */
public class AssignableElementCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {

		if (parameter.getFeature() == FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
			if (!(parameter.getEValue() instanceof VesselClass)) {
				// Clear value
				return SetCommand.create(editingDomain, parameter.getOwner(), FleetPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE);
			} else {
				if (parameter.getOwner() instanceof AssignableElement) {
					AssignableElement assignableElement = (AssignableElement) parameter.getOwner();
					if (assignableElement.isSetSpotIndex() == false) {
						// Set a default value if not present
						return SetCommand.create(editingDomain, assignableElement, FleetPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, 0);
					}

				}
			}
		}

		return null;
	}

}
