/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * @author hinton
 * 
 */

public class ReferenceInlineEditor extends UnsettableInlineEditor {
	private static final Logger log = LoggerFactory.getLogger(ReferenceInlineEditor.class);
	/**
	 */
	protected Combo combo;
	/**
	 */
	protected IReferenceValueProvider valueProvider;

	/**
	 */
	protected final List<String> nameList = new ArrayList<>();
	/**
	 */
	protected final List<EObject> valueList = new ArrayList<>();

	/**
	 */
	protected IItemPropertyDescriptor propertyDescriptor = null;

	public ReferenceInlineEditor(final ETypedElement typedElement) {
		super(typedElement);
	}

	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		if (typedElement instanceof EStructuralFeature feature) {
			EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
			if (eAnnotation == null) {
				eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
			}
			if (eAnnotation != null) {
				for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
					if (f.getName().equals(feature.getName() + "Override")) {
						isOverridable = true;
						this.overrideToggleFeature = f;
					}
				}
				if (feature.isUnsettable()) {
					isOverridable = true;
				}
			}
			if (isOverridable) {
				isOverridableWithButton = true;
			}
		}
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		if (input == null) {
			valueProvider = null;
		} else {
			valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) typedElement);
			if (valueProvider == null) {
				log.error("Could not get a value provider for " + input.eClass().getName() + "." + typedElement.getName());
			}
		}
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected void updateControl() {
		if (combo == null || combo.isDisposed()) {
			return;
		}

		final List<Pair<String, EObject>> values = getValues();
		// update combo contents
		combo.removeAll();
		nameList.clear();
		valueList.clear();

		for (final Pair<String, EObject> object : values) {
			valueList.add(object.getSecond());
			nameList.add(object.getFirst());
			combo.add(object.getFirst());
		}
		super.updateControl();
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}

	@Override
	public Control createValueControl(final Composite parent) {
		this.combo = new Combo(parent, SWT.READ_ONLY);
		// this.combo = toolkit.createCombo(parent, SWT.READ_ONLY);
		toolkit.adapt(combo, true, true);

		if (typedElement instanceof EOperation) {
			combo.setEnabled(false);
		} else if (typedElement instanceof EStructuralFeature feature) {
			combo.setEnabled(feature.isChangeable() && isEditorEnabled());
		}

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
				int indexOf = nameList.indexOf(combo.getText());
				if (indexOf >= 0) {
					doSetValue(valueList.get(indexOf), false);
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		return super.wrapControl(combo);

	}

	protected List<Pair<String, EObject>> getValues() {
		return valueProvider != null ? valueProvider.getAllowedValues(input, typedElement) : Collections.<Pair<String, EObject>> emptyList();
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo == null || combo.isDisposed()) {
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
	protected boolean updateOnChangeToFeature(final Object changedFeature) {

		if (valueProvider != null) {
			boolean b = valueProvider.updateOnChangeToFeature(changedFeature);
			if (b) {
				return true;
			}
		}
		return super.updateOnChangeToFeature(changedFeature);
	}

	@Override
	public void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (combo != null && !combo.isDisposed()) {
			combo.setEnabled(controlsEnabled);
		}

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	public void setControlsVisible(final boolean visible) {
		if (!combo.isDisposed()) {
			combo.setVisible(visible);
		}

		super.setControlsVisible(visible);
	}
}
