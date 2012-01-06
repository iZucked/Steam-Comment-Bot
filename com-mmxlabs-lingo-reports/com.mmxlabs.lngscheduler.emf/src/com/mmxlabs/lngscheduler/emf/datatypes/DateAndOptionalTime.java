/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.datatypes;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import scenario.port.Port;

/**
 * A datatype containing a date and a flag for whether the time should be considered set.
 * 
 * @author Tom Hinton
 * 
 */
public class DateAndOptionalTime extends Date {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1L;

	public DateAndOptionalTime(final Date datePart, final boolean isOnlyDate) {
		super(datePart.getTime());
		this.onlyDate = isOnlyDate;
	}

	private boolean onlyDate;

	public boolean isOnlyDate() {
		return onlyDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateAndOptionalTime) {
			final DateAndOptionalTime daot = (DateAndOptionalTime) obj;
			return super.equals(obj) && daot.isOnlyDate() == isOnlyDate();
		}
		return super.equals(obj);
	}

	/**
	 * Get a date equal to this date using the defaults from the given port
	 * 
	 * @param hour
	 * @return
	 */
	public Date getDateWithDefaults(final Port port) {
		if (isOnlyDate()) {
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(port.getTimeZone()));
			calendar.setTime(this);
			calendar.set(Calendar.HOUR_OF_DAY, port.getDefaultWindowStart());
			return calendar.getTime();
		} else {
			return this;
		}
	}
}
