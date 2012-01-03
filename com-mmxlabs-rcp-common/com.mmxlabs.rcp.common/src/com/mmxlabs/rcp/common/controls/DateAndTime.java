/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;

/**
 * A combined small-size date-and-time widget,
 * 
 * @author hinton
 * 
 */
public class DateAndTime extends Composite {
	final DateTime date, time;
	boolean settingValue = false;

	public DateAndTime(final Composite parent, final int style,
			final boolean bigDate) {
		super(parent, style);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final RowLayout layout = new RowLayout(bigDate ? SWT.VERTICAL
				: SWT.HORIZONTAL);
		if (!bigDate) {
			layout.marginBottom = 0;
			layout.marginTop = 0;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.spacing = 1;
		}
		setLayout(layout);
		date = new DateTime(this, bigDate ? SWT.CALENDAR | SWT.BORDER
				: SWT.DATE | SWT.MEDIUM);
		time = new DateTime(this, SWT.TIME | SWT.SHORT);

		final SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				notifyNewSelection(false);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				notifyNewSelection(true);
			}
		};

		date.addSelectionListener(listener);
		time.addSelectionListener(listener);

		final KeyListener kl = new KeyListener() {
			private Event toEvent(KeyEvent e) {
				Event e2 = new Event();
				e2.widget = DateAndTime.this;
				e2.character = e.character;
				e2.data = e.data;
				e2.keyCode = e.keyCode;
				e2.keyLocation = e.keyLocation;
				e2.stateMask = e.stateMask;
				e2.display = e.display;
				return e2;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				notifyListeners(SWT.KeyDown, toEvent(e));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				notifyListeners(SWT.KeyUp, toEvent(e));
			}
		};

		time.addKeyListener(kl);
		date.addKeyListener(kl);
	}

	private TimeZone timeZone = TimeZone.getDefault();

	public void setTimeZone(final TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Calendar getValue() {
		final Calendar c = Calendar.getInstance(timeZone); // TODO this is a bit
															// dodgy

		// TODO causes a bug if you getvalue without having previously set a
		// value. grar.

		c.clear();

		c.set(date.getYear(), date.getMonth(), date.getDay(), time.getHours(),
				time.getMinutes());

		return c;
	}

	private void notifyNewSelection(boolean isDefault) {
		if (settingValue)
			return;
		final Event changeEvent = new Event();
		changeEvent.widget = this;

		notifyListeners(isDefault ? SWT.DefaultSelection : SWT.Selection,
				changeEvent);
	}

	public synchronized void setValue(final Calendar newValue) {
		settingValue = true;

		if (newValue != null) {
			timeZone = newValue.getTimeZone();

			date.setYear(newValue.get(Calendar.YEAR));
			date.setMonth(newValue.get(Calendar.MONTH));
			date.setDay(newValue.get(Calendar.DAY_OF_MONTH));

			time.setHours(newValue.get(Calendar.HOUR_OF_DAY));
			time.setMinutes(newValue.get(Calendar.MINUTE));
		}
		settingValue = false;
		notifyNewSelection(false);
	}

	@Override
	public boolean isFocusControl() {
		if (date.isFocusControl() || time.isFocusControl())
			return true;
		return super.isFocusControl();
	}

	@Override
	public boolean setFocus() {
		if (isFocusControl()) {
			return true;
		} else {
			return date.setFocus() || time.setFocus();
		}
	}
}
