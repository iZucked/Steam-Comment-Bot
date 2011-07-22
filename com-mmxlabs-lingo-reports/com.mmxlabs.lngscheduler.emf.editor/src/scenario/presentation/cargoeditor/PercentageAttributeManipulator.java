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
 * Editor for percentages, modifies the behaviour of
 * {@link NumericAttributeManipulator} by multiplying the displayed values by
 * 100 and adding a % when rendering.
 * 
 * @author hinton
 * 
 */
public class PercentageAttributeManipulator extends NumericAttributeManipulator {
	public PercentageAttributeManipulator(EStructuralFeature field,
			EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public String render(Object object) {
		Object val = getValue(object);
		if (val == null)
			return "";

		return String.format("%.1f%%", val);
	}

	@Override
	public CellEditor getCellEditor(Composite c, Object object) {
		final SpinnerCellEditor editor = (SpinnerCellEditor) super
				.getCellEditor(c, object);

		editor.setDigits(1);

		editor.setMaximumValue(1000);
		editor.setMinimumValue(0);

		return editor;
	}

	@Override
	public Object getValue(Object object) {
		final Number n = (Number) super.getValue(object);
		return (n.doubleValue()) * 100.0;
	}

	@Override
	public void setValue(Object object, Object value) {
		value = Double.valueOf(((Number) value).doubleValue() / 100.0);

		super.setValue(object, value);
	}

	@Override
	public Comparable getComparable(Object object) {
		return (Comparable) super.getValue(object);
	}
}
