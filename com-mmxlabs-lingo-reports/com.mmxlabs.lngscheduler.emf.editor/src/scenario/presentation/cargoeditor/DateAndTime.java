/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/**
 * A combined date-and-time widget
 * 
 * @author hinton
 * 
 */
public class DateAndTime extends Composite {
	final DateTime date, time;

	public DateAndTime(final Composite parent, final int style) {
		super(parent, style);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.spacing = 1;
		setLayout(layout);
		date = new DateTime(this, SWT.DATE | SWT.MEDIUM);
		time = new DateTime(this, SWT.TIME | SWT.SHORT);

	}

	public Date getValue() {
		final Calendar c = Calendar.getInstance();
		
		c.clear();
		
		c.set(date.getYear(), date.getMonth(), date.getDay(), time.getHours(),
				time.getMinutes());
		
		return c.getTime();
	}
	
	public void setValue(final Date newValue) {
		final Calendar c = Calendar.getInstance();
		c.setTime(newValue);
		
		date.setYear(c.get(Calendar.YEAR));
		date.setMonth(c.get(Calendar.MONTH));
		date.setDay(c.get(Calendar.DAY_OF_MONTH));
		
		time.setHours(c.get(Calendar.HOUR_OF_DAY));
		time.setMinutes(Calendar.MINUTE);
	}
}
