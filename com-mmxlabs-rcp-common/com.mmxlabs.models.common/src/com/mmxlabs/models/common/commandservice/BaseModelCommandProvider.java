/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.common.commandservice;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A typical command provider. Specialise by implementing
 * <ol>
 * <li>
 * {@link #shouldHandleAddition(Object)}</li>
 * <li> {@link #shouldHandleDeletion(Object)}</li>
 * <li> {@link #objectAdded(EditingDomain, MMXRootObject, Object)}</li>
 * <li> {@link #objectDeleted(EditingDomain, MMXRootObject, Object)}</li>
 * <ol>
 * 
 * @author hinton
 * 
 */
public abstract class BaseModelCommandProvider<T> extends AbstractModelCommandProvider<T> {

	/**
	 */
	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		if (commandClass == AddCommand.class) {
			return handleAddition(editingDomain, rootObject, collect(parameter), overrides, editSet);
		} else if (commandClass == DeleteCommand.class) {
			return handleDeletion(editingDomain, rootObject, collect(parameter), overrides, editSet);
		}

		return null;
	}

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		return null;
	}

	/**
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<Object> collect(final CommandParameter parameter) {
		if (parameter.getCollection() != null)
			return (Collection<Object>) parameter.getCollection();
		else if (parameter.getValue() != null)
			return Collections.singleton(parameter.getValue());
		return Collections.emptySet();
	}

	protected Command unwrap(final CompoundCommand cc) {
		final Command result = cc.unwrap();
		if (result == UnexecutableCommand.INSTANCE)
			return null;
		return result;
	}

	protected Command handleAddition(final EditingDomain domain, final MMXRootObject root, final Collection<Object> added, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		final CompoundCommand compound = new CompoundCommand();

		for (final Object o : added) {
			if (shouldHandleAddition(o, overrides, editSet)) {
				final Command a = objectAdded(domain, root, o, overrides, editSet);
				if (a != null)
					compound.append(a);
			}
		}

		return unwrap(compound);
	}

	/**
	 */
	protected Command handleDeletion(final EditingDomain editingDomain, final MMXRootObject rootObject, final Collection<Object> deleted, final Map<EObject, EObject> overrides,
			final Set<EObject> editSet) {
		final CompoundCommand compound = new CompoundCommand();

		for (final Object o : deleted) {
			if (shouldHandleDeletion(o, overrides, editSet)) {
				final Command a = objectDeleted(editingDomain, rootObject, o, overrides, editSet);
				if (a != null)
					compound.append(a);
			}
		}

		return unwrap(compound);
	}

	/**
	 */
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return false;
	}

	/**
	 */
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return false;
	}

	/**
	 */
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object added, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return null;
	}

	/**
	 */
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return null;
	}
}
