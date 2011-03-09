/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.widgets;

import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
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
				notifyNewSelection();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				notifyNewSelection();
			}
		};

		date.addSelectionListener(listener);
		time.addSelectionListener(listener);
	}

	private TimeZone timeZone;
	
	public Calendar getValue() {
		final Calendar c = Calendar.getInstance(timeZone); //TODO this is a bit dodgy

		c.clear();

		c.set(date.getYear(), date.getMonth(), date.getDay(), time.getHours(),
				time.getMinutes());

		return c;
	}

	private void notifyNewSelection() {
		if (settingValue)
			return;
		final Event changeEvent = new Event();
		changeEvent.widget = this;
		notifyListeners(SWT.Selection, changeEvent);
	}

	public synchronized void setValue(final Calendar newValue) {
		settingValue = true;

		timeZone = newValue.getTimeZone();
		
		date.setYear(newValue.get(Calendar.YEAR));
		date.setMonth(newValue.get(Calendar.MONTH));
		date.setDay(newValue.get(Calendar.DAY_OF_MONTH));

		time.setHours(newValue.get(Calendar.HOUR_OF_DAY));
		time.setMinutes(newValue.get(Calendar.MINUTE));

		settingValue = false;
		notifyNewSelection();
	}

	@Override
	public boolean isFocusControl() {
		if (date.isFocusControl() || time.isFocusControl()) return true;
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
