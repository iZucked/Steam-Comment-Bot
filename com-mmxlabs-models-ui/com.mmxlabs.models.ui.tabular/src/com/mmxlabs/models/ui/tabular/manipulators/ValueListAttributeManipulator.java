/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;

/**
 * Analog to {@link com.mmxlabs.shiplingo.ui.detailview.editors.ValueListInlineEditor}, but in column form.
 * 
 * You can specify a list of name/value pairs at construction time and the editor will display a combo with the names which sets the values.
 * 
 * @author hinton
 * 
 */
public class ValueListAttributeManipulator extends BasicAttributeManipulator {
	private final ArrayList<Object> valueList;
	private final ArrayList<String> names;

	public ValueListAttributeManipulator(final EAttribute field, final EditingDomain editingDomain, final List<Pair<String, Object>> values) {
		super(field, editingDomain);
		names = new ArrayList<String>(values.size());
		this.valueList = new ArrayList<Object>(values.size());
		for (final Pair<String, Object> pair : values) {
			names.add(pair.getFirst());
			this.valueList.add(pair.getSecond());
		}
	}

	@Override
	public String render(final Object object) {
		Object value = super.getValue(object);
		final int index = valueList.indexOf(value);
		if (index == -1) {
			return value == null ? "" : value.toString();
		}
		return names.get(index);
	}
	
//	@Override
//	public String renderSetValue(final Object o, final Object object) {
//		final Object value = super.getValue(object);
//		final int index = valueList.indexOf(value);
//		if (index == -1) {
//			return value == null ? "" : value.toString();
//		}
//		return names.get(index);
//	}

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
		return new ComboBoxCellEditor(c, names.toArray(new String[valueList.size()]));
	}

	@Override
	public Object getValue(final Object object) {
		return valueList.indexOf(super.getValue(object));
	}

}
