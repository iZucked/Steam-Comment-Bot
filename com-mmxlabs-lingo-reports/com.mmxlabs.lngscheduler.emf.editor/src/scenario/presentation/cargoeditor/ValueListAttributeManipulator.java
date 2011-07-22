/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;

/**
 * Analog to
 * {@link scenario.presentation.cargoeditor.detailview.ValueListInlineEditor},
 * but in column form.
 * 
 * You can specify a list of name/value pairs at construction time and the
 * editor will display a combo with the names which sets the values.
 * 
 * @author hinton
 * 
 */
public class ValueListAttributeManipulator extends BasicAttributeManipulator {
	private final ArrayList<String> names;
	private final ArrayList<Object> values;

	public ValueListAttributeManipulator(final EAttribute field,
			final EditingDomain editingDomain,
			final List<Pair<String, Object>> values) {
		super(field, editingDomain);
		names = new ArrayList<String>(values.size());
		this.values = new ArrayList<Object>(values.size());
		for (final Pair<String, Object> pair : values) {
			names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		return new ComboBoxCellEditor(c,
				names.toArray(new String[values.size()]));
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// value is an Integer
		final int intValue = ((Integer) value).intValue();

		if (intValue == -1) {
		} else {
			super.setValue(object, values.get(intValue));
		}
	}

	@Override
	public Object getValue(final Object object) {
		return values.indexOf(super.getValue(object));
	}

	@Override
	public String render(final Object object) {
		final int index = values.indexOf(super.getValue(object));
		if (index == -1)
			return super.render(object); //fallback to superclass?
		return names.get(index);
	}

}
