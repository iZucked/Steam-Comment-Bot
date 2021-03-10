/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class VesselEventVesselsManipulator extends MultipleReferenceManipulator {

	public VesselEventVesselsManipulator(final EStructuralFeature field, final ICommandHandler commandHandler, final IReferenceValueProvider valueProvider, final EAttribute nameAttribute) {
		super(field, commandHandler, valueProvider, nameAttribute);
	}

	public VesselEventVesselsManipulator(final EStructuralFeature field, final IReferenceValueProviderProvider providerProvider, final ICommandHandler commandHandler, final EAttribute nameAttribute) {
		super(field, commandHandler, providerProvider.getReferenceValueProvider(field.getEContainingClass(), (EReference) field), nameAttribute);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {

		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) {
			return;
		}

		final CompoundCommand cmd = new CompoundCommand("Set event vessels");
		final Collection<?> collection = (Collection<?>) value;
		cmd.append(CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), (EObject) object, field, collection));

		commandHandler.handleCommand(cmd, (EObject) object, field);
	}
}
