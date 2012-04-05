/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * @author hinton
 * 
 */
public class ReferenceInlineEditor extends UnsettableInlineEditor {
	private static final Logger log = LoggerFactory.getLogger(ReferenceInlineEditor.class);
	private Combo combo;
	private IReferenceValueProvider valueProvider;

	private final ArrayList<String> nameList = new ArrayList<String>();
	private final ArrayList<EObject> valueList = new ArrayList<EObject>();

	public ReferenceInlineEditor(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(MMXRootObject context, EObject input, final Collection<EObject> range) {
		valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) feature);
		if (valueProvider == null) {
			log.error("Could not get a value provider for " + input.eClass().getName() + "." + feature.getName());
		}
		super.display(context, input, range);
	}

	@Override
	public Control createValueControl(final Composite parent) {
		final Combo combo = new Combo(parent, SWT.READ_ONLY);
		this.combo = combo;

		combo.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				combo.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						combo.removeSelectionListener(sl);
					}
				});
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetValue(valueList.get(nameList.indexOf(combo.getText())));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		return combo;
	}

	@Override
	protected void updateControl() {
		if (combo.isDisposed())
			return;
		final List<Pair<String, EObject>> values = valueProvider
				.getAllowedValues(input, feature);
		// update combo contents
		combo.removeAll();
		nameList.clear();
		valueList.clear();

		for (final Pair<String, EObject> object : values) {
			valueList.add(object.getSecond());
			nameList.add(object.getFirst());
			combo.add(object.getFirst());
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo.isDisposed())
			return;
		final int curIndex = valueList.indexOf(value);
		if (curIndex == -1)
			combo.setText("");
		else
			combo.setText(nameList.get(curIndex));
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}

	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		return valueProvider.updateOnChangeToFeature(changedFeature);
	}
}
