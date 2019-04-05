/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * An implementation of {@link BasicAttributeManipulator} to handle boolean objects. By default this will show a Y/N choice, but this can changed using the alternative constructor,
 * 
 * @author Simon Goodall
 */
public class BooleanFlagAttributeManipulator extends BasicAttributeManipulator {

	private final String trueString;
	private final String falseString;

	public BooleanFlagAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		this(field, editingDomain, "\u2713", "");
	}

	public BooleanFlagAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain, final String trueString, final String falseString) {
		super(field, editingDomain);
		this.trueString = trueString;
		this.falseString = falseString;
	}

	@Override
	protected CellEditor createCellEditor(final Composite parent, final Object object) {
		return new CheckboxCellEditor(parent, SWT.CHECK);
	}

	@Override
	public void doSetValue(Object object, Object value) {

		if (value instanceof Integer) {
			super.doSetValue(object, (((Integer) value).intValue() == 0));
		} else {
			super.doSetValue(object, value);
		}
	}

	@Override
	public Object getValue(final Object object) {

		Object object2 = super.getValue(object);
		if (object2 instanceof Boolean) {
			return (Boolean) object2;
		}

		return false;
	}

	@Override
	public boolean canEdit(final Object object) {
		return true;
	}

	@Override
	protected String renderSetValue(final Object container, final Object setValue) {
		if (setValue instanceof Boolean) {
			Boolean value = (Boolean) setValue;
			if (value == Boolean.TRUE) {
				return trueString;
			}
			if (value == Boolean.FALSE) {
				return falseString;
			}
		}
		return "";
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return render(object);
	}

}