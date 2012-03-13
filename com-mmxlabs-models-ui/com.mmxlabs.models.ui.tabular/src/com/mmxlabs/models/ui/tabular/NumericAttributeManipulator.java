/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

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
import org.eclipse.swt.widgets.Composite;

/**
 * @author hinton
 * 
 */
public class NumericAttributeManipulator extends BasicAttributeManipulator {
	public NumericAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public Object getValue(final Object object) {
		// TODO Auto-generated method stub
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
		
		if (field.getEType().equals(EcorePackage.eINSTANCE.getEInt())) {
			result.setFormatter(new IntegerFormatter());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			result.setFormatter(new LongFormatter());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEFloat())) {
			result.setFormatter(new FloatFormatter());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEDouble())) {
			result.setFormatter(new DoubleFormatter());
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
		return (Comparable) super.getValue(object);
	}
}
