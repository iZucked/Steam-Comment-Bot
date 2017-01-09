/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.OverrideableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * An implementation of {@link EditingDomain} wrapping another instance and delegating all methods to the wrapped instance. It is intended that clients sub-class this class and override methods as
 * required.
 * 
 */
public class WrappedEditingDomain implements EditingDomain {

	private final EditingDomain domain;

	public WrappedEditingDomain(final EditingDomain domain) {
		this.domain = domain;
	}

	@Override
	public TreeIterator<?> treeIterator(final Object object) {

		return domain.treeIterator(object);
	}

	@Override
	public void setClipboard(final Collection<Object> clipboard) {

		domain.setClipboard(clipboard);
	}

	@Override
	public Resource loadResource(final String fileNameURI) {

		return domain.loadResource(fileNameURI);
	}

	@Override
	public boolean isReadOnly(final Resource resource) {

		return domain.isReadOnly(resource);
	}

	@Override
	public boolean isControllable(final Object object) {

		return domain.isControllable(object);
	}

	@Override
	public List<?> getTreePath(final Object object) {

		return domain.getTreePath(object);
	}

	@Override
	public Object getRoot(final Object object) {

		return domain.getRoot(object);
	}

	@Override
	public ResourceSet getResourceSet() {

		return domain.getResourceSet();
	}

	@Override
	public Object getParent(final Object object) {

		return domain.getParent(object);
	}

	@Override
	public boolean getOptimizeCopy() {

		return domain.getOptimizeCopy();
	}

	@Override
	public Collection<?> getNewChildDescriptors(final Object object, final Object sibling) {

		return domain.getNewChildDescriptors(object, sibling);
	}

	@Override
	public CommandStack getCommandStack() {

		return domain.getCommandStack();
	}

	@Override
	public Collection<Object> getClipboard() {

		return domain.getClipboard();
	}

	@Override
	public Collection<?> getChildren(final Object object) {

		return domain.getChildren(object);
	}

	@Override
	public Resource createResource(final String fileNameURI) {

		return domain.createResource(fileNameURI);
	}

	@Override
	public Command createOverrideCommand(final OverrideableCommand command) {

		return domain.createOverrideCommand(command);
	}

	@Override
	public Command createCommand(final Class<? extends Command> commandClass, final CommandParameter commandParameter) {

		return domain.createCommand(commandClass, commandParameter);
	}
}