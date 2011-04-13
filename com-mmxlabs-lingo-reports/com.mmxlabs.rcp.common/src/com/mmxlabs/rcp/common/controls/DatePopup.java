/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


/**
 * A popup composite which contains a date and time.
 * @author hinton
 *
 */
public class DatePopup extends PopupComposite {
	private DateAndTime dateAndTime;
	private DateAndTime popupDateAndTime;
	
	private Calendar value;
	private Listener listener;
	
	private boolean settingValue = false;
	
	public DatePopup(Composite parent, int style) {
		super(parent, style);
	}

	private Listener getListener() {
		if (listener == null) {
			listener = new Listener() {
				@Override
				public void handleEvent(final Event event) {
					if (settingValue) return;
					final DateAndTime source = (DateAndTime) event.widget;
					setValue(source.getValue());
				}
			};
		}
		return listener;
	}
	
	@Override
	protected void createPopupContents(final Shell popup) {
		popup.setLayout (new FillLayout());
		popupDateAndTime = new DateAndTime(popup, SWT.NONE, true);
		popupDateAndTime.addListener(SWT.Selection, getListener());
		popupDateAndTime.addListener(SWT.KeyDown, new Forward(SWT.KeyDown));
		popupDateAndTime.addListener(SWT.KeyUp, new Forward(SWT.KeyUp));
		popupDateAndTime.addListener(SWT.DefaultSelection, new Forward(SWT.DefaultSelection));
		popup.pack();
	}

	private class Forward implements Listener {
		final int et;
		public Forward(int et) {
			this.et = et;
		}
		@Override
		public void handleEvent(Event event) {
			if (isDisposed()) return;
			notifyListeners(et, event);
		}
		
	}
	
	@Override
	protected Composite createSmallWidget(final Composite parent, int style) {
		dateAndTime = new DateAndTime(parent, style, false);
		dateAndTime.addListener(SWT.Selection, getListener());
		
		dateAndTime.addListener(SWT.KeyDown, new Forward(SWT.KeyDown));
		dateAndTime.addListener(SWT.KeyUp, new Forward(SWT.KeyUp));
		dateAndTime.addListener(SWT.DefaultSelection, new Forward(SWT.DefaultSelection));
		
		return dateAndTime;
	}
	
	public Calendar getValue() {
		return value;
	}
	
	public synchronized void setValue(final Calendar value) {
		settingValue = true;
		this.value = value;
		dateAndTime.setValue(value);
		popupDateAndTime.setValue(value);
		settingValue = false;
		Event event = new Event();
		event.widget = this;
		notifyListeners(SWT.Selection, event);
	}
}
