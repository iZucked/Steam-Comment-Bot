/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * An inline editor for picking a value from a fixed list of values.
 * 
 * It's constructed with a list of name/value pairs. The names are displayed for the corresponding value objects.
 * 
 * @author hinton
 * 
 */
public class ValueListInlineEditor extends UnsettableInlineEditor {
	private Combo combo;

	private final List<String> names;
	private final List<Object> values;

	public ValueListInlineEditor(final EMFPath path, final EStructuralFeature feature, final EditingDomain editingDomain, final ICommandProcessor processor, final List<Pair<String, Object>> values) {
		super(path, feature, editingDomain, processor);
		names = new ArrayList<String>(values.size());
		this.values = new ArrayList<Object>(values.size());
		for (final Pair<String, Object> pair : values) {
			this.names.add(pair.getFirst());
			this.values.add(pair.getSecond());
		}
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY);

		combo.setItems(names.toArray(new String[names.size()]));

		final SelectionListener sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doSetValue(values.get(combo.getSelectionIndex()));
			}
		};

		combo.addSelectionListener(sl);
		combo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				combo.removeSelectionListener(sl);
			}
		});

		return combo;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		combo.select(values.indexOf(value));
	}

	@Override
	protected Object getInitialUnsetValue() {
		return values.get(0);
	}
}
