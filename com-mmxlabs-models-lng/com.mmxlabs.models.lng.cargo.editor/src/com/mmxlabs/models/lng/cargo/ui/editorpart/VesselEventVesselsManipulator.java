/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class VesselEventVesselsManipulator extends MultipleReferenceManipulator {

	public VesselEventVesselsManipulator(final EStructuralFeature field, final EditingDomain editingDomain, final IReferenceValueProvider valueProvider, final EAttribute nameAttribute) {
		super(field, editingDomain, valueProvider, nameAttribute);
	}

	public VesselEventVesselsManipulator(final EStructuralFeature field, final IReferenceValueProviderProvider providerProvider, final EditingDomain editingDomain, final EAttribute nameAttribute) {
		super(field, editingDomain, providerProvider.getReferenceValueProvider(field.getEContainingClass(), (EReference) field), nameAttribute);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		AssignableElement assignableElement = (AssignableElement)object;
		
		
		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) {
			return;
		}

		final CompoundCommand cmd = new CompoundCommand("Set event vessels");
		final Collection<?> collection = (Collection<?>) value;
		cmd.append(CommandUtil.createMultipleAttributeSetter(editingDomain, (EObject) object, field, collection));

		if (collection.size() == 1) {
			final Object newAssignment = collection.iterator().next();
			cmd.append(SetCommand.create(editingDomain, assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, newAssignment));
		} else {
			if (!collection.contains(assignableElement.getAssignment())) {
				cmd.append(SetCommand.create(editingDomain, assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
			}
		}
		editingDomain.getCommandStack().execute(cmd);
	}
}
