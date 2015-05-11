/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public class DateInlineEditor extends UnsettableInlineEditor {
	private FormattedText formattedText;
	private DateTimeFormatter dateFormatter;

	public DateInlineEditor(final EStructuralFeature feature, final DateTimeFormatter formatter) {
		super(feature);
		this.dateFormatter = formatter;
	}

	public DateInlineEditor(final EStructuralFeature feature) {
		this(feature, createDefaultDateFormatter());
	}

	private static DateTimeFormatter createDefaultDateFormatter() {
		final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		if (!(df instanceof SimpleDateFormat)) {
			throw new IllegalArgumentException("No default pattern for locale " + Locale.getDefault().getDisplayName());
		}

		final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) df;
		String datePattern = simpleDateFormat.toPattern();
		// Expand year to 4 digits
		if (!datePattern.contains("yyyy") && datePattern.contains("yy")) {
			datePattern = datePattern.replaceAll("yy", "yyyy");
		}
		// Append time component
		return new DateTimeFormatter(String.format("%s HH:00", datePattern));
	}

	@Override
	protected void updateValueDisplay(final Object value) {

		if (formattedText.getControl() == null || formattedText.getControl().isDisposed()) {
			return;
		}
		dateFormatter.setTimeZone(LocalDateUtil.getTimeZone(input, (EAttribute) this.feature));
		formattedText.setValue(value);
	}

	@Override
	protected Object getInitialUnsetValue() {
		final Calendar cal = Calendar.getInstance(LocalDateUtil.getTimeZone(input, (EAttribute) this.feature));
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		return cal.getTime();
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		formattedText = new FormattedText(parent);

		formattedText.setFormatter(dateFormatter);

		formattedText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (formattedText.isValid()) {
					doSetValue((Date) formattedText.getValue(), false);
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

		dateFormatter = new DateTimeFormatter(format);
		formattedText.setFormatter(dateFormatter);
	}
}
