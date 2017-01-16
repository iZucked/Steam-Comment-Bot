/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder;

import java.util.Calendar;
import java.util.Date;

public interface IUnitsConvertor {

	int getTimeFromDate(Date date);

	Date getDateFromTime(int time);

	int getTimeFromCalendar(Calendar calendar);

	Calendar getCalendarFromTime(int time);
}
