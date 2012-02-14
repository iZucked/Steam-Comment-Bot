package com.mmxlabs.models.lng.types.ui;

import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public class DateInlineEditor extends UnsettableInlineEditor {
	private FormattedText formattedText;

	public DateInlineEditor(EStructuralFeature feature) {
		super(feature);
	}
	
	@Override
	protected void updateValueDisplay(Object value) {
		formattedText.setValue(value); // TODO localize
	}

	@Override
	protected Object getInitialUnsetValue() {
		return new Date(); //TODO zero minutes
	}

	@Override
	protected Control createValueControl(Composite parent) {
		formattedText = new FormattedText(parent);
		formattedText.setFormatter(new DateTimeFormatter());
		
		formattedText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (formattedText.isValid()) {
					doSetValue(localize((Date)formattedText.getValue()));
				}
			}
		});
		return formattedText.getControl();
	}

	/**
	 * Handle timezones
	 * @param value
	 * @return
	 */
	protected Object localize(final Date value) {
		return value;
	}
	
	protected TimeZone getTimeZone() {
		if (input instanceof ITimezoneProvider) {
			return TimeZone.getTimeZone(((ITimezoneProvider) input).getTimeZone((EAttribute) feature));
		} else {
			return TimeZone.getTimeZone("GMT");
		}
	}
}
