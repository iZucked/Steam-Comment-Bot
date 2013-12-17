package com.mmxlabs.models.ui.properties.ui;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class CalendarFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	private DateFormat dateFormat;
	private boolean showZone;

	public CalendarFormatLabelProvider(final DateFormat df, boolean showZone) {
		this.showZone = showZone;
		dateFormat = df==null ? DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT) : df;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}
	
	@Override
	public String getText(final Object element) {
		if(element instanceof Calendar){			
			Calendar cal = (Calendar) element;
			dateFormat.setTimeZone(cal.getTimeZone());
			dateFormat.setCalendar(cal);
			return dateFormat.format(cal.getTime()) + (showZone ? (" (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		}
		else{
			throw new IllegalArgumentException("Can only format Calendars!");
		}
	}
}
