/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.datetime.ui.formatters.LocalDateTextFormatter;

public class LocalDateInlineEditor extends UnsettableInlineEditor {
	private FormattedText formattedText;
	private LocalDateTextFormatter dateFormatter;

	public LocalDateInlineEditor(final EStructuralFeature feature, final LocalDateTextFormatter formatter) {
		super(feature);
		this.dateFormatter = formatter;
	}

	public LocalDateInlineEditor(final EStructuralFeature feature) {
		this(feature, new LocalDateTextFormatter());
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (formattedText.getControl() == null || formattedText.getControl().isDisposed()) {
			return;
		}
		formattedText.setValue(value);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return LocalDate.now();
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		formattedText = new FormattedText(parent);

		formattedText.setFormatter(dateFormatter);

		formattedText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (formattedText.isValid()) {
					doSetValue((LocalDate) formattedText.getValue(), false);
				}
			}
		});
		return formattedText.getControl();
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {

		final Text control = formattedText.getControl();
		if (control == null || control.isDisposed()) {
			return;
		}
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;
		control.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		if (formattedText.getControl().isDisposed()) {
			return;
		}
		formattedText.getControl().setVisible(visible);

		super.setControlsVisible(visible);
	}

	/**
	 * Change the format string after creation. String will be passed into a {@link DateTimeFormatter}.
	 * 
	 * @param format
	 */
	public void setDateFormat(final String format) {

		dateFormatter = new LocalDateTextFormatter(format);
		formattedText.setFormatter(dateFormatter);
	}
}
