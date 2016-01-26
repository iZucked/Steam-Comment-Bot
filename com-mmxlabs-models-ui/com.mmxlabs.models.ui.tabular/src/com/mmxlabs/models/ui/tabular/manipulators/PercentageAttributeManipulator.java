/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.rcp.common.celleditors.SpinnerCellEditor;

/**
 * Editor for percentages, modifies the behaviour of {@link NumericAttributeManipulator} by multiplying the displayed values by 100 and adding a % when rendering.
 * 
 * @author hinton
 * 
 */
public class PercentageAttributeManipulator extends NumericAttributeManipulator {
	public PercentageAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public String renderSetValue(final Object o, final Object val) {
		if (val == null) {
			return "";
		}

		return String.format("%.1f%%", val);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final SpinnerCellEditor editor = (SpinnerCellEditor) super.getCellEditor(c, object);

		editor.setDigits(1);

		editor.setMaximumValue(1000);
		editor.setMinimumValue(0);

		return editor;
	}

	@Override
	public Object getValue(final Object object) {
		final Number n = (Number) super.getValue(object);
		return n == null ? 0d : (n.doubleValue()) * 100.0;
	}

	@Override
	public void doSetValue(final Object object, Object value) {
		value = Double.valueOf(((Number) value).doubleValue() / 100.0);

		super.runSetCommand(object, value);
	}

	@Override
	public Comparable getComparable(final Object object) {
		return (Comparable) super.getValue(object);
	}
}
