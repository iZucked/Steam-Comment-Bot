/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class VesselEventVesselsManipulator extends MultipleReferenceManipulator {

	private final AssignmentModel assignmentModel;

	public VesselEventVesselsManipulator(final EStructuralFeature field, final EditingDomain editingDomain, final IReferenceValueProvider valueProvider, final EAttribute nameAttribute,
			final AssignmentModel assignmentModel) {
		super(field, editingDomain, valueProvider, nameAttribute);
		this.assignmentModel = assignmentModel;
	}

	public VesselEventVesselsManipulator(final EStructuralFeature field, final IReferenceValueProviderProvider providerProvider, final EditingDomain editingDomain, final EAttribute nameAttribute,
			final AssignmentModel assignmentModel) {
		super(field, editingDomain, providerProvider.getReferenceValueProvider(field.getEContainingClass(), (EReference) field), nameAttribute);
		this.assignmentModel = assignmentModel;
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) {
			return;
		}

		final CompoundCommand cmd = new CompoundCommand("Set event vessels");
		final Collection<?> collection = (Collection<?>) value;
		cmd.append(CommandUtil.createMultipleAttributeSetter(editingDomain, (EObject) object, field, collection));

		if (collection.size() == 1) {
			final Object newAssignment = collection.iterator().next();
			cmd.append(AssignmentEditorHelper.reassignElement(editingDomain, assignmentModel, (UUIDObject) object, (AVesselSet) newAssignment));
		} else {
			final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, (UUIDObject) object);
			if (!collection.contains(elementAssignment.getAssignment())) {
				cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, assignmentModel, (UUIDObject) object));
			}
		}
		editingDomain.getCommandStack().execute(cmd);
	}
}
