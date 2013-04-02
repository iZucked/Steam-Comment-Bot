/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.widgets.Composite;

/**
 * @author hinton
 * @since 3.0
 * 
 */
public class NumericAttributeManipulator extends BasicAttributeManipulator {
	public NumericAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public Object getValue(final Object object) {
		final Object value = super.getValue(object);
		return value;
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
		final EDataType eType = (EDataType) field.getEType();

		final FormattedTextCellEditor result = new FormattedTextCellEditor(c);
		final NumberFormatter formatter;
		if (eType.equals(EcorePackage.eINSTANCE.getEInt())) {
			formatter = new IntegerFormatter();
		} else if (eType.equals(EcorePackage.eINSTANCE.getELong())) {
			formatter = new LongFormatter();
		} else if (eType.equals(EcorePackage.eINSTANCE.getEFloat())) {
			formatter = new FloatFormatter();
		} else if (eType.equals(EcorePackage.eINSTANCE.getEDouble())) {
			formatter = new DoubleFormatter();
		} else {
			formatter = null;
		}

		if (formatter != null) {
			formatter.setFixedLengths(false, false);
			result.setFormatter(formatter);
		}
		return result;
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
		final Object object2 = super.getValue(object);
		if (object2 instanceof Comparable)
			return (Comparable) object2;
		return -Integer.MAX_VALUE;
	}
}
