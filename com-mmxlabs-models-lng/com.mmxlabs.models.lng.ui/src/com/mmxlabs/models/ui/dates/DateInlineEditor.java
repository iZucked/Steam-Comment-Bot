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

	public DateInlineEditor(EStructuralFeature feature) {
		super(feature);
	}
	
	@Override
	protected void updateValueDisplay(Object value) {
		dateFormatter.setTimeZone(LocalDateUtil.getTimeZone(value, (EAttribute) this.feature));
		formattedText.setValue(value);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return new Date(); //TODO zero minutes
	}

	@Override
	protected Control createValueControl(Composite parent) {
		formattedText = new FormattedText(parent);
		//TODO update the date time formatter to fix to zero.
		dateFormatter = new DateTimeFormatter();
		
		formattedText.setFormatter(dateFormatter);
		
		formattedText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (formattedText.isValid()) {
					doSetValue((Date)formattedText.getValue());
				}
			}
		});
		return formattedText.getControl();
	}
}
