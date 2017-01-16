/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.models.lng.types.ITimezoneProvider;

/**
 * Utility class for modifying dates in a localized way
 * 
 * @author hinton
 *
 */
public class LocalDateUtil {
	public static final String DEFAULT_TIMEZONE = "UTC";
	
	/**
	 * Get a {@link TimeZone} for the given field on the given object.
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	public static final TimeZone getTimeZone(final Object object, final EAttribute field) {
		String code = null;
		if (object instanceof ITimezoneProvider) {
			code = ((ITimezoneProvider) object).getTimeZone(field);
		}
		if (code == null) code = DEFAULT_TIMEZONE;
		return TimeZone.getTimeZone(code);
	}
}
