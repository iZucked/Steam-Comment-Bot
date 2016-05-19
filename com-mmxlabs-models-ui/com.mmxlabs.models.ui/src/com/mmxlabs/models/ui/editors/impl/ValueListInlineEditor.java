/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Pair;

/**
 * An inline editor for picking a value from a fixed list of values.
 * 
 * It's constructed with a list of name/value pairs. The names are displayed for the corresponding value objects.
 * 
 * @author hinton
 * 
 */
public class ValueListInlineEditor extends UnsettableInlineEditor {

	/**
	 * Create a new List<Pair<String, Object>> from the list of elements. This method assumes an even number of elements in the form { label1, enum1, label2, enum2...}
	 * 
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static List<Pair<String, Object>> createPairList(Object[] elements) {
		final LinkedList<Pair<String, Object>> pairList = new LinkedList<Pair<String, Object>>();
		for (int i = 0; i < elements.length; i += 2) {
			final String key = (String) elements[i];
			final Object value = (Object) elements[i + 1];
			pairList.add(new Pair<String, Object>(key, value));
		}
		return pairList;
	}

	private Combo combo;

	private final ArrayList<String> names;
	private final ArrayList<Object> values;

	public ValueListInlineEditor(final EStructuralFeature feature, final List<Pair<String, Object>> values) {
		super(feature);
		this.names = new ArrayList<String>(values.size());
		this.values = new ArrayList<Object>(values.size());
		for (final Pair<String, Object> pair : values) {
			this.names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
	}

	protected void updateCombo(final List<Pair<String, Object>> values) {
		this.names.clear();
		this.values.clear();

		this.names.ensureCapacity(values.size());
		this.values.ensureCapacity(values.size());

		for (final Pair<String, Object> pair : values) {
			this.names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
		
		combo.setItems(names.toArray(new String[names.size()]));

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
		combo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				combo.removeSelectionListener(sl);
			}
		});

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
		return values.get(0);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		combo.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		combo.setVisible(visible);

		super.setControlsVisible(visible);
	}
}
