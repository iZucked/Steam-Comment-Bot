/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * Simple implementation of {@link CommandStack} wrapping a {@link ICommandHandler} which performs the real command execution. This class does not implement all methods on {@link CommandStack}
 * 
 */
public class CommandHandlerCommandStack implements CommandStack {

	private final ICommandHandler handler;
	private final EObject target;
	private final EStructuralFeature feature;

	private final Set<CommandStackListener> listeners = new HashSet<CommandStackListener>();

	public CommandHandlerCommandStack(final ICommandHandler handler, final EObject target, final EStructuralFeature feature) {
		this.handler = handler;
		this.target = target;
		this.feature = feature;
	}

	@Override
	public void execute(final Command command) {
		handler.handleCommand(command, target, feature);
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void undo() {

	}

	@Override
	public boolean canRedo() {
		return false;
	}

	@Override
	public Command getUndoCommand() {
		return null;
	}

	@Override
	public Command getRedoCommand() {
		return null;
	}

	@Override
	public Command getMostRecentCommand() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void redo() {

	}

	@Override
	public void flush() {

	}

	@Override
	public void addCommandStackListener(final CommandStackListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeCommandStackListener(final CommandStackListener listener) {
		listeners.remove(listener);

	}
}