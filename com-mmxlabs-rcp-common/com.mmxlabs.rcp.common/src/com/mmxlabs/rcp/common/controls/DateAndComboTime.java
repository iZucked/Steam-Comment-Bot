/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;

/**
 * A combined small-size date widget and a hourly combo time widget. An offset can be specified to set the minutes for each hour interval.
 * 
 * @author Tom Hinton
 * 
 */
public class DateAndComboTime extends Composite {
	private final DateTime date;
	private final Combo time;
	private final Button setTime;
	private int offset;
	boolean settingValue = false;
	private final boolean optionalTime;

	public DateAndComboTime(final Composite parent, final int style, final boolean bigDate, int offset) {
		this(parent, style, bigDate, offset, false);
	}

	public DateAndComboTime(final Composite parent, final int style, final boolean bigDate, int offset, final boolean optionalTime) {
		super(parent, style);
		this.optionalTime = optionalTime;
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final RowLayout layout = new RowLayout(bigDate ? SWT.VERTICAL : SWT.HORIZONTAL);
		if (!bigDate) {
			layout.marginBottom = 0;
			layout.marginTop = 0;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.spacing = 1;
		}
		setLayout(layout);
		date = new DateTime(this, (bigDate ? SWT.CALENDAR | SWT.BORDER : SWT.DATE | SWT.MEDIUM) | SWT.BORDER | style);

		if (optionalTime) {
			setTime = new Button(this, SWT.CHECK);
			final SelectionListener sl = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					time.setEnabled(setTime.getSelection());
					notifyNewSelection(false);
				}
			};
			setTime.addSelectionListener(sl);
			setTime.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					setTime.removeSelectionListener(sl);
				}
			});
		} else {
			setTime = null;
		}

		time = new Combo(this, SWT.BORDER | style | SWT.READ_ONLY);
		this.offset = offset;
		createHourItems(time, offset);

		if (optionalTime) {
			time.setEnabled(false);
		}

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
				e2.widget = DateAndComboTime.this;
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

	public boolean isTimeSet() {
		return setTime.getSelection();
	}

	public synchronized Calendar getValue() {
		final Calendar c = Calendar.getInstance(timeZone); // TODO this is a bit
															// dodgy

		// TODO causes a bug if you getvalue without having previously set a
		// value. grar.

		c.clear();

		final int index = time.getSelectionIndex();

		c.set(date.getYear(), date.getMonth(), date.getDay(), index, offset);

		return c;
	}

	private void notifyNewSelection(boolean isDefault) {
		if (settingValue)
			return;
		final Event changeEvent = new Event();
		changeEvent.widget = this;

		notifyListeners(isDefault ? SWT.DefaultSelection : SWT.Selection, changeEvent);
	}

	public synchronized void setValue(final Calendar newValue) {
		setValue(newValue, true);
	}

	public synchronized void setValue(final Calendar newValue, final boolean isTimeSet) {
		settingValue = true;

		if (newValue != null) {
			timeZone = newValue.getTimeZone();
			if (!date.isDisposed()) {
				date.setYear(newValue.get(Calendar.YEAR));
				date.setMonth(newValue.get(Calendar.MONTH));
				date.setDay(newValue.get(Calendar.DAY_OF_MONTH));
			}
			if (!time.isDisposed()) {
				time.select(newValue.get(Calendar.HOUR_OF_DAY));
				if (optionalTime) {
					time.setEnabled(isTimeSet);
					setTime.setSelection(isTimeSet);
				}
			}
		} else {
			if (!time.isDisposed()) {
				time.select(0);
			}
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

	private void createHourItems(Combo c, int offset) {
		if (offset > 59 || offset < 0) {
			throw new IllegalArgumentException("Time offset should be in the range 0 to 59");
		}
		String[] items = new String[24];
		for (int i = 0; i < 24; ++i) {
			final String offsetStr = String.format("%02d:%02d", i, offset);
			items[i] = offsetStr;
		}
		c.setItems(items);
	}
}
