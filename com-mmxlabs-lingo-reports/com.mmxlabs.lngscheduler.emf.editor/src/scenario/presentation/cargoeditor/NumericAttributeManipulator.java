/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

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
public class NumericAttributeManipulator extends BasicAttributeManipulator {
	public NumericAttributeManipulator(EStructuralFeature field,
			EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public String render(Object object) {
		Object val = getValue(object);
		if (val == null) return "";
		if (val instanceof Integer) {
			return String.format("%,d", (Integer) val);
		}
		else
			return val.toString();
	}

	@Override
	public CellEditor getCellEditor(Composite c, Object object) {
		final SpinnerCellEditor editor = new SpinnerCellEditor(c);
		
		if (field.getEType().equals(EcorePackage.eINSTANCE.getEInt())
				|| field.getEType().equals(EcorePackage.eINSTANCE.getELong())
		) {
			editor.setDigits(0);
		} else {
			editor.setDigits(3);
		}
		
		editor.setMaximumValue(10000000);
		return editor;
	}
	
	

	@Override
	public void setValue(Object object, Object value) {
		if (field.getEType().equals(EcorePackage.eINSTANCE.getEInt())) {
			value = Integer.valueOf(((Number)value).intValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			value = Long.valueOf(((Number)value).longValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEFloat())) {
			value = Float.valueOf(((Number)value).floatValue());
		} else if (field.getEType().equals(EcorePackage.eINSTANCE.getEDouble())) {
			value = Double.valueOf(((Number)value).doubleValue());
		}
		super.setValue(object, value);
	}

	@Override
	public Comparable getComparable(Object object) {
		return (Comparable) super.getValue(object);
	}
}
