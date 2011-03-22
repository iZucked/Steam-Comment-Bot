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

/**
 * @author hinton
 * 
 */
public class SingleReferenceManipulator extends BasicAttributeManipulator {
	protected String NULL_STRING = "empty";
	
	final EAttribute nameAttribute;
	final boolean allowNullValues;
	final IReferenceValueProvider valueProvider;
	final EditingDomain editingDomain;

	private ComboBoxCellEditor editor;

	public SingleReferenceManipulator(final EReference field,
			final EAttribute nameAttribute, final boolean allowNullValues,
			final IReferenceValueProvider valueProvider,
			final EditingDomain editingDomain) {
		super(field, editingDomain);
		
		this.nameAttribute = nameAttribute;
		this.allowNullValues = allowNullValues;
		this.valueProvider = valueProvider;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		final EObject value = (EObject) super.getValue(object);
		if (value == null)
			return NULL_STRING;
		else
			return value.eGet(nameAttribute).toString();
	}

	@Override
	public void setValue(final Object object, final Object value) {
		final EObject newValue = valueList.get((Integer)value);
		super.setValue(object, newValue);
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		editor = new ComboBoxCellEditor(c, new String[0]);
		setEditorNames();
		return editor;
	}
	final ArrayList<EObject> valueList = new ArrayList<EObject>();
	@Override
	public Object getValue(final Object object) {
		return (Integer) valueList.indexOf(super.getValue(object));
	}

	@Override
	public boolean canEdit(final Object object) {
		// get legal item list
		final Iterable<? extends EObject> values = valueProvider.getAllowedValues(
				(EObject) object, field);
		
		valueList.clear();
		for (final EObject value : values) {
			valueList.add(value);
		}
		
		if (allowNullValues)
			valueList.add(null);
		
		Collections.sort(valueList,
				new Comparator<EObject>() {
					@Override
					public int compare(EObject arg0, EObject arg1) {
						if (arg0 == null) return -1;
						else if (arg1 == null) return 1;
						
						return arg0.eGet(nameAttribute).toString().compareTo(
								arg1.eGet(nameAttribute).toString());
					}
		});
		setEditorNames();
		return valueList.size() > 0;
	}
	
	void setEditorNames() {
		if (editor == null) return;
		final ArrayList<String> names = new ArrayList<String>();
		for (final EObject value : valueList) {
			names.add(value == null ? NULL_STRING : value.eGet(nameAttribute).toString());
		}
		
		editor.setItems(names.toArray(new String[]{}));
	}

}
