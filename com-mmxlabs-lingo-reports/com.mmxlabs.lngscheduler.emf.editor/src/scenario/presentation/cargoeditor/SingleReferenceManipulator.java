/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.NameList;

import com.mmxlabs.common.Pair;

/**
 * @author hinton
 * 
 */
public class SingleReferenceManipulator extends BasicAttributeManipulator {
	final IReferenceValueProvider valueProvider;
	final EditingDomain editingDomain;

	private ComboBoxCellEditor editor;

	public SingleReferenceManipulator(final EReference field,
			final IReferenceValueProvider valueProvider,
			final EditingDomain editingDomain) {
		super(field, editingDomain);

		this.valueProvider = valueProvider;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		final EObject value = (EObject) super.getValue(object);

		int x = valueList.indexOf(value);
		if (x == -1)
			return "empty";
		else
			return names.get(x);

	}

	@Override
	public void setValue(final Object object, final Object value) {
		final EObject newValue = valueList.get((Integer) value);
		super.setValue(object, newValue);
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		editor = new ComboBoxCellEditor(c, new String[0]);
		setEditorNames();
		return editor;
	}

	final ArrayList<EObject> valueList = new ArrayList<EObject>();
	final ArrayList<String> names = new ArrayList<String>();

	@Override
	public Object getValue(final Object object) {
		int x = valueList.indexOf(super.getValue(object));
		if (x == -1) {
			System.err.println("index of " + object + ", "
					+ super.getValue(object) + " is -1!");
		}
		return x;
	}

	@Override
	public boolean canEdit(final Object object) {
		// get legal item list
		final Iterable<Pair<String, EObject>> values = valueProvider
				.getAlloweValues((EObject) object, field);

		valueList.clear();
		names.clear();
		for (final Pair<String, EObject> value : values) {
			names.add(value.getFirst());
			valueList.add(value.getSecond());
		}

		

		setEditorNames();
		return valueList.size() > 0;
	}

	void setEditorNames() {
		editor.setItems(names.toArray(new String[] {}));
	}

}
