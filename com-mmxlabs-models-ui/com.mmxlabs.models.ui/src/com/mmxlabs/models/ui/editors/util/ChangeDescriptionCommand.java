/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.change.ChangeDescription;

/**
 * Class to wrap a {@link ChangeDescription} in a {@link Command}
 * 
 * @since 3.0
 * 
 */
public class ChangeDescriptionCommand extends AbstractCommand {
	private ChangeDescription changeDescription;
	private Collection<?> affectedObjects;

	public ChangeDescriptionCommand(final ChangeDescription changeDescription, final Collection<?> affectedObjects) {
		this.changeDescription = changeDescription;
		this.affectedObjects = affectedObjects;
	}

	@Override
	public Collection<?> getAffectedObjects() {
		return affectedObjects == null ? Collections.emptyList() : affectedObjects;
	}

	@Override
	public void dispose() {
		changeDescription = null;
		affectedObjects = null;
		super.dispose();
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute() {
		changeDescription.applyAndReverse();
	}

	@Override
	public void undo() {
		changeDescription.applyAndReverse();
	}

	@Override
	public void redo() {
		changeDescription.applyAndReverse();
	}
}