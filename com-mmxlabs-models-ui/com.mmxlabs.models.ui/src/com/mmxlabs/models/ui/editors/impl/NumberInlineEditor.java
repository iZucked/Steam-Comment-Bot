/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * TODO look at nebula formattedtext
 * 
 * @author hinton
 * 
 */
public class NumberInlineEditor extends UnsettableInlineEditor implements ModifyListener, DisposeListener {
	private final EDataType type;

	private FormattedText text;
	private Object defaultValue;
	private NumberFormatter formatter;
	private int scale = 1;
	private String unit = null;

	public NumberInlineEditor(final EStructuralFeature feature) {
		super(feature);
		type = (EDataType) feature.getEType();

		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/numberFormat");
		String format = null;
		String defaultValueString = "0";

		if (annotation != null) {
			if (annotation.getDetails().containsKey("defaultValue")) {
				defaultValueString = annotation.getDetails().get("defaultValue");
			}

			if (annotation.getDetails().containsKey("formatString")) {
				format = annotation.getDetails().get("formatString");
			}

			if (annotation.getDetails().containsKey("scale")) {
				scale = Integer.parseInt(annotation.getDetails().get("scale"));
			}

			if (annotation.getDetails().containsKey("unit")) {
				unit = annotation.getDetails().get("unit");
			}
		}

		if (type == EcorePackage.eINSTANCE.getELong()) {
			defaultValue = Long.parseLong(defaultValueString);
			formatter = format == null ? new LongFormatter() : new LongFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			defaultValue = Integer.parseInt(defaultValueString);
			formatter = format == null ? new IntegerFormatter() : new IntegerFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			defaultValue = Float.parseFloat(defaultValueString);
			formatter = format == null ? new FloatFormatter() : new FloatFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			defaultValue = Double.parseDouble(defaultValueString);
			formatter = format == null ? new DoubleFormatter() : new DoubleFormatter(format);
		}
		if (format == null)
			formatter.setFixedLengths(false, false);
	}

	@Override
	public Control createValueControl(Composite parent) {
		if (unit != null) {
			// final Composite sub = new Composite(parent, SWT.NONE);
			final Composite sub = toolkit.createComposite(parent, SWT.NONE);
			final GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			sub.setLayout(layout);
			parent = sub;
		}

		text = new FormattedText(parent, SWT.BORDER);
		text.setFormatter(formatter);
		text.setValue(defaultValue);

		text.getControl().addModifyListener(this);
		text.getControl().addDisposeListener(this);

		// Hook into Forms stuff
		toolkit.adapt(text.getControl(), true, false);

		if (unit != null) {
			// final Label unitLabel = new Label(parent, SWT.NONE);
			final Label unitLabel = toolkit.createLabel(parent, unit, SWT.NONE);
			// unitLabel.setText(unit);
			text.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
			return parent;
		} else {
			return text.getControl();
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (text != null) {
			text.setValue(scale(value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return defaultValue;
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		if (e.getSource() instanceof Text) {
			final Text t = (Text) e.getSource();
			t.removeModifyListener(this);
			t.removeDisposeListener(this);
		}
		text = null;
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		if (text.isValid()) {
			doSetValue(descale(text.getValue()), false);
		}
	}

	private Object scale(final Object internalValue) {
		if (internalValue instanceof Integer) {
			return ((Integer) internalValue).intValue() * scale;
		} else if (internalValue instanceof Long) {
			return ((Long) internalValue).longValue() * scale;
		} else if (internalValue instanceof Float) {
			return ((Float) internalValue).floatValue() * scale;
		} else if (internalValue instanceof Double) {
			return ((Double) internalValue).doubleValue() * scale;
		}
		return internalValue;
	}

	private Object descale(final Object displayValue) {
		if (displayValue instanceof Integer) {
			return ((Integer) displayValue).intValue() / scale;
		} else if (displayValue instanceof Long) {
			return ((Long) displayValue).longValue() / scale;
		} else if (displayValue instanceof Float) {
			return ((Float) displayValue).floatValue() / scale;
		} else if (displayValue instanceof Double) {
			return ((Double) displayValue).doubleValue() / scale;
		}
		return displayValue;
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {

		text.getControl().setEnabled(enabled);

		super.setControlsEnabled(enabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		text.getControl().setVisible(visible);

		super.setControlsVisible(visible);
	}
}
