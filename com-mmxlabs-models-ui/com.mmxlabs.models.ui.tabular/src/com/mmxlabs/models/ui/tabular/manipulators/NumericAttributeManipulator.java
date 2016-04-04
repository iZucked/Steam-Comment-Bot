/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * @author hinton
 * 
 */
public class NumericAttributeManipulator extends BasicAttributeManipulator {

	private final EDataType type;

	private NumberFormatter formatter;
	private int scale = 1;

	public NumericAttributeManipulator(final EStructuralFeature feature, final EditingDomain editingDomain) {
		super(feature, editingDomain);
		type = (EDataType) feature.getEType();

		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/numberFormat");
		String format = null;

		if (annotation != null) {
			if (annotation.getDetails().containsKey("formatString")) {
				format = annotation.getDetails().get("formatString");
			}

			if (annotation.getDetails().containsKey("scale")) {
				scale = Integer.parseInt(annotation.getDetails().get("scale"));
			}
		}

		if (type == EcorePackage.eINSTANCE.getELong()) {
			formatter = format == null ? new LongFormatter() : new LongFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			formatter = format == null ? new IntegerFormatter() : new IntegerFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			formatter = format == null ? new FloatFormatter() : new FloatFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			formatter = format == null ? new DoubleFormatter() : new DoubleFormatter(format);
		}
		if (format == null)
			formatter.setFixedLengths(false, false);

	}

	//
	@Override
	public Object getValue(final Object object) {
		final Object value = super.getValue(object);
		return scale(value);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		super.doSetValue(object, descale(value));
	}

	@Override
	public Comparable getComparable(final Object object) {
		Object object2 = super.getValue(object);
		if (object2 == null || object2 == SetCommand.UNSET_VALUE) {
			if ((object instanceof EObject) && (field.isUnsettable()) && !((EObject) object).eIsSet(field)) {
				object2 = (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : null;
			}
		}
		if (object2 instanceof Comparable) {
			return (Comparable) object2;
		}
		return -Integer.MAX_VALUE;
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		if (setValue == null) {
			return null;
		}
		formatter.setValue(setValue);
//		return setValue == null ? null : 
			return formatter.getDisplayString();//setValue.toString();
	}

	

	@Override
	protected CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c);

		editor.setFormatter(formatter);
		return editor;
	}

	private Object scale(final Object internalValue) {
		
		if (scale == 1) {
			return internalValue;
		}
		
		if (internalValue instanceof Integer) {
			return ((Integer) internalValue).intValue() * (double) scale;
		} else if (internalValue instanceof Long) {
			return ((Long) internalValue).longValue() * (double) scale;
		} else if (internalValue instanceof Float) {
			return ((Float) internalValue).floatValue() * (double) scale;
		} else if (internalValue instanceof Double) {
			return ((Double) internalValue).doubleValue() * (double) scale;
		}
		return internalValue;
	}

	private Object descale(final Object displayValue) {
		if (scale == 1) {
			return displayValue;
		}
		
		if (displayValue instanceof Integer) {
			return ((Integer) displayValue).intValue() / (double) scale;
		} else if (displayValue instanceof Long) {
			return ((Long) displayValue).longValue() / (double) scale;
		} else if (displayValue instanceof Float) {
			return ((Float) displayValue).floatValue() / (double) scale;
		} else if (displayValue instanceof Double) {
			return ((Double) displayValue).doubleValue() / (double) scale;
		}
		return displayValue;
	}
}
