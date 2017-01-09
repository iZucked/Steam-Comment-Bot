/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.rcp.common.celleditors.SpinnerCellEditor;

/**
 * @author hinton
 * 
 */
public class NumericSpinnerAttributeManipulator extends BasicAttributeManipulator {
	public NumericSpinnerAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public String renderSetValue(final Object o, final Object val) {
		if (val == null) {
			return "";
		}
		if (val instanceof Integer) {
			return String.format("%,d", (Integer) val);
		} else {
			return val.toString();
		}
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final SpinnerCellEditor editor = new SpinnerCellEditor(c);

		if (field.getEType().equals(EcorePackage.eINSTANCE.getEInt()) || field.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			editor.setDigits(0);
		} else {
			editor.setDigits(3);
		}

		editor.setMaximumValue(10000000);
		return editor;
	}

	@Override
	public void doSetValue(final Object object, Object value) {
		if (field.getEType().equals(EcorePackage.eINSTANCE.getEInt())) {
			value = Integer.valueOf(((Number) value).intValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			value = Long.valueOf(((Number) value).longValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEFloat())) {
			value = Float.valueOf(((Number) value).floatValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEDouble())) {
			value = Double.valueOf(((Number) value).doubleValue());
		}
		super.runSetCommand(object, value);
	}

	@Override
	public Comparable getComparable(final Object object) {
		return (Comparable) super.getValue(object);
	}
}
