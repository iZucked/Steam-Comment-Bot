/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

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

/**
 * Displays a textbox for editing an EAttribute.
 * 
 * @author hinton
 * 
 */
public class BasicAttributeManipulator implements ICellManipulator, ICellRenderer {
	protected final EStructuralFeature field;
	protected final EditingDomain editingDomain;

	public BasicAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super();
		this.field = field;
		this.editingDomain = editingDomain;
	}
	
	@Override
	public String render(final Object object) {
		if ((object instanceof EObject) && !((EObject) object).eIsSet(field)) {
			return renderUnsetValue(
					(object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) :
						null);
		} else {
			return renderSetValue(
					getValue(object)
					);
		}
	}
	
	protected String renderUnsetValue(final Object unsetDefault) {
		return renderSetValue(unsetDefault);
	}
	
	protected String renderSetValue(final Object setValue) {
		return setValue == null ? "":setValue.toString();
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		if (value == SetCommand.UNSET_VALUE && field.isUnsettable()) {
			runSetCommand(object, value);
		} else {
			doSetValue(object, value);
		}
	}

	public void runSetCommand(final Object object, final Object value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}

		final Command command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, field, value));
		((SetCommand) command).setLabel("Set " + field.getName() + " to " + (value == null ? "null" : value.toString()));
		editingDomain.getCommandStack().execute(command);
	}
	
	public void doSetValue(final Object object, final Object value) {
		runSetCommand(object, value);
	}
	
	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
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
		if (field.isUnsettable() && ((EObject)object).eIsSet(field) == false) return SetCommand.UNSET_VALUE;
		final Object result = ((EObject) object).eGet(field);
		if ((result == null) && (field.getEType() == EcorePackage.eINSTANCE.getEString())) {
			return "";
		} else {
			return result;
		}
	}

	@Override
	public boolean canEdit(final Object object) {
		return true;
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
