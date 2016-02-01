/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.editorfactories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;

/**
 */

public class DestinationTypeValueListInlineEditor extends BasicAttributeInlineEditor {
	private Combo combo;

	private final List<String> names;
	private final List<Object> values;

	public DestinationTypeValueListInlineEditor(final EStructuralFeature feature) {
		super(feature);

		final EList<EEnumLiteral> eLiterals = AnalyticsPackage.eINSTANCE.getDestinationType().getELiterals();
		names = new ArrayList<String>(eLiterals.size());
		values = new ArrayList<Object>(eLiterals.size());
		for (final DestinationType type : DestinationType.values()) {
			names.add(type.getName());
			values.add(type);
		}

	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		boolean controlsEnabled = !isFeatureReadonly() && enabled;
		combo.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		combo.setVisible(visible);

		super.setControlsVisible(visible);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		combo = new Combo(parent, SWT.READ_ONLY);

		combo.setItems(names.toArray(new String[names.size()]));

		final SelectionListener sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doSetValue(values.get(combo.getSelectionIndex()), false);
			}
		};

		combo.addSelectionListener(sl);
		combo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				combo.removeSelectionListener(sl);
			}
		});

		return wrapControl(combo);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		super.display(dialogContext, context, input, range);
		if (input != null) {
			final Object obj = input.eGet(feature);
			if (obj instanceof DestinationType) {
				final DestinationType destinationType = (DestinationType) obj;
				if (destinationType == DestinationType.START) {
					combo.setEnabled(false);
				} else if (destinationType == DestinationType.END) {
					combo.setEnabled(false);
				} else {
					combo.setEnabled(true);
				}
			}
		}

	}

	@Override
	protected void updateDisplay(final Object value) {
		if (combo.isDisposed()) {
			return;
		}
		final int indexOf = values.indexOf(value);
		combo.select(indexOf);
	}
}
