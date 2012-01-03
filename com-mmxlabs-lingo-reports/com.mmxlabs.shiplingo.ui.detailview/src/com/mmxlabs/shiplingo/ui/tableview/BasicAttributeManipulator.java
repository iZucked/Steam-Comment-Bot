/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;

/**
 * Displays a textbox for editing an EAttribute.
 * 
 * @author hinton
 * 
 */
public class BasicAttributeManipulator implements ICellManipulator,
		ICellRenderer {

	protected final EStructuralFeature field;
	protected final EditingDomain editingDomain;

	public BasicAttributeManipulator(final EStructuralFeature field,
			final EditingDomain editingDomain) {
		super();
		this.field = field;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		final Object value = getValue(object);
		if (value == null) return "";
		else return value.toString();
	}

	@Override
	public void setValue(final Object object, final Object value) {
		final Object currentValue = reallyGetValue(object);
		if ((currentValue == null && value == null) || ((currentValue != null && value != null) && currentValue.equals(value)))
			return;

		final Command command = editingDomain.createCommand(SetCommand.class,
				new CommandParameter((EObject) object, field, value));
		((SetCommand) command).setLabel("Set " + field.getName() + " to "
				+ (value == null ? "null" : value.toString()));
		editingDomain.getCommandStack().execute(command);
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		return new TextCellEditor(c);
	}

	@Override
	public Object getValue(Object object) {
		return reallyGetValue(object);
	}
	
	@Override
	public Object getFilterValue(Object object) {
		return getComparable(object);
	}

	private Object reallyGetValue(Object object) {
		if (object == null) return "";
		final Object result = ((EObject) object).eGet(field);
		if (result == null && (field.getEType() == EcorePackage.eINSTANCE.getEString())) {
			return "";
		} else {
			return result;
		}
	}

	@Override
	public boolean canEdit(Object object) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(Object object) {
		return render(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(
			Object object) {
		return Collections.emptySet();
	}
}
