/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;

/**
 * Class to wrap a {@link ChangeDescription} in a {@link Command}
 * 
 * 
 */
public class ChangeDescriptionCommand extends CompoundCommand {
	private ChangeDescription changeDescription;
	private Collection<?> affectedObjects;
	private final Map<Pair<EObject, EReference>, Collection<EObject>> objectsAdded;
	private final Collection<EObject> objectsRemoved;
	private final EditingDomain domain;

	public ChangeDescriptionCommand(final EditingDomain domain, final ChangeDescription changeDescription, final Collection<?> affectedObjects,
			final Map<Pair<EObject, EReference>, Collection<EObject>> objectsAdded, final Collection<EObject> objectsRemoved) {
		this.domain = domain;
		this.changeDescription = changeDescription;
		this.affectedObjects = affectedObjects;
		this.objectsAdded = objectsAdded;
		this.objectsRemoved = objectsRemoved;
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
		for (final Map.Entry<Pair<EObject, EReference>, Collection<EObject>> e : objectsAdded.entrySet()) {
			final Pair<EObject, EReference> p = e.getKey();
			final Collection<EObject> objects = e.getValue();
			appendAndExecute(AddCommand.create(domain, p.getFirst(), p.getSecond(), objects));
		}
		appendAndExecute(DeleteCommand.create(domain, objectsRemoved));
	}

	@Override
	public void undo() {
		super.undo();
		changeDescription.applyAndReverse();
	}

	@Override
	public void redo() {
		changeDescription.applyAndReverse();
		super.redo();
	}
}