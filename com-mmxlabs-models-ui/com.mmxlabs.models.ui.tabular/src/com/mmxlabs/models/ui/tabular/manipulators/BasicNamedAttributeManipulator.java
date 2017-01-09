/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

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
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Displays a textbox for editing an EAttribute based on name rather than specific attribute.
 * 
 * @author hinton
 */
public class BasicNamedAttributeManipulator implements ICellManipulator, ICellRenderer {
	protected final String fieldName;
	protected final EditingDomain editingDomain;

	public BasicNamedAttributeManipulator(final String fieldName, final EditingDomain editingDomain) {
		super();
		this.fieldName = fieldName;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {

		if (object == null) {
			return null;
		}

		// if the object for some reason does not support the field (e.g. it's a placeholder row in a table)
		// ignore it
		final EStructuralFeature field = getNamedField(object);
		if (object instanceof EObject && field == null) {
			return null;
		}

		if ((object instanceof EObject) && (field.isUnsettable()) && !((EObject) object).eIsSet(field)) {
			return renderUnsetValue(object, (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : null);
		} else {
			return renderSetValue(object, getValue(object));
		}
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? null : setValue.toString();
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		final EStructuralFeature field = getNamedField(object);

		if (value == SetCommand.UNSET_VALUE && field.isUnsettable()) {
			runSetCommand(object, value);
		} else {
			doSetValue(object, value);
		}
	}

	@Override
	public boolean isValueUnset(final Object object) {
		if (object == null) {
			return false;
		}
		final EStructuralFeature field = getNamedField(object);
		if (field.isUnsettable() && ((EObject) object).eIsSet(field) == false) {
			return true;
		}
		return false;
	}

	protected EStructuralFeature getNamedField(final Object object) {
		return ((EObject) object).eClass().getEStructuralFeature(fieldName);
	}

	public void runSetCommand(final Object object, final Object value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}

		final EStructuralFeature field = getNamedField(object);

		final Command command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, field, value));
		// command.setLabel("Set " + field.getName() + " to " + (value == null ? "null" : value.toString()));
		editingDomain.getCommandStack().execute(command);
	}

	public void doSetValue(final Object object, final Object value) {
		runSetCommand(object, value);
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		if (object == null) {
			return null;
		}
		final EStructuralFeature field = getNamedField(object);

		if (field.isUnsettable()) {
			final CellEditorWrapper wrapper = new CellEditorWrapper(c);
			wrapper.setDelegate(createCellEditor(wrapper.getInnerComposite(), object));
			return wrapper;
		}
		return createCellEditor(c, object);
	}

	protected CellEditor createCellEditor(final Composite c, final Object object) {
		return new TextCellEditor(c);
	}

	@Override
	public Object getValue(final Object object) {
		if (object == null) {
			return null;
		}

		return reallyGetValue(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	private Object reallyGetValue(final Object object) {
		if (object == null) {
			return null;
		}
		final EStructuralFeature field = getNamedField(object);
		if (field == null) {
			return null;
		}

		if (field.isUnsettable() && ((EObject) object).eIsSet(field) == false)
			return SetCommand.UNSET_VALUE;
		final Object result = ((EObject) object).eGet(field);
		if ((result == null) && (field.getEType() == EcorePackage.eINSTANCE.getEString())) {
			return "";
		} else {
			return result;
		}
	}

	@Override
	public boolean canEdit(final Object object) {
		return object != null && getNamedField(object) != null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}
}
