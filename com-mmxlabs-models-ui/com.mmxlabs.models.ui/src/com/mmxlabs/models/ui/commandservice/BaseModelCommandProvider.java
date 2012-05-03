/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.commandservice;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A typical command provider. Specialise by implementing
 * <ol>
 * <li>
 * {@link #shouldHandleAddition(Object)}
 * </li>
 * <li> {@link #shouldHandleDeletion(Object)} </li>
 * <li> {@link #objectAdded(EditingDomain, MMXRootObject, Object)} </li>
 * <li> {@link #objectDeleted(EditingDomain, MMXRootObject, Object)} </li>
 * <ol>
 * @author hinton
 *
 */
public abstract class BaseModelCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(
			EditingDomain editingDomain, 
			MMXRootObject rootObject, 
			Class<? extends Command> commandClass, 
			CommandParameter parameter, 
			Command input) {
		
		if (commandClass == AddCommand.class) {
			return handleAddition(editingDomain, rootObject, collect(parameter));
		} else if (commandClass == DeleteCommand.class) {
			return handleDeletion(editingDomain, rootObject, collect(parameter));
		}
		
		return null;
	}
	
	/**
	 * @param parameter
	 * @return
	 */
	private Collection<Object> collect(CommandParameter parameter) {
		if (parameter.getCollection() != null) return (Collection<Object>) parameter.getCollection();
		else if (parameter.getValue() != null) return Collections.singleton(parameter.getValue());
		return Collections.emptySet();
	}

	protected Command unwrap(final CompoundCommand cc) {
		final Command result = cc.unwrap();
		if (result == UnexecutableCommand.INSTANCE) return null;
		return result;
	}
	
	protected Command handleAddition(final EditingDomain domain, final MMXRootObject root, final Collection<Object> added) {
		final CompoundCommand compound = new CompoundCommand();
		
		for (final Object o : added) {
			if (shouldHandleAddition(o)) {
				final Command a = objectAdded(domain, root, o);
				if (a != null) compound.append(a);
			}
		}
		
		return unwrap(compound);
	}
	
	protected Command handleDeletion(EditingDomain editingDomain, MMXRootObject rootObject, final Collection<Object> deleted) {
		final CompoundCommand compound = new CompoundCommand();
		
		for (final Object o : deleted) {
			if (shouldHandleDeletion(o)) {
				final Command a = objectDeleted(editingDomain, rootObject, o);
				if (a != null) compound.append(a);
			}
		}
		
		return unwrap(compound);
	}
	
	protected boolean shouldHandleAddition(final Object addedObject) {
		return false;
	}
	protected boolean shouldHandleDeletion(final Object deletedObject){
		return false;
	}
	
	protected Command objectAdded(final EditingDomain domain, MMXRootObject rootObject, Object added) {
		return null;
	}
	protected Command objectDeleted(final EditingDomain domain, MMXRootObject rootObject, Object deleted){
		return null;
	}

	private ThreadLocal<AtomicInteger> provisionStack = new ThreadLocal<AtomicInteger>();
	private ThreadLocal<Object> provisionContext = new ThreadLocal<Object>();
	
	protected void setContext(final Object context) {
		provisionContext.set(context);
	}
	
	protected Object getContext() {
		return provisionContext.get();
	}
	
	protected int getProvisionDepth() {
		return provisionStack.get().get();
	}
	
	@Override
	public void startCommandProvision() {
		if (provisionStack.get() == null) {
			provisionStack.set(new AtomicInteger(0));
		}
		if (provisionStack.get().getAndIncrement() == 0) {
			provisionContext.set(null);
		}
	}

	@Override
	public void endCommandProvision() {
		if (provisionStack.get().decrementAndGet() == 0) {
			provisionContext.set(null);
		}
	}
}
