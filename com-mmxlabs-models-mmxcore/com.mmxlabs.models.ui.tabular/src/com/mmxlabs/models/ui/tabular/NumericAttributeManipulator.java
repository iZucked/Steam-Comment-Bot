/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.rcp.common.celleditors.NumberCellEditor;

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
	public String doRender(final Object object) {
		final Object val = getValue(object);
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
		//TODO fixme
//		final NumberCellEditor editor = new NumberCellEditor(c, new NumberVerifyListener(eType), null, new NumberCellEditor.INumberConvertor() {
//
//			@Override
//			public String toString(final Number number) {
//				return NumberTypes.toString(eType, number);
//			}
//
//			@Override
//			public Number toNumber(final String string) {
//				try {
//					return NumberTypes.toNumber(eType, string);
//				} catch (final ParseException e) {
//					throw new RuntimeException(e);
//				}
//			}
//		});
//		return editor;
		
		final FormattedTextCellEditor result = new FormattedTextCellEditor(c);
		result.setFormatter(new DoubleFormatter());
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
