/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * @author hinton
 * 
 */
public class ReferenceInlineEditor extends UnsettableInlineEditor {
	private static final Logger log = LoggerFactory.getLogger(ReferenceInlineEditor.class);
	/**
	 * @since 2.0
	 */
	protected Combo combo;
	/**
	 * @since 2.0
	 */
	protected IReferenceValueProvider valueProvider;

	/**
	 * @since 2.0
	 */
	protected final ArrayList<String> nameList = new ArrayList<String>();
	/**
	 * @since 2.0
	 */
	protected final ArrayList<EObject> valueList = new ArrayList<EObject>();

	public ReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		if (input == null) {
			valueProvider = null;
		} else {
			valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) feature);
			if (valueProvider == null) {
				log.error("Could not get a value provider for " + input.eClass().getName() + "." + feature.getName());
			}
		}
		super.display(location, context, input, range);
	}

	@Override
	public Control createValueControl(final Composite parent) {
		this.combo = new Combo(parent, SWT.READ_ONLY);
		// this.combo = toolkit.createCombo(parent, SWT.READ_ONLY);
		toolkit.adapt(combo, true, true);

		combo.setEnabled(isEditorEnabled());

		combo.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				combo.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						combo.removeSelectionListener(sl);
					}
				});
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				doSetValue(valueList.get(nameList.indexOf(combo.getText())), false);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		return combo;
	}

	@Override
	protected void updateControl() {
		if (combo.isDisposed()) {
			return;
		}
		final List<Pair<String, EObject>> values = valueProvider != null ? valueProvider.getAllowedValues(input, feature) : Collections.<Pair<String, EObject>> emptyList();
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

	/**
	 * @since 2.0
	 */
	@Override
	protected void updateDisplay(final Object target) {

		super.updateDisplay(getValue());
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo.isDisposed()) {
			return;
		}
		final int curIndex = valueList.indexOf(value);
		if (curIndex == -1) {
			combo.setText("");
		} else {
			combo.setText(nameList.get(curIndex));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		if (valueProvider != null) {
			return valueProvider.updateOnChangeToFeature(changedFeature);
		}
		return false;
	}

	@Override
	public void setControlsEnabled(final boolean enabled) {
		if (combo != null && !combo.isDisposed()) {
			combo.setEnabled(enabled);
		}

		super.setControlsEnabled(enabled);
	}

	@Override
	public void setControlsVisible(final boolean visible) {
		if (!combo.isDisposed()) {
			combo.setVisible(visible);
		}

		super.setControlsVisible(visible);
	}
}
