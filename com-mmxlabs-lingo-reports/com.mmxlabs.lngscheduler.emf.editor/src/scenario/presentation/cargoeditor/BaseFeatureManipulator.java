/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.presentation.cargoeditor.IFeatureEditor.IFeatureManipulator;

/**
 * Some basic functionality for most feature manipulators
 * 
 * @author hinton
 * 
 */
public abstract class BaseFeatureManipulator implements IFeatureManipulator {
	protected final EStructuralFeature field;
	protected final List<EReference> path;
	protected final EditingDomain editingDomain;

	protected BaseFeatureManipulator(final List<EReference> path,
			final EStructuralFeature field, final EditingDomain editingDomain) {
		this.path = path;
		this.field = field;
		this.editingDomain = editingDomain;
	}

	protected EObject getTarget(EObject o) {
		final Iterator<EReference> it = path.iterator();
		while (o != null && it.hasNext()) {
			o = (EObject) o.eGet(it.next());
		}
		return o;
	}

	protected Object getFieldValue(final EObject o) {
		final EObject target = getTarget(o);
		return target == null ? null : target.eGet(field);
	}

	protected void doSetCommand(final EObject target, final Object newValue) {
		if (newValue.equals(target.eGet(field))) return; // don't do edits which do nothing
		editingDomain.getCommandStack().execute(
				editingDomain.createCommand(SetCommand.class,
						new CommandParameter(target, field, newValue)));
	}
}
