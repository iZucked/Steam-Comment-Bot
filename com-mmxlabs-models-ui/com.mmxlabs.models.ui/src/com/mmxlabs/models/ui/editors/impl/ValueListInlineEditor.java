/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;

/**
 * An inline editor for picking a value from a fixed list of values.
 * 
 * It's constructed with a list of name/value pairs. The names are displayed for the corresponding value objects.
 * 
 * @author hinton
 * 
 */
public class ValueListInlineEditor<T> extends UnsettableInlineEditor {

	/**
	 * Create a new List<Pair<String, Object>> from the list of elements. This method assumes an even number of elements in the form { label1, enum1, label2, enum2...}
	 * 
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static <T> List<Pair<String, T>> createPairList(Object[] elements) {
		final LinkedList<Pair<String, T>> pairList = new LinkedList<>();
		for (int i = 0; i < elements.length; i += 2) {
			final String key = (String) elements[i];
			final T value = (T) elements[i + 1];
			pairList.add(new Pair<>(key, value));
		}
		return pairList;
	}

	protected Combo combo;

	protected final ArrayList<String> names;
	protected final ArrayList<T> values;

	public ValueListInlineEditor(final EStructuralFeature feature, final List<Pair<String, T>> values) {
		super(feature);
		this.names = new ArrayList<>(values.size());
		this.values = new ArrayList<>(values.size());
		for (final Pair<String, T> pair : values) {
			this.names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
	}

	//FIXME!!! add the hiding code for the cases when the override is on the feature itself!
	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (eAnnotation == null) {
			eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}
		
		if (eAnnotation != null) {		
			for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
				if (f.getName().equals(feature.getName() + "Override")) {
					isOverridable = true;
					this.overrideToggleFeature = f;
					break;
				}
			}
			if (feature.isUnsettable()) {
				isOverridable = true;
			}
		}
		if (isOverridable) {
			isOverridableWithButton = true;
		}
		return super.createControl(parent, dbc, toolkit);
	}

	protected void updateCombo(final List<Pair<String, T>> values) {
		this.names.clear();
		this.values.clear();

		this.names.ensureCapacity(values.size());
		this.values.ensureCapacity(values.size());

		for (final Pair<String, T> pair : values) {
			this.names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
		if (!combo.isDisposed()) {
			combo.setItems(names.toArray(new String[names.size()]));
		}
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY);
		toolkit.adapt(combo, true, true);

		combo.setItems(names.toArray(new String[names.size()]));

		final SelectionListener sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetValue(values.get(combo.getSelectionIndex()), false);
			}
		};

		combo.addSelectionListener(sl);
		combo.addDisposeListener(e -> combo.removeSelectionListener(sl));

		return combo;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo != null && !combo.isDisposed()) {
			combo.select(values.indexOf(value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		if (isOverridable) {
			return null;
		} else {
			return values.get(0);
		}
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (combo != null && !combo.isDisposed()) {
			combo.setEnabled(controlsEnabled);
		}

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		if (!combo.isDisposed()) {
			combo.setVisible(visible);
		}
		super.setControlsVisible(visible);
	}
}
