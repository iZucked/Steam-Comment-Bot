/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * A column manipulator for setting single-valued EReference features.
 * 
 * Uses {@link ComboBoxCellEditor} for its edit control, and takes the values from an {@link IReferenceValueProvider}.
 * 
 * @author hinton
 * 
 */
public class SingleReferenceManipulator extends BasicAttributeManipulator {
	private static final Logger log = LoggerFactory.getLogger(SingleReferenceManipulator.class);
	final IReferenceValueProvider valueProvider;
	final EditingDomain editingDomain;

	private ComboBoxCellEditor editor;

	/**
	 * Create a manipulator for the given field in the target object, taking values from the given valueProvider and creating set commands in the provided editingDomain.
	 * 
	 * @param field
	 *            the field to set
	 * @param valueProvider
	 *            provides the names & values for the field
	 * @param editingDomain
	 *            editing domain for setting
	 */
	public SingleReferenceManipulator(final EReference field, final IReferenceValueProvider valueProvider, final EditingDomain editingDomain) {
		super(field, editingDomain);

		this.valueProvider = valueProvider;
		this.editingDomain = editingDomain;
	}
	
	public SingleReferenceManipulator(final EReference field, final IReferenceValueProviderProvider valueProviderProvider, final EditingDomain editingDomain) {
		this(field, valueProviderProvider.getReferenceValueProvider(field.getEContainingClass(), field), editingDomain);
	}


	@Override
	public String render(final Object object) {
		Object superValue = super.getValue(object);
		if (super.getValue(object) == SetCommand.UNSET_VALUE) {
			if (object instanceof MMXObject) {
				final Object defaultValue = ((MMXObject) object).getUnsetValue(field);
				if (defaultValue instanceof EObject || defaultValue == null) {
					return valueProvider.getName((EObject) object, (EReference) field, (EObject) defaultValue);
				}
			}
		} else {
			final Object value = super.getValue(object);
			if ((value instanceof EObject) || (value == null)) {
				return valueProvider.getName((EObject) object, (EReference) field, (EObject) value);
			} else {
				return "";
			}	
		}
		return "";
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		if (value.equals(-1)) return;
		final EObject newValue = valueList.get((Integer) value);
		super.runSetCommand(object, newValue);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		editor = new ComboBoxCellEditor(c, new String[0], SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		// editor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
		setEditorNames();
		return editor;
	}

	final ArrayList<EObject> valueList = new ArrayList<EObject>();
	final ArrayList<String> names = new ArrayList<String>();

	@Override
	public Object getValue(final Object object) {
		final int x = valueList.indexOf(super.getValue(object));
		if (x == -1) {
			log.warn("Index of " + object + " to be selected is -1, so it is not a legal option in the control");
		}
		return x;
	}

	@Override
	public boolean canEdit(final Object object) {
		// get legal item list
		final Iterable<Pair<String, EObject>> values = valueProvider.getAllowedValues((EObject) object, field);

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
		if (editor == null) {
			return;
		}
		editor.setItems(names.toArray(new String[] {}));
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		final Object value = super.getValue(object);
		if (value instanceof EObject) {
			return valueProvider.getNotifiers((EObject) object, (EReference) field, (EObject) value);
		} else {
			return super.getExternalNotifiers(object);
		}
	}
}
