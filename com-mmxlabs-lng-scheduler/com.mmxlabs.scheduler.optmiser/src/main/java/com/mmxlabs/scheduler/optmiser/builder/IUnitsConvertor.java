package com.mmxlabs.scheduler.optmiser.builder;

import java.util.Calendar;
import java.util.Date;

public interface IUnitsConvertor {

	int getTimeFromDate(Date date);

	Date getDateFromTime(int time);

	int getTimeFromCalendar(Calendar calendar);

	Calendar getCalendarFromTime(int time);
}
