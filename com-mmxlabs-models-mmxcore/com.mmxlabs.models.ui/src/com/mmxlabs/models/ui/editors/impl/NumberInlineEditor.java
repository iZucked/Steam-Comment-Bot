/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * TODO look at nebula formattedtext
 * @author hinton
 *
 */
public class NumberInlineEditor extends UnsettableInlineEditor implements ModifyListener, VerifyListener, DisposeListener {
	private EDataType type;

	public NumberInlineEditor(EStructuralFeature feature) {
		super(feature);
		type = (EDataType) feature.getEType();
	}

	private Text text;

	@Override
	public Control createValueControl(Composite parent) {
		text = new Text(parent, SWT.BORDER);
		
		text.addVerifyListener(this);
		text.addModifyListener(this);
		text.addDisposeListener(this);
		
		return text;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		text.setText("" + value);
	}

	@Override
	protected Object getInitialUnsetValue() {
		if (type == EcorePackage.eINSTANCE.getELong()) {
			return 0l;
		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			return (int)0;
		} else if (type == EcorePackage.eINSTANCE.getEShort()) {
			return (short) 0;
		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			return 0.0f;
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			return 0.0d;
		} else {
			return null;
		}
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		text.removeVerifyListener(this);
		text.removeModifyListener(this);
		text.removeDisposeListener(this);
		text = null;
	}

	@Override
	public void verifyText(final VerifyEvent e) {
		final String value = text.getText();
		final String newText = value.substring(0, e.start) + e.text + value.substring(e.start, e.end);
		// try and parse string as appropriate number type
		final Number nValue = parse(newText);
		e.doit = (nValue != null && nValue.doubleValue() >= 0);
	}

	private Number parse(final String newText) {
		try {
			if (type == EcorePackage.eINSTANCE.getELong()) {
				return Long.parseLong(newText);
			} else if (type == EcorePackage.eINSTANCE.getEInt()) {
				return Integer.parseInt(newText);
			} else if (type == EcorePackage.eINSTANCE.getEShort()) {
				return Short.parseShort(newText);
			} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
				return Float.parseFloat(newText);
			} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
				return Double.parseDouble(newText);
			} else {
				return null;
			}
		} catch (final NumberFormatException nfe) {
			return null;
		}
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		doSetValue(parse(text.getText()));
	}
}
