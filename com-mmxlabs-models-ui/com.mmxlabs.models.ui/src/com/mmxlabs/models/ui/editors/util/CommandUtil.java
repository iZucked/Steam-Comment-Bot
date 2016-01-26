/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Tom Hinton
 * 
 */
public class CommandUtil {
	public static CompoundCommand createMultipleAttributeSetter(
			final EditingDomain editingDomain, final EObject target,
			final EStructuralFeature feature, final Collection<?> value) {
		final CompoundCommand setter = new CompoundCommand();

		if (feature.isUnique()) {
			final Collection<?> oldValues = (Collection<?>) target.eGet(feature);
			final Collection<?> newValues = (Collection<?>) value;

			// this is everything not in the new value list
			final ArrayList<?> removeValues = new ArrayList<Object>(oldValues);
			removeValues.removeAll(newValues);

			// this is everything actually being added
			final ArrayList<?> addedValues = new ArrayList<Object>(newValues);
			addedValues.removeAll(oldValues);

			setter.append(new IdentityCommand());
			if (removeValues.size() > 0)
				setter.append(RemoveCommand.create(editingDomain, target,
						feature, removeValues));

			if (addedValues.size() > 0)
				setter.append(AddCommand.create(editingDomain, target, feature,
						addedValues));
		} else {
			setter.append(SetCommand.create(editingDomain, target, feature,
					SetCommand.UNSET_VALUE));
			if (((Collection<?>) value).size() > 0) {
				setter.append(AddCommand.create(editingDomain, target, feature,
						(Collection<?>) value));
			}
		}
		return setter;
	}
}
