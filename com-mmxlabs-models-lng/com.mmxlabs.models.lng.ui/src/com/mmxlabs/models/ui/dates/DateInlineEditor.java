/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public class DateInlineEditor extends UnsettableInlineEditor {
	private FormattedText formattedText;
	private DateTimeFormatter dateFormatter;

	public DateInlineEditor(EStructuralFeature feature, final DateTimeFormatter formatter) {
		super(feature);
		this.dateFormatter = formatter;
	}

	public DateInlineEditor(final EStructuralFeature feature) {
		this(feature, new DateTimeFormatter("dd/MM/yyyy HH:00"));
	}

	@Override
	protected void updateValueDisplay(Object value) {
		if (formattedText.getControl() == null || formattedText.getControl().isDisposed()) {
			return;
		}
		dateFormatter.setTimeZone(LocalDateUtil.getTimeZone(input, (EAttribute) this.feature));
		formattedText.setValue(value);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return new Date();
	}

	@Override
	protected Control createValueControl(Composite parent) {
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
	public void setEnabled(final boolean enabled) {
		if (formattedText.getControl().isDisposed()) {
			return;
		}
		formattedText.getControl().setEnabled(enabled);

		super.setEnabled(enabled);
	}
}
