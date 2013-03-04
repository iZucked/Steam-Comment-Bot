/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editorfactories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnumLiteral;
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

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

/**
 * @since 2.0
 */

public class DeliveryTypeValueListInlineEditor extends UnsettableInlineEditor {
	private Combo combo;

	private final List<String> names;
	private final List<Object> values;

	public DeliveryTypeValueListInlineEditor(final EStructuralFeature feature) {
		super(feature);

		final EList<EEnumLiteral> eLiterals = TypesPackage.eINSTANCE.getCargoDeliveryType().getELiterals();
		names = new ArrayList<String>(eLiterals.size());
		values = new ArrayList<Object>(eLiterals.size());
		for (final CargoDeliveryType type : CargoDeliveryType.values()) {
			final String name;
			switch (type) {
			case ANY:
				name = "Any";
				break;
			case DELIVERED:
				name = "Not Shipped";
				break;
			case SHIPPED:
				name = "Shipped";
				break;
			default:
				name = type.getName();
				break;

			}
			names.add(name);
			values.add(type);
		}

	}

	@Override
	protected Control createValueControl(final Composite parent) {
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

//	@Override
//	protected void updateValueDisplay(final Object value) {
//		if (combo.isDisposed()) {
//			return;
//		}
//		final int indexOf = values.indexOf(value);
//		combo.select(indexOf);
//	}

//	@Override
//	protected Control createValueControl(final Composite parent) {
//		combo = new Combo(parent, SWT.READ_ONLY);
//
//		combo.setItems(names.toArray(new String[names.size()]));
//
//		final SelectionListener sl = new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				doSetValue(values.get(combo.getSelectionIndex()), false);
//			}
//		};
//
//		combo.addSelectionListener(sl);
//		combo.addDisposeListener(new DisposeListener() {
//			@Override
//			public void widgetDisposed(DisposeEvent e) {
//				combo.removeSelectionListener(sl);
//			}
//		});
//
//		return combo;
//	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo.isDisposed()) {
			return;
		}
		combo.select(values.indexOf(value));
	}

	@Override
	protected Object getInitialUnsetValue() {
		return values.get(0);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {

		combo.setEnabled(enabled);

		super.setControlsEnabled(enabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		combo.setVisible(visible);

		super.setControlsVisible(visible);
	}
}
