package com.mmxlabs.models.lng.input.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

public class ElementAssignmentCommandProvider extends BaseModelCommandProvider {

	@Override
	protected boolean shouldHandleAddition(Object addedObject) {
		return addedObject instanceof Cargo || addedObject instanceof VesselEvent;
	}

	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		return shouldHandleAddition(deletedObject);
	}

	@Override
	protected Command objectAdded(EditingDomain domain, MMXRootObject rootObject, Object added) {
		final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
		ea.setAssignedObject((UUIDObject) added);
		return AddCommand.create(domain, rootObject.getSubModel(InputModel.class), InputPackage.eINSTANCE.getInputModel_ElementAssignments(), ea);
	}

	@Override
	protected Command objectDeleted(EditingDomain domain, MMXRootObject rootObject, Object deleted) {
		final ElementAssignment ea = AssignmentEditorHelper.getElementAssignment(rootObject.getSubModel(InputModel.class), (UUIDObject) deleted);
		if (ea != null) {
			return DeleteCommand.create(domain, ea);
		} else {
			return null;
		}
	}
}
