/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * Analog to {@link com.mmxlabs.shiplingo.ui.detailview.editors.ValueListInlineEditor}, but in column form.
 * 
 * You can specify a list of name/value pairs at construction time and the editor will display a combo with the names which sets the values.
 * 
 * @author hinton
 * 
 */
public class ValueListAttributeManipulator<T> extends BasicAttributeManipulator {
	protected final ArrayList<T> valueList;
	protected final ArrayList<String> names;

	public ValueListAttributeManipulator(final EAttribute field, final ICommandHandler commandHandler, final List<Pair<String, T>> values) {
		super(field, commandHandler);
		names = new ArrayList<>(values.size());
		this.valueList = new ArrayList<>(values.size());
		for (final Pair<String, T> pair : values) {
			names.add(pair.getFirst());
			this.valueList.add(pair.getSecond());
		}
	}

	@Override
	public String render(final Object object) {
		updateDisplay(object);

		Object value;
		if (isValueUnset(object)) {
			value = (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : SetCommand.UNSET_VALUE;
		} else {
			value = super.getValue(object);
		}

		if (value == SetCommand.UNSET_VALUE) {
			return "";
		}
		final int index = valueList.indexOf(value);
		if (index == -1) {
			return (value == null || value == SetCommand.UNSET_VALUE) ? "" : value.toString();
		}
		return names.get(index);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		if (value.equals(-1)) {
			return;
		}
		final Object newValue = valueList.get((Integer) value);
		super.runSetCommand(object, newValue);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		updateDisplay(object);
		return new ComboBoxCellEditor(c, names.toArray(new String[valueList.size()]));
	}

	@Override
	public Object getValue(final Object object) {
		return valueList.indexOf(super.getValue(object));
	}

	protected void updateDisplay(Object value) {
		// Do nothing, for subclasses
	}
}
