/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Tom Hinton
 * 
 */
public class CommandUtil {
	public static CompoundCommand createMultipleAttributeSetter(final EditingDomain editingDomain, final EObject target, final ETypedElement typedElement, final Collection<?> value) {
		final CompoundCommand setter = new CompoundCommand();

		if (typedElement.isUnique() && typedElement instanceof EStructuralFeature feature) {
			final Collection<?> oldValues = (Collection<?>) target.eGet(feature);
			final Collection<?> newValues = value;

			// this is everything not in the new value list
			final ArrayList<?> removeValues = new ArrayList<>(oldValues);
			removeValues.removeAll(newValues);

			// this is everything actually being added
			final ArrayList<?> addedValues = new ArrayList<>(newValues);
			addedValues.removeAll(oldValues);

			setter.append(new IdentityCommand());
			if (!removeValues.isEmpty()) {
				setter.append(RemoveCommand.create(editingDomain, target, typedElement, removeValues));
			}

			if (!addedValues.isEmpty()) {
				setter.append(AddCommand.create(editingDomain, target, typedElement, addedValues));
			}
		} else {
			setter.append(SetCommand.create(editingDomain, target, typedElement, SetCommand.UNSET_VALUE));
			if (!value.isEmpty()) {
				setter.append(AddCommand.create(editingDomain, target, typedElement, value));
			}
		}
		return setter;
	}
}
