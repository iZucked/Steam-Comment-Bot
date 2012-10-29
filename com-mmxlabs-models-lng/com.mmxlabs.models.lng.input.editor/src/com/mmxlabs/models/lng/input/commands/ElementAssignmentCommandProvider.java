/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class ElementAssignmentCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		// Check for correct owner. For example we do not want to trigger this for cargoes being added to cargo groups.
		if (parameter.getOwner()  == null || parameter.getOwner() instanceof CargoModel || parameter.getOwner() instanceof FleetModel) {
			return super.provideAdditionalCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
		}

		return null;
	}

	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (addedObject instanceof Cargo || addedObject instanceof VesselEvent) {

			for (final EObject entry : editSet) {
				if (entry instanceof ElementAssignment) {
					final ElementAssignment elementAssignment = (ElementAssignment) entry;
					if (elementAssignment.getAssignedObject() == addedObject) {
						// There is already an assignment under consideration.
						return false;
					}
				}
			}

			return true;
		}

		return false;
	}

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return (deletedObject instanceof Cargo || deletedObject instanceof VesselEvent);
	}

	@Override
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object added, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
		ea.setAssignedObject((UUIDObject) added);
		return AddCommand.create(domain, rootObject.getSubModel(InputModel.class), InputPackage.eINSTANCE.getInputModel_ElementAssignments(), ea);
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		final ElementAssignment ea = AssignmentEditorHelper.getElementAssignment(rootObject.getSubModel(InputModel.class), (UUIDObject) deleted);
		if (ea != null) {
			return DeleteCommand.create(domain, ea);
		} else {
			return null;
		}
	}

}
