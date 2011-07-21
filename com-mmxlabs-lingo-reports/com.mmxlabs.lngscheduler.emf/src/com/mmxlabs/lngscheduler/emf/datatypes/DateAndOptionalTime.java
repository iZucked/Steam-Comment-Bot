/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.datatypes;

import java.util.Date;

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
}
