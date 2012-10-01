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
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class ElementAssignmentCommandProvider extends BaseModelCommandProvider {

	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (addedObject instanceof Cargo || addedObject instanceof VesselEvent) {

			for (final EObject entry : editSet) {
				if (entry instanceof ElementAssignment) {
					ElementAssignment elementAssignment = (ElementAssignment) entry;
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

	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object added, final Map<EObject, EObject> overrides) {
		final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
		ea.setAssignedObject((UUIDObject) added);
		return AddCommand.create(domain, rootObject.getSubModel(InputModel.class), InputPackage.eINSTANCE.getInputModel_ElementAssignments(), ea);
	}

	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides) {
		final ElementAssignment ea = AssignmentEditorHelper.getElementAssignment(rootObject.getSubModel(InputModel.class), (UUIDObject) deleted);
		if (ea != null) {
			return DeleteCommand.create(domain, ea);
		} else {
			return null;
		}
	}

}
